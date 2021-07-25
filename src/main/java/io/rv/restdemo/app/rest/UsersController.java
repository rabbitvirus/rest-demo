package io.rv.restdemo.app.rest;

import io.rv.restdemo.app.domain.DemoUser;
import io.rv.restdemo.app.domain.GitHubToDemoUserConverter;
import io.rv.restdemo.app.domain.GitHubUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger LOGGER = LogManager.getLogger(UsersController.class);

    private final GitHubClient gitHubRestClient;
    private final GitHubToDemoUserConverter converter;

    @Autowired
    public UsersController(final GitHubClient gitHubRestClient,
                           final GitHubToDemoUserConverter converter) {
        this.gitHubRestClient = gitHubRestClient;
        this.converter = converter;
    }

    @GetMapping(value = "/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemoUser> getLoginDetails(@PathVariable final String login) {
        final GitHubUser ghUser = gitHubRestClient.getGitHubUser(login);
        if (ghUser == null) {
            LOGGER.info("User {} not found...", () -> login);
            return ResponseEntity.notFound().build();
        }

        final DemoUser demoUser = converter.convertToDemoUser(ghUser);
        LOGGER.info("Demo user created: {}", () -> demoUser);
        return ResponseEntity.ok(demoUser);
    }

}
