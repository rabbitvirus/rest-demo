package io.rv.restdemo.app.rest;

import io.rv.restdemo.app.db.APICallListener;
import io.rv.restdemo.app.domain.CalculationsHelper;
import io.rv.restdemo.app.domain.GitHubToDemoUserConverter;
import io.rv.restdemo.app.domain.GitHubUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UsersController.class)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubClient gitHubClient;

    @MockBean
    private APICallListener callListener;

    @SpyBean
    private CalculationsHelper calculationsHelper;

    @SpyBean
    private GitHubToDemoUserConverter converter;

    @Test
    public void shouldReturnNotFoundWhenUserWasNotReturnedByClient() throws Exception {
        mockMvc.perform(get("/users/user-that-does-not-exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotifyAPICallListenerOnClientCall() throws Exception {
        mockMvc.perform(get("/users/some-user"))
                .andExpect(status().isNotFound());

        verify(callListener, times(1)).registerCallForLogin(eq("some-user"));
    }

    @Test
    public void shouldReturnDemoUserWhenItIsFoundByClient() throws Exception {
        when(gitHubClient.getGitHubUser(eq("octocat")))
                .thenReturn(GitHubUser.Builder.newBuilderFor(583231, "octocat")
                        .withName("The Octocat")
                        .withAvatarURL(new URL("https://avatars.githubusercontent.com/u/583231?v=4"))
                        .withType("User")
                        .withCreatedAt(ZonedDateTime.parse("2011-01-25T18:44:36Z"))
                        .withFollowersCount(3879)
                        .withPublicRepos(8)
                        .build());

        mockMvc.perform(get("/users/octocat"))
                .andExpect(status().isOk())
                .andExpect(content().json(Files.readString(Paths.get(ClassLoader.getSystemResource("io/rv/restdemo/app/rest/demo-octocat-resp.json").toURI()))));
    }

}
