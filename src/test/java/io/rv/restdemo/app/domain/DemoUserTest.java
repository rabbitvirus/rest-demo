package io.rv.restdemo.app.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DemoUserTest {

    private static final int TEST_ID = 583231;
    private static final String TEST_LOGIN = "octocat";
    private static final String TEST_NAME = "The Octocat";
    private static final String TEST_TYPE = "User";
    private static final URL TEST_AVATAR_URL = URLUtil.createURL("https://avatars.githubusercontent.com/u/583231?v=4");
    private static final ZonedDateTime TEST_CREATED_AT = ZonedDateTime.parse("2011-01-25T18:44:36Z");
    private static final Double TEST_CALCULATIONS = 12.345d;


    @ParameterizedTest
    @MethodSource("provideAssertionParams")
    public void shouldCreateDemoUserWithCorrectFields(final Function<DemoUser, ?> fieldExtractor, final Object expectedFieldVal) {
        var user = DemoUser.Builder.newBuilderFor(TEST_ID, TEST_LOGIN)
                .withName(TEST_NAME)
                .withType(TEST_TYPE)
                .withAvatarUrl(TEST_AVATAR_URL)
                .withCreateAt(TEST_CREATED_AT)
                .withCalculations(TEST_CALCULATIONS)
                .build();

        var actualFieldVal = fieldExtractor.apply(user);

        assertEquals(expectedFieldVal, actualFieldVal);
    }

    private static Stream<Arguments> provideAssertionParams() {
        return Stream.of(
                Arguments.of((Function<DemoUser, ?>) DemoUser::getId, TEST_ID),
                Arguments.of((Function<DemoUser, ?>) DemoUser::getLogin, TEST_LOGIN),
                Arguments.of((Function<DemoUser, ?>) DemoUser::getName, TEST_NAME),
                Arguments.of((Function<DemoUser, ?>) DemoUser::getType, TEST_TYPE),
                Arguments.of((Function<DemoUser, ?>) DemoUser::getAvatarUrl, TEST_AVATAR_URL),
                Arguments.of((Function<DemoUser, ?>) DemoUser::getCreatedAt, TEST_CREATED_AT),
                Arguments.of((Function<DemoUser, ?>) DemoUser::getCalculations, TEST_CALCULATIONS)
        );
    }

}
