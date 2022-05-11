package webrtc.openvidu.repository.channel;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import webrtc.openvidu.domain.ChannelUser;
import webrtc.openvidu.exception.ChannelUserException;
import webrtc.openvidu.exception.ChannelUserException.NotExistChannelUserException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ChannelUserRepositoryImpl implements ChannelUserRepository{

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(ChannelUser channelUser) {
        System.out.println("ChannelUserRepositoryImpl = " + channelUser.getChannel());
        em.persist(channelUser);
    }


    @Transactional
    public void delete(ChannelUser channelUser) {
        em.remove(channelUser);
    }

    public ChannelUser findOneChannelUser(String channelId, String userId) {
        try {
            return em.createQuery(
                    "select cu from ChannelUser cu " +
                            "where channel_id = :channel_id " +
                            "and user_id = :user_id", ChannelUser.class)
                    .setParameter("channel_id", channelId)
                    .setParameter("user_id", userId)
                    .getSingleResult();
        } catch(NoResultException e) {
            throw new NotExistChannelUserException();
        }
    }
}
