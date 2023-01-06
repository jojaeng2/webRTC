package webrtc.v1.service.channel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webrtc.v1.channel.dto.ChannelDto;
import webrtc.v1.channel.dto.ChannelDto.FindMyChannelDto;
import webrtc.v1.channel.entity.Channel;
import webrtc.v1.channel.service.ChannelFindServiceImpl;
import webrtc.v1.channel.service.ChannelInfoInjectService;
import webrtc.v1.hashtag.entity.HashTag;
import webrtc.v1.user.entity.Users;
import webrtc.v1.channel.dto.ChannelDto.ChannelResponse;
import webrtc.v1.channel.enums.ChannelType;
import webrtc.v1.channel.exception.ChannelException.NotExistChannelException;
import webrtc.v1.hashtag.exception.HashTagException.NotExistHashTagException;
import webrtc.v1.user.exception.UserException.NotExistUserException;
import webrtc.v1.channel.repository.ChannelCrudRepository;
import webrtc.v1.channel.repository.ChannelListRepository;
import webrtc.v1.hashtag.repository.HashTagRepository;
import webrtc.v1.user.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static webrtc.v1.channel.enums.ChannelType.TEXT;

@ExtendWith(MockitoExtension.class)
public class ChannelFindServiceTest {

    @InjectMocks
    private ChannelFindServiceImpl channelFindService;

    @Mock
    private ChannelListRepository channelListRepository;
    @Mock
    private ChannelCrudRepository channelCrudRepository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private HashTagRepository hashTagRepository;

    @Mock
    private ChannelInfoInjectService channelInfoInjectService;

    String nickname1 = "nickname1";
    String password = "password";
    String email1 = "email1";
    String channelName1 = "channelName1";
    String tag1 = "tagname";

    ChannelType text = TEXT;

    int mini = 14;
    int maxi = 20;

    @Test
    void 채널찾기성공() {
        // given
        Channel channel = createChannel(channelName1, text);

        doReturn(Optional.of(channel))
                .when(channelCrudRepository).findById(any(String.class));
        doReturn(channel)
                .when(channelInfoInjectService).setTtl(any(Channel.class));

        // when
        Channel findChannel = channelFindService.findById(channel.getId());

        // then
        assertThat(channel.getId()).isEqualTo(findChannel.getId());
    }

    @Test
    void 채널찾기실패채널없음() {
        // given
        Channel channel = createChannel(channelName1, text);

        doReturn(Optional.empty())
                .when(channelCrudRepository).findById(any(String.class));

        // when

        // then
        assertThrows(NotExistChannelException.class, () -> channelFindService.findById(channel.getId()));
    }

//    @Test
//    void 모든채널목록성공20개미만() {
//        // given
//        doReturn(channelListUnder20())
//                .when(channelListRepository).findAnyChannels(any(Integer.class), any(String.class));
//
//        doReturn(channelResponseList())
//                .when(channelInfoInjectService).setReturnChannelsTTL(any(Channel.class));
//        // when
//        List<ChannelResponse> result = channelFindService.findAnyChannel("partiASC", 0);
//
//        // then
//        assertThat(result.size()).isEqualTo(mini);
//    }

//    @Test
//    void 모든채널목록성공20개이상() {
//        // given
//        doReturn(channelList20())
//                .when(channelListRepository).findAnyChannels(any(Integer.class), any(String.class));
//        doReturn(channelResponseList(channelList20()))
//                .when(channelInfoInjectService).setReturnChannelsTTL(any(Channel.class));
//        // when
//        List<ChannelResponse> result = channelFindService.findAnyChannel("partiASC", 0);
//
//        // then
//        assertThat(result.size()).isEqualTo(maxi);
//
//    }

//    @Test
//    void 나의채널목록성공20개미만() {
//        // given
//        doReturn(Optional.of(createUser()))
//                .when(usersRepository).findUserByEmail(any(String.class));
//
//        doReturn(channelListUnder20())
//                .when(channelListRepository).findMyChannels(any(String.class), any(Integer.class), any(String.class));
//        doReturn(channelResponseList(channelListUnder20()))
//                .when(channelInfoInjectService).setReturnChannelsTTL(any(Channel.class));
//        // when
//        List<ChannelResponse> result = channelFindService.findMyChannel("partiASC", email1, 0);
//
//        // then
//        assertThat(result.size()).isEqualTo(mini);
//
//    }
//
//    @Test
//    void 나의채널목록성공20개이상() {
//        // given
//        doReturn(Optional.of(createUser()))
//                .when(usersRepository).findUserByEmail(any(String.class));
//        doReturn(channelList20())
//                .when(channelListRepository).findMyChannels(any(String.class), any(Integer.class), any(String.class));
//        doReturn(channelResponseList(channelList20()))
//                .when(channelInfoInjectService).setReturnChannelsTTL(any(Channel.class));
//        // when
//        List<ChannelResponse> result = channelFindService.findMyChannel("partiASC", email1, 0);
//
//        // then
//        assertThat(result.size()).isEqualTo(maxi);
//    }

    @Test
    void 나의채널목록실패유저없음() {
        // given
        doReturn(Optional.empty())
                .when(usersRepository).findById(any(String.class));

        // when


        // then
        assertThrows(NotExistUserException.class, () -> channelFindService.findMyChannel(new FindMyChannelDto("partiASC", "1", 0)));
    }

//    @Test
//    void 해시태그채널목록성공20개미만() {
//        // given
//        doReturn(Optional.of(createHashTag()))
//                .when(hashTagRepository).findHashTagByName(any(String.class));
//        doReturn(channelListUnder20())
//                .when(channelListRepository).findChannelsByHashName(any(HashTag.class), any(Integer.class), any(String.class));
//        doReturn(channelResponseList(channelListUnder20()))
//                .when(channelInfoInjectService).setReturnChannelsTTL(any(Channel.class));
//        // when
//        List<ChannelResponse> result = channelFindService.findChannelByHashName(tag1, "partiASC", 0);
//
//        // then
//        assertThat(result.size()).isEqualTo(mini);
//    }
//
//    @Test
//    void 해시태그채널목록성공20개이상() {
//        // given
//        doReturn(Optional.of(createHashTag()))
//                .when(hashTagRepository).findHashTagByName(any(String.class));
//        doReturn(channelList20())
//                .when(channelListRepository).findChannelsByHashName(any(HashTag.class), any(Integer.class), any(String.class));
//        doReturn(channelResponseList(channelList20()))
//                .when(channelInfoInjectService).setReturnChannelsTTL(any(Channel.class));
//        // when
//        List<ChannelResponse> result = channelFindService.findChannelByHashName(tag1, "partiASC", 0);
//
//        // then
//        assertThat(result.size()).isEqualTo(maxi);
//
//    }

    @Test
    void 해시태그채널목록실패태그없음() {
        // given
        doReturn(Optional.empty())
                .when(hashTagRepository).findByName(any(String.class));

        // when

        // then
        assertThrows(NotExistHashTagException.class, () -> channelFindService.findByName(tag1, "partiASC", 0));

    }

    private Users createUser() {
        return Users.builder()
                .nickname(nickname1)
                .password(password)
                .email(email1)
                .build();
    }

    private Channel createChannel(String name, ChannelType type) {
        return Channel.builder()
                .channelName(name)
                .channelType(type)
                .build();
    }

    private List<Channel> channelListUnder20() {
        List<Channel> channels = new ArrayList<>();
        for(int i=0; i<mini; i++) {
            Channel channel = Channel.builder()
                    .channelName(i + " Channel")
                    .channelType(text)
                    .build();
            channels.add(channel);
        }
        return channels;
    }

    private List<Channel> channelList20() {
        List<Channel> channels = new ArrayList<>();
        for(int i=0; i<maxi; i++) {
            Channel channel = Channel.builder()
                    .channelName(i + " Channel")
                    .channelType(text)
                    .build();
            channels.add(channel);
        }
        return channels;
    }

    private ChannelResponse channelResponseList(Channel channel) {
        return new ChannelResponse(channel);
    }

    private HashTag createHashTag() {
        return HashTag.builder()
                .name(tag1)
                .build();
    }
}
