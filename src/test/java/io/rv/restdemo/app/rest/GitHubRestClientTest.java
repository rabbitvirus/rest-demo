package io.rv.restdemo.app.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.web.client.MockRestServiceServer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest(GitHubRestClient.class)
@AutoConfigureWebClient(registerRestTemplate = true)
public class GitHubRestClientTest {

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private GitHubRestClient gitHubRestClient;

    @Test
    public void shouldReturnUserOnSuccessfulResponseFromServer() throws IOException, URISyntaxException {
        mockServer.expect(requestTo("https://api.github.com/users/octocat"))
                .andRespond(withSuccess(
                        Files.readString(Paths.get(ClassLoader.getSystemResource("io/rv/restdemo/app/rest/octocat.json").toURI())),
                        MediaType.APPLICATION_JSON));

        var respUser = gitHubRestClient.getGitHubUser("octocat");

        assertNotNull(respUser);
        assertEquals(583231, respUser.getId());
        assertEquals("octocat", respUser.getLogin());
        assertEquals("The Octocat", respUser.getName());
        assertEquals("User", respUser.getType());
        assertEquals("https://avatars.githubusercontent.com/u/583231?v=4", respUser.getAvatarURL().toString());
        assertEquals(ZonedDateTime.parse("2011-01-25T18:44:36Z[UTC]"), respUser.getCreatedAt());
        assertEquals(3879, respUser.getFollowersCount());
        assertEquals(8, respUser.getPublicRepos());
    }

    @Test
    public void shouldReturnNullOnNotFoundResponseFromServer() {
        mockServer.expect(requestTo("https://api.github.com/users/user-that-does-not-exist"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        var respUser = gitHubRestClient.getGitHubUser("user-that-does-not-exist");

        assertNull(respUser);
    }

    @Test
    public void shouldReturnNullOnInternalServerErrorResponseFromServer() {
        mockServer.expect(requestTo("https://api.github.com/users/ise-user"))
                .andRespond(withServerError());

        var respUser = gitHubRestClient.getGitHubUser("ise-user");

        assertNull(respUser);
    }


    @TestConfiguration
    public static class TestRestClientConfig {

        @Bean
        public RetryTemplate httpRetryTemplate() {
            var retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(new NeverRetryPolicy());
            return retryTemplate;
        }
    }
}
