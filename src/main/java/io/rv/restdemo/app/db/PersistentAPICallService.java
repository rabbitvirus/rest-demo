package io.rv.restdemo.app.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class PersistentAPICallService implements APICallListener, Runnable {

    private static final Logger LOGGER = LogManager.getLogger(PersistentAPICallService.class);

    private final Map<String, Integer> retryMap = new HashMap<>();
    private final BlockingQueue<String> loginsQ = new LinkedBlockingQueue<>();
    private final RequestStatsRepository repository;

    @Autowired
    public PersistentAPICallService(final RequestStatsRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void startService() {
        var es = Executors.newSingleThreadExecutor();
        es.submit(this);
        es.shutdown();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            String login = null;
            try {
                login = loginsQ.take();
                repository.upsertRequestsStatsFor(login);
            } catch (final InterruptedException e) {
                LOGGER.warn("Interrupted!", e);
                Thread.currentThread().interrupt();
            } catch (final Exception e) {
                var loginF = login;
                LOGGER.error("Got an error processing >>{}<<", () -> loginF);
                retryLater(loginF);
            }
        }
    }

    @Override
    public void registerCallForLogin(final String login) {
        try {
            loginsQ.put(login);
        } catch (final InterruptedException e) {
            LOGGER.warn("Interrupted!", e);
            Thread.currentThread().interrupt();
        }
    }

    private void retryLater(final String login) {
        var rVal = retryMap.merge(login, 1, Integer::sum);
        if (rVal <= 10) {
            LOGGER.debug("Re-adding to the queue - retry iteration: {}", () -> rVal);
            if (!loginsQ.offer(login)) {
                LOGGER.warn("Unfortunately, retry is not possible - skipping update for: {}", () -> login);
                retryMap.remove(login);
            }
        } else {
            LOGGER.warn("Max retry attempts hit - skipping update for: {}", () -> login);
            retryMap.remove(login);
        }
    }
}
