package webrtc.openvidu.repository.hashtag;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import webrtc.openvidu.domain.HashTag;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class HashTagRepositoryImpl implements HashTagRepository{

    @PersistenceContext
    private EntityManager em;

    public void save(HashTag hashTag) {
        em.persist(hashTag);
    }

    public HashTag findHashTagById(Long id) {
        return em.find(HashTag.class, id);
    }

    public List<HashTag> findHashTagByName(String tagName) {
        return em.createQuery("select h from HashTag h where h.tagName = :tagName", HashTag.class)
                    .setParameter("tagName", tagName)
                    .getResultList();
    }
}
