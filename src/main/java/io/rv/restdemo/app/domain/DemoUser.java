package io.rv.restdemo.app.domain;

import java.net.URL;
import java.time.ZonedDateTime;

/**
 * User representation that should be returned in this demo application.
 * As the class is immutable, use {@link Builder} to create new instances.
 */
public final class DemoUser {

    private final int id;
    private final String login;
    private final String name;
    private final String type;
    private final URL avatarUrl;
    private final ZonedDateTime createdAt;
    private final Double calculations;

    private DemoUser(final Builder builder) {
        this.id = builder.id;
        this.login = builder.login;
        this.name = builder.name;
        this.type = builder.type;
        this.avatarUrl = builder.avatarUrl;
        this.createdAt = builder.createdAt;
        this.calculations = builder.calculations;
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

    public URL getAvatarUrl() {
        return avatarUrl;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Double getCalculations() {
        return calculations;
    }

    public static final class Builder {

        private final int id;
        private final String login;
        private String name;
        private String type;
        private URL avatarUrl;
        private ZonedDateTime createdAt;
        private Double calculations;

        public static Builder newBuilderFor(final int id, final String login) {
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

        public Builder withAvatarUrl(final URL avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder withCreatedAt(final ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder withCalculations(final Double calculations) {
            this.calculations = calculations;
            return this;
        }

        public DemoUser build() {
            return new DemoUser(this);
        }
    }

}
