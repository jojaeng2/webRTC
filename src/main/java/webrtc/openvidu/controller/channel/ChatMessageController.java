package webrtc.openvidu.controller.channel;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import webrtc.openvidu.domain.Channel;
import webrtc.openvidu.dto.ChatDto.*;
import webrtc.openvidu.enums.ChannelServiceReturnType;
import webrtc.openvidu.enums.ClientMessageType;
import webrtc.openvidu.repository.ChannelRepository;
import webrtc.openvidu.service.channel.ChannelService;
import webrtc.openvidu.service.chat.ChatService;

import static webrtc.openvidu.enums.SocketServerMessageType.RENEWAL;


@RequiredArgsConstructor
@Controller
public class ChatMessageController {

    private final ChannelService channelService;
    private final ChatService chatService;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final ChannelRepository channelRepository;

    /**
     * /pub/chat/room 으로 오는 메시지 반환
     */
    @MessageMapping("/chat/room")
    public void message(ClientMessage message, @Header("jwt") String jwtToken) {
        System.out.println(jwtToken);
        System.out.println("ChatMessageController message Method");
        ClientMessageType clientMessageType = message.getType();
        String channelId = message.getChannelId();
        String senderName = message.getSenderName();
        String chatMessage = message.getMessage();
        System.out.println("senderName = " + senderName);
        System.out.println("chatMessage = " + chatMessage);
        System.out.println("channelId = " + channelId);
        switch(clientMessageType) {
            case CHAT:
                message.setSenderName(senderName);
                break;
            case ENTER:
                channelService.enterChannel(channelId, senderName);
                break;
            case EXIT:
                channelService.exitChannel(channelId, senderName);
                break;
        }
        chatService.sendChatMessage(clientMessageType, channelId, senderName, chatMessage);
    }
}
