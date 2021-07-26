package io.rv.restdemo.app.db;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class RequestStatsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void upsertRequestsStatsFor(final String login) {
        var en = entityManager.find(RequestsStats.class, login);
        if (en == null) {
            en = new RequestsStats(login);
        }

        en.setRequestCount(en.getRequestCount() + 1);
        entityManager.persist(en);
        entityManager.flush();
    }

}
