package io.rv.restdemo.app.rest;

import io.rv.restdemo.app.domain.GitHubUser;

@FunctionalInterface
public interface GitHubClient {

    /**
     * Gets the GitHub data for the provided user.
     * @param user name of the user
     * @return domain representation of the user
     */
    GitHubUser getGitHubUser(final String user);

}
