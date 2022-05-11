package webrtc.openvidu.service.channel;

import webrtc.openvidu.domain.ChannelUser;

public interface ChannelUserService {

    void save(ChannelUser channelUser);

    void delete(ChannelUser channelUser);

    ChannelUser findOneChannelUser(String channelId, String userId);
}
