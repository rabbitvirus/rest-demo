package io.rv.restdemo.app.domain;

import org.junit.jupiter.api.Test;

import java.net.URL;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class GitHubToDemoUserConverterTest {

    private static final int TEST_ID = 57966879;
    private static final String TEST_LOGIN = "rabbitvirus";
    private static final String TEST_NAME = "Rabbit Virus";
    private static final String TEST_TYPE = "User";
    private static final URL TEST_AVATAR_URL = URLUtil.createURL("https://avatars.githubusercontent.com/u/57966879?v=4");
    private static final ZonedDateTime TEST_CREATED_AT = ZonedDateTime.parse("2019-11-19T21:40:06Z");

    @Test
    public void shouldNotCreateConverterWithNullCalculationsHelper() {
        assertThrows(NullPointerException.class, () -> new GitHubToDemoUserConverter(null));
    }

    @Test
    public void shouldPerformConversion() {
        var calcHelperMock = mock(CalculationsHelper.class);
        when(calcHelperMock.calculateFrom(eq(2), eq(1))).thenReturn(9.0d);

        var ghUser = createGitHubTestUser();
        var converter = new GitHubToDemoUserConverter(calcHelperMock);

        var actDemoUser = converter.convertToDemoUser(ghUser);

        assertNotNull(actDemoUser);
        assertEquals(TEST_ID, actDemoUser.getId());
        assertEquals(TEST_LOGIN, actDemoUser.getLogin());
        assertEquals(TEST_NAME, actDemoUser.getName());
        assertEquals(TEST_TYPE, actDemoUser.getType());
        assertEquals(TEST_AVATAR_URL, actDemoUser.getAvatarUrl());
        assertEquals(TEST_CREATED_AT, actDemoUser.getCreatedAt());
        assertEquals(9.0d, actDemoUser.getCalculations(), 1e-6d);
    }

    private static GitHubUser createGitHubTestUser() {
        return GitHubUser.Builder.newBuilderFor(TEST_ID, TEST_LOGIN)
                .withName(TEST_NAME)
                .withAvatarURL(TEST_AVATAR_URL)
                .withType(TEST_TYPE)
                .withCreatedAt(TEST_CREATED_AT)
                .withFollowersCount(2)
                .withPublicRepos(1)
                .build();
    }

}
