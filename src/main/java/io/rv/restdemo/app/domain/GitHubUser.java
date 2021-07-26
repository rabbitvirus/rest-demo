package io.rv.restdemo.app.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.net.URL;
import java.time.ZonedDateTime;

/**
 * POJO-like representation of a GitHub user - GET results to the GitHub should be deserialized to this class.
 * The class consists only of fields that are required in this exercise. Others are ignored.
 * Use {@link Builder} to create new instances.
 */
@JsonDeserialize(builder = GitHubUser.Builder.class)
public final class GitHubUser {

    private final int id;
    private final String login;
    private final String name;
    private final String type;
    private final URL avatarURL;
    private final ZonedDateTime createdAt;
    private final int followersCount;
    private final int publicRepos;

    private GitHubUser(final Builder builder) {
        this.id = builder.id;
        this.login = builder.login;
        this.name = builder.name;
        this.type = builder.type;
        this.avatarURL = builder.avatarURL;
        this.createdAt = builder.createdAt;
        this.followersCount = builder.followersCount;
        this.publicRepos = builder.publicRepos;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public URL getAvatarURL() {
        return avatarURL;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getPublicRepos() {
        return publicRepos;
    }

    @Override
    public String toString() {
        return "GitHubUser{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", avatarURL=" + avatarURL +
                ", createdAt=" + createdAt +
                ", followersCount=" + followersCount +
                ", publicRepos=" + publicRepos +
                '}';
    }

    @JsonPOJOBuilder
    public static final class Builder {

        private final int id;
        private final String login;
        private String name;
        private String type;
        private URL avatarURL;
        private ZonedDateTime createdAt;
        private int followersCount;
        private int publicRepos;

        @JsonCreator
        public static Builder newBuilderFor(@JsonProperty("id") final int id, @JsonProperty("login") final String login) {
            return new Builder(id, login);
        }

        private Builder(final int id, final String login) {
            this.id = id;
            this.login = login;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withType(final String type) {
            this.type = type;
            return this;
        }

        @JsonProperty("avatar_url")
        public Builder withAvatarURL(final URL avatarURL) {
            this.avatarURL = avatarURL;
            return this;
        }

        @JsonProperty("created_at")
        public Builder withCreatedAt(final ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @JsonProperty("followers")
        public Builder withFollowersCount(final int followersCount) {
            this.followersCount = followersCount;
            return this;
        }

        @JsonProperty("public_repos")
        public Builder withPublicRepos(final int publicRepos) {
            this.publicRepos = publicRepos;
            return this;
        }

        public GitHubUser build() {
            return new GitHubUser(this);
        }
    }
}
