package webrtc.chatservice.service.chat.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import webrtc.chatservice.domain.Users;
import webrtc.chatservice.dto.chat.*;
import webrtc.chatservice.enums.ClientMessageType;
import webrtc.chatservice.service.channel.ChannelIOService;
import webrtc.chatservice.service.chat.template.MessageInsertTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static webrtc.chatservice.enums.ClientMessageType.*;

@Component
@RequiredArgsConstructor
public class SocketMessageFactoryImpl implements SocketMessageFactory{

    private final ChannelIOService channelIOService;
    private final Map<ClientMessageType, MessageInsertTemplate> messageTypes = new HashMap<>();

    @PostConstruct
    public void messageFactoryConst() {
        this.messageTypes.put(CHAT, (message, user, channelId) -> message.setSenderName(user.getNickname()));
        this.messageTypes.put(ENTER, (message, user, channelId) -> message.setMessage("[알림] " + user.getNickname()+ " 님이 채팅방에 입장했습니다."));
        this.messageTypes.put(EXIT, (message, user, channelId) -> {
            channelIOService.exitChannel(channelId, user.getId());
            message.setMessage("[알림] " + user.getNickname() + " 님이 채팅방에서 퇴장했습니다.");
        });
    }

    public void execute(ClientMessageType type, ClientMessage overallMessage, Users user, String channelId) {
        this.messageTypes.get(type).execute(overallMessage, user, channelId);
    }
}
