package io.rv.restdemo.app.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "requests_stats")
public class RequestsStats {

    @Id
    private String login;

    @Version
    private Long requestCount = 0L;

    public RequestsStats() {}

    public RequestsStats(final String login) {
        this.login = login;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public String getLogin() {
        return login;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    @Override
    public String toString() {
        return "RequestsStats{" +
                "login='" + login + '\'' +
                ", requestCount=" + requestCount +
                '}';
    }
}
