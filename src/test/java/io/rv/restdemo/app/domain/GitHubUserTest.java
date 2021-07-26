package io.rv.restdemo.app.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GitHubUserTest {

    private static final int TEST_ID = 583231;
    private static final String TEST_LOGIN = "octocat";
    private static final String TEST_NAME = "The Octocat";
    private static final String TEST_TYPE = "User";
    private static final URL TEST_AVATAR_URL = URLUtil.createURL("https://avatars.githubusercontent.com/u/583231?v=4");
    private static final ZonedDateTime TEST_CREATED_AT = ZonedDateTime.parse("2011-01-25T18:44:36Z");
    private static final int TEST_FOLLOWERS_COUNT = 3877;
    private static final int TEST_PUBLIC_REPOS = 8;


    @ParameterizedTest
    @MethodSource("provideAssertionParams")
    public void shouldCreateGitHubUserWithCorrectFields(final Function<GitHubUser, ?> fieldExtractor, final Object expectedFieldVal) {
        var user = GitHubUser.Builder.newBuilderFor(TEST_ID, TEST_LOGIN)
                .withName(TEST_NAME)
                .withType(TEST_TYPE)
                .withAvatarURL(TEST_AVATAR_URL)
                .withCreatedAt(TEST_CREATED_AT)
                .withFollowersCount(TEST_FOLLOWERS_COUNT)
                .withPublicRepos(TEST_PUBLIC_REPOS)
                .build();

        var actualFieldVal = fieldExtractor.apply(user);

        assertEquals(expectedFieldVal, actualFieldVal);
    }


    private static Stream<Arguments> provideAssertionParams() {
        return Stream.of(
                Arguments.of((Function<GitHubUser, ?>) GitHubUser::getId, TEST_ID),
                Arguments.of((Function<GitHubUser, ?>) GitHubUser::getLogin, TEST_LOGIN),
                Arguments.of((Function<GitHubUser, ?>) GitHubUser::getName, TEST_NAME),
                Arguments.of((Function<GitHubUser, ?>) GitHubUser::getType, TEST_TYPE),
                Arguments.of((Function<GitHubUser, ?>) GitHubUser::getAvatarURL, TEST_AVATAR_URL),
                Arguments.of((Function<GitHubUser, ?>) GitHubUser::getCreatedAt, TEST_CREATED_AT),
                Arguments.of((Function<GitHubUser, ?>) GitHubUser::getFollowersCount, TEST_FOLLOWERS_COUNT),
                Arguments.of((Function<GitHubUser, ?>) GitHubUser::getPublicRepos, TEST_PUBLIC_REPOS)
        );
    }

}
