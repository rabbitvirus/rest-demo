package io.rv.restdemo.app.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:test.properties")
public class RequestStatsRepositoryTest {

    private static final String TEST_USER_1 = "rabbitvirus";
    private static final String TEST_USER_2 = "octocat";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RequestStatsRepository repository;

    @Test
    public void shouldInsertNewLoginIfItDoesNotExist() {
        assertNull(getRequestsCount(TEST_USER_1));

        repository.upsertRequestsStatsFor(TEST_USER_1);

        assertEquals(1L, getRequestsCount(TEST_USER_1));
    }

    @Test
    public void shouldIncrementLoginCounts() {
        assertNull(getRequestsCount(TEST_USER_1));

        repository.upsertRequestsStatsFor(TEST_USER_1);
        repository.upsertRequestsStatsFor(TEST_USER_1);
        repository.upsertRequestsStatsFor(TEST_USER_1);
        repository.upsertRequestsStatsFor(TEST_USER_1);
        repository.upsertRequestsStatsFor(TEST_USER_1);

        assertEquals(5L, getRequestsCount(TEST_USER_1));
    }

    @Test
    public void shouldInsertMultipleUsers() {
        assertNull(getRequestsCount(TEST_USER_1));
        assertNull(getRequestsCount(TEST_USER_2));

        repository.upsertRequestsStatsFor(TEST_USER_1);
        repository.upsertRequestsStatsFor(TEST_USER_2);
        repository.upsertRequestsStatsFor(TEST_USER_1);

        assertEquals(2L, getRequestsCount(TEST_USER_1));
        assertEquals(1L, getRequestsCount(TEST_USER_2));
    }

    private Long getRequestsCount(final String login) {
        Query query = entityManager.createQuery("FROM RequestsStats r where LOGIN = :login", RequestsStats.class);
        query.setParameter("login", login);

        try {
            return ((RequestsStats) query.getSingleResult()).getRequestCount();
        } catch (NoResultException x) {
            return null;
        }
    }
}