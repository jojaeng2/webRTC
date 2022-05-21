package webrtc.openvidu.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import webrtc.openvidu.domain.Channel;
import webrtc.openvidu.domain.ChatLog;
import webrtc.openvidu.domain.User;
import webrtc.openvidu.dto.ChatDto.ChatServerMessage;
import webrtc.openvidu.enums.ChatEnumType;
import webrtc.openvidu.enums.ClientMessageType;
import webrtc.openvidu.enums.SocketServerMessageType;
import webrtc.openvidu.repository.chat.ChatRepository;
import webrtc.openvidu.service.channel.ChannelService;
import webrtc.openvidu.service.user.UserService;

import java.util.List;

import static webrtc.openvidu.enums.ChatEnumType.CHAT;
import static webrtc.openvidu.enums.ChatEnumType.NOTICE;
import static webrtc.openvidu.enums.SocketServerMessageType.*;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService{

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    private final ChatRepository chatRepository;

    private final ChannelService channelService;
    private final UserService userService;

    public void saveChatMessage(ChatEnumType type, String chatMessage, String username, Channel channel) {
        ChatLog chatLog = new ChatLog(type, chatMessage, username);
        chatLog.setChannel(channel);
        chatRepository.save(chatLog);
    }

    /**
     * Chatting Room에 message 발송
     */
    public void sendChatMessage(ClientMessageType type, String channelId, String senderName, String chatMessage) {
        Channel channel = channelService.findOneChannelById(channelId);
        Long currentParticipants = channel.getCurrentParticipants();
        ChatServerMessage serverMessage = new ChatServerMessage(channelId);
        ChatEnumType enumType = NOTICE;
        List<User> currentUsers = userService.findUsersByChannelId(channelId);
        switch (type) {
            case CHAT:
                enumType = CHAT;
                serverMessage.setMessageType(SocketServerMessageType.CHAT, senderName, chatMessage, currentParticipants, currentUsers);
                break;
            case ENTER:
                chatMessage = senderName+ " 님이 채팅방에 입장했습니다.";
                serverMessage.setMessageType(RENEWAL, senderName, chatMessage, currentParticipants, currentUsers);
                break;
            case EXIT:
                chatMessage = senderName+ " 님이 채팅방에서 퇴장했습니다.";
                serverMessage.setMessageType(RENEWAL, senderName, senderName+ " 님이 채팅방에서 퇴장했습니다.", currentParticipants, currentUsers);
                break;
            case CLOSE:
                serverMessage.setMessageType(CLOSE, senderName, chatMessage, currentParticipants, currentUsers);
        }
        saveChatMessage(enumType, chatMessage, senderName, channel);
        redisTemplate.convertAndSend(channelTopic.getTopic(), serverMessage);
    }

    public List<ChatLog> findChatLogsByIndex(String channelId, int idx) {
        return chatRepository.findChatLogsByChannelId(channelId, idx);
    }
}
