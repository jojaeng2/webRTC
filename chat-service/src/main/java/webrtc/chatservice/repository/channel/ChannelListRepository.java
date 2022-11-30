package webrtc.chatservice.repository.channel;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import webrtc.chatservice.domain.Channel;
import webrtc.chatservice.domain.ChannelHashTag;
import webrtc.chatservice.domain.ChannelUser;
import webrtc.chatservice.domain.HashTag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelListRepository {

    List<Channel> findAnyChannels(int idx, String type);
    List<Channel> findMyChannels(UUID userId, int idx, String type);
    List<Channel> findChannelsByHashName(HashTag hashTag, int idx, String type);

    List<Channel> findChannelsRecentlyTalk(int idx, String type);
}
