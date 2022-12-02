package webrtc.v1.repository.channel;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import webrtc.v1.domain.Channel;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class ChannelRedisRepositoryImpl implements ChannelRedisRepository{

    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> opsValueOperation;
    private final long channelTTL = 60L * 60L;

    @PostConstruct
    private void init() {
        opsValueOperation = redisTemplate.opsForValue();
    }

    public void save(Channel channel) {
        opsValueOperation.set(channel.getId(), channel);
        redisTemplate.expire(channel.getId(), channelTTL, TimeUnit.SECONDS);
    }


    public Long findTtl(String channelId) {
        return redisTemplate.getExpire(channelId);
    }

    public void extensionTtl(Channel channel, Long requestTTL) {
        long newTTL = findTtl(channel.getId()) + requestTTL;
        channel.setTimeToLive(newTTL);
        redisTemplate.expire(channel.getId(), newTTL, TimeUnit.SECONDS);
    }

    public void delete(String channelId) {
        redisTemplate.delete(channelId);
    }
}
