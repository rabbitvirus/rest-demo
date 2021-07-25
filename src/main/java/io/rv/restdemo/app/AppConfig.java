package io.rv.restdemo.app;

import io.rv.restdemo.app.domain.CalculationsHelper;
import io.rv.restdemo.app.domain.GitHubToDemoUserConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CalculationsHelper calculationsHelper() {
        return new CalculationsHelper();
    }

    @Bean
    public GitHubToDemoUserConverter gitHubToDemoUserConverter() {
        return new GitHubToDemoUserConverter(calculationsHelper());
    }

}
