package io.rv.restdemo.app.db;

@FunctionalInterface
public interface APICallListener {

    void registerCallForLogin(String login);

}
