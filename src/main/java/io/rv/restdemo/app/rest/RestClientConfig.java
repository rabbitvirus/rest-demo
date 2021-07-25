package io.rv.restdemo.app.rest;

import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@Configuration
public class RestClientConfig {

    private static final int MAX_RETRY_ATTEMPTS = 10;
    private static final long EXP_RETRY_INI_INTERVAL = 500L;
    private static final long EXP_RETRY_MAX_INTERVAL = 3000L;
    private static final double EXP_RETRY_MULTIPLIER = 1.5d;

    private static final Set<HttpStatus> RETRYABLE_SERVER_ERRORS = Set.of(
            BAD_GATEWAY,
            SERVICE_UNAVAILABLE,
            INTERNAL_SERVER_ERROR,
            GATEWAY_TIMEOUT
    );

    private final SimpleRetryPolicy defaultRetryPolicy = new SimpleRetryPolicy(MAX_RETRY_ATTEMPTS);

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RetryTemplate httpRetryTemplate() {
        var errorPolicy = new ExceptionClassifierRetryPolicy();
        errorPolicy.setExceptionClassifier(errorDispatchingPolicy());

        var retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(errorPolicy);
        retryTemplate.setBackOffPolicy(createExponentialBackOffPolicy());
        return retryTemplate;
    }

    private static ExponentialBackOffPolicy createExponentialBackOffPolicy() {
        var backoffPolicy = new ExponentialBackOffPolicy();
        backoffPolicy.setInitialInterval(EXP_RETRY_INI_INTERVAL);
        backoffPolicy.setMaxInterval(EXP_RETRY_MAX_INTERVAL);
        backoffPolicy.setMultiplier(EXP_RETRY_MULTIPLIER);
        return backoffPolicy;
    }


    private Classifier<Throwable, RetryPolicy> errorDispatchingPolicy() {
        return thr -> {
            if (thr instanceof HttpStatusCodeException) {
                var status = ((HttpStatusCodeException) thr).getStatusCode();
                if (RETRYABLE_SERVER_ERRORS.contains(status))
                    return defaultRetryPolicy;
            }
            return new NeverRetryPolicy();
        };
    }
}
