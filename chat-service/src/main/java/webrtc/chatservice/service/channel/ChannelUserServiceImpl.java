package webrtc.chatservice.service.channel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webrtc.chatservice.domain.ChannelUser;
import webrtc.chatservice.exception.ChannelUserException;
import webrtc.chatservice.exception.ChannelUserException.NotExistChannelUserException;
import webrtc.chatservice.repository.channel.ChannelUserRepository;

@RequiredArgsConstructor
@Service
public class ChannelUserServiceImpl implements ChannelUserService{

    private final ChannelUserRepository channelUserRepository;

    @Transactional(readOnly = true)
    public ChannelUser findOneChannelUser(String channelId, String userId) {
        return channelUserRepository.findOneChannelUser(channelId, userId).orElseThrow(NotExistChannelUserException::new);
    }
}
