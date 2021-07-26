package io.rv.restdemo.app.domain;

public final class CalculationsHelper {

    /**
     * Returns result of the following formula: (6 / followers * (2 + publicRepos)). If number of followers is zero,
     * then <code>null</code> is returned.
     *
     * @param followers number of followers for a particular user
     * @param publicRepos number of public repositories for a praticular user
     * @return calculations result or <code>null</code> if the calculation is not possible
     */
    public Double calculateFrom(final int followers, final int publicRepos) {
        return followers == 0 ? null : 6.0d / followers * (2 + publicRepos);
    }

}
