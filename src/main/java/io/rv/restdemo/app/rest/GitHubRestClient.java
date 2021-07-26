package io.rv.restdemo.app.rest;

import io.rv.restdemo.app.domain.GitHubUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class GitHubRestClient implements GitHubClient {

    private static final Logger LOGGER = LogManager.getLogger(GitHubRestClient.class);
    private static final String GITHUB_USERS_BASE = "https://api.github.com/users/";

    private final RetryTemplate httpRetryTemplate;
    private final RestTemplate restTemplate;

    @Autowired
    public GitHubRestClient(@Qualifier("httpRetryTemplate") final RetryTemplate httpRetryTemplate,
                            final RestTemplate restTemplate) {
        this.httpRetryTemplate = httpRetryTemplate;
        this.restTemplate = restTemplate;
    }

    /**
     * {@inheritDoc}
     * Calls GitHub REST API underneath.
     *
     * @param user name of the user for which GET call should be made
     * @return {@link GitHubUser} data or <code>null</code> if the data cannot be obtained
     */
    @Override
    public GitHubUser getGitHubUser(final String user) {
        final GitHubUser ghUser = obtainUserDetails(user);
        LOGGER.info("Got the following user data: {} for user name: {}", () -> ghUser, () -> user);
        return ghUser;
    }

    private GitHubUser obtainUserDetails(final String user) {
        GitHubUser ghUser = null;
        try {
            ghUser = httpRetryTemplate.execute((RetryCallback<GitHubUser, RestClientException>) retryContext -> {
                LOGGER.debug("About to make GET call to obtain GitHub user details for: {}", () -> user);
                return restTemplate.getForObject(createURIForLogin(user), GitHubUser.class);
            });
        } catch (final Exception x) {
            LOGGER.error("Could not obtain GitHub user data for " + user, x);
        }
        return ghUser;
    }

    private static URI createURIForLogin(final String login) {
        return UriComponentsBuilder.fromHttpUrl(GITHUB_USERS_BASE)
                .path(login)
                .build()
                .toUri();
    }

}
