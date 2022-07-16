package webrtc.chatservice.repository.channel;

import org.springframework.stereotype.Repository;
import webrtc.chatservice.domain.ChannelUser;
import webrtc.chatservice.exception.ChannelUserException.NotExistChannelUserException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ChannelUserRepositoryImpl implements ChannelUserRepository{

    @PersistenceContext
    private EntityManager em;

    public void save(ChannelUser channelUser) {
        em.persist(channelUser);
    }


    public void delete(ChannelUser channelUser) {
        ChannelUser findChannelUser = em.find(ChannelUser.class, channelUser.getId());
        em.remove(findChannelUser);
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
