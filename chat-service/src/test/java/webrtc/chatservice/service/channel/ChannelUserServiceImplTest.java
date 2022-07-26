package webrtc.chatservice.service.channel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import webrtc.chatservice.domain.Channel;
import webrtc.chatservice.domain.ChannelUser;
import webrtc.chatservice.domain.User;
import webrtc.chatservice.enums.ChannelType;
import webrtc.chatservice.exception.ChannelUserException.NotExistChannelUserException;
import webrtc.chatservice.repository.channel.ChannelDBRepository;
import webrtc.chatservice.repository.channel.ChannelUserRepository;
import webrtc.chatservice.repository.user.UserRepository;
import webrtc.chatservice.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static webrtc.chatservice.enums.ChannelType.TEXT;
import static webrtc.chatservice.enums.ChannelType.VOIP;

@ExtendWith(MockitoExtension.class)
public class ChannelUserServiceImplTest {

    @InjectMocks
    private ChannelUserServiceImpl channelUserService;
    @Mock private ChannelUserRepository channelUserRepository;

    String nickname1 = "nickname1";
    String nickname2 = "nickname2";
    String password = "password";
    String email1 = "email1";
    String email2 = "email2";
    String channelName1 = "channelName1";
    String tag1 = "tag1";
    String tag2 = "tag2";
    String tag3 = "tag3";
    ChannelType text = TEXT;

    @Test
    @Transactional
    public void 채널유저_조회성공() {
        // given
        Channel channel = new Channel(channelName1, text);
        User user = new User(nickname1, password, email1);
        doReturn(new ChannelUser(user, channel))
                .when(channelUserRepository)
                .findOneChannelUser(channel.getId(), user.getId());

        // when
        ChannelUser channelUser = channelUserService.findOneChannelUser(channel.getId(), user.getId());

        // then
        assertThat(channelUser.getChannel().getId()).isEqualTo(channel.getId());
        assertThat(channelUser.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @Transactional
    public void 채널유저_조회실패() {
        // given
        Channel channel = new Channel(channelName1, text);
        User user = new User(nickname1, password, email1);

        doThrow(new NotExistChannelUserException())
                .when(channelUserRepository)
                .findOneChannelUser(channel.getId(), user.getId());

        // when

        // then
        Assertions.assertThrows(NotExistChannelUserException.class, ()-> {
           channelUserRepository.findOneChannelUser(channel.getId(), user.getId());
        });
    }

}
