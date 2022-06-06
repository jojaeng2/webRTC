package webrtc.openvidu.repository.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import webrtc.openvidu.domain.Point;
import webrtc.openvidu.domain.User;
import webrtc.openvidu.dto.ChannelDto.CreateChannelRequest;
import webrtc.openvidu.repository.user.UserRepository;
import webrtc.openvidu.service.channel.ChannelService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PointRepositoryImplTest {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PointRepository pointRepository;

    @BeforeEach
    public void saveChannelAndUser() {
        List<String> hashTags = new ArrayList<>();
        hashTags.add("tag1");
        hashTags.add("tag2");
        hashTags.add("tag2");

        CreateChannelRequest request = new CreateChannelRequest("testChannel", hashTags);
        User user = new User("testUser", "testUser", "email1");
        userRepository.saveUser(user);
        channelService.createChannel(request, "email1");
    }

    @Test
    @DisplayName("userEmail으로 Point 객체 찾기")
    public void findPointByUserEmail() {
        // given

        // when
        Point findPoint = pointRepository.findPointByUserEmail("email1");

        // then
        assertThat(findPoint.getPoint()).isEqualTo(1000000);
    }

    @Test
    @DisplayName("Point 객체 point 감소 성공")
    public void decreasePointSuccess(){
        // given
        Point findPoint = pointRepository.findPointByUserEmail("email1");

        // when
        pointRepository.decreasePoint(findPoint.getId(), 10000L);

        Point reFindPoint = pointRepository.findPointByUserEmail("email1");

        // then
        assertThat(findPoint.getPoint()).isEqualTo(990000L);
        assertThat(reFindPoint.getPoint()).isEqualTo(990000L);
    }

}
