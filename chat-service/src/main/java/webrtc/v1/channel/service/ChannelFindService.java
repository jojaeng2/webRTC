package webrtc.v1.channel.service;

import webrtc.v1.channel.dto.ChannelDto.FindChannelDto;
import webrtc.v1.channel.dto.ChannelDto.FindMyChannelDto;
import webrtc.v1.channel.entity.Channel;

import java.util.List;

public interface ChannelFindService {

  List<Channel> findAnyChannel(FindChannelDto request);

  List<Channel> findMyChannel(FindMyChannelDto request);


  Channel findById(String id);

  List<Channel> findByName(String tagName, String orderType, int idx);

  List<Channel> findChannelsRecentlyTalk(FindChannelDto request);
}
