package webrtc.chatservice.service.channel;

import webrtc.chatservice.domain.Channel;
import webrtc.chatservice.dto.ChannelDto;
import webrtc.chatservice.dto.ChannelDto.ChannelResponse;

import java.util.List;

public interface ChannelInfoInjectService {

    List<ChannelResponse> setReturnChannelsTTL(List<Channel> channels);
}
