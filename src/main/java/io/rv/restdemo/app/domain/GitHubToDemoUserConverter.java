package io.rv.restdemo.app.domain;

import java.util.Objects;

/**
 * Converter class - converts {@link GitHubUser} to {@link DemoUser}.
 */
public final class GitHubToDemoUserConverter {

    private final CalculationsHelper calcHelper;

    /**
     * @param calcHelper {@link CalculationsHelper} used during conversion
     * @throws NullPointerException if the arg was null
     */
    public GitHubToDemoUserConverter(final CalculationsHelper calcHelper) {
        this.calcHelper = Objects.requireNonNull(calcHelper, "Calculations helper cannot be null!");
    }

    /**
     * Performs the actual conversion. Uses {@link CalculationsHelper} to find a value for {@link DemoUser#calculations}.
     *
     * @param ghUser user to be converted
     * @return <code>null</code> if the provided user was <code>null</code>, {@link DemoUser} otherwise
     */
    public DemoUser convertToDemoUser(final GitHubUser ghUser) {
        return ghUser == null ? null : DemoUser.Builder.newBuilderFor(ghUser.getId(), ghUser.getLogin())
                .withName(ghUser.getName())
                .withType(ghUser.getType())
                .withAvatarUrl(ghUser.getAvatarURL())
                .withCreatedAt(ghUser.getCreatedAt())
                .withCalculations(calcHelper.calculateFrom(ghUser.getFollowersCount(), ghUser.getPublicRepos()))
                .build();
    }

}
