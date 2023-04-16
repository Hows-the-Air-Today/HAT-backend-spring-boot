package io.howstheairtoday.airqualityclient.config;

import io.howstheairtoday.airqualityclient.exception.RequestExecutionException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    public static final int CONNECT_TIMEOUT_SECONDS = 5;
    public static final int READ_TIMEOUT_SECONDS = 5;
    public static final int MAX_RETRY_COUNT = 3;
    public static final long RETRY_INTERVAL_MS = 1000L;

    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT_SECONDS))
                .setReadTimeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS))
                .additionalInterceptors(retryInterceptor(), loggingInterceptor())
                .build();
    }

    @Bean
    public RetryTemplate retryTemplate() {

        return createRetryTemplate();
    }

    /**
     * RestTemplate에서 요청 실패 시 재시도를 위한 인터셉터
     *
     * @return ClientHttpRequestInterceptor 재시도 인터셉터
     */
    public ClientHttpRequestInterceptor retryInterceptor() {

        return ((request, body, execution) -> {
            RetryTemplate retryTemplate = createRetryTemplate();

            try {
                return retryTemplate.execute(context -> execution.execute(request, body));
            } catch (IOException throwable) {
                throw new RequestExecutionException("요청을 처리하는 중에 오류가 발생했습니다.", throwable);
            }
        });
    }

    /**
     * RestTemplate에서 요청과 응답을 로깅하기 위한 로깅 인터셉터
     *
     * @return ClientHttpRequestInterceptor 로깅 인터셉터
     */
    public ClientHttpRequestInterceptor loggingInterceptor() {

        return new LoggingInterceptor();
    }


    /**
     * RetryTemplate 생성
     *
     * @return RetryTemplate
     */
    public RetryTemplate createRetryTemplate() {

        RetryTemplate retryTemplate = new RetryTemplate();

        // 재시도 정책 설정
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(MAX_RETRY_COUNT);
        retryTemplate.setRetryPolicy(retryPolicy);

        // 재시도 주기 정책 설정
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        // 1초 (1000ms) 간격으로 재시도
        backOffPolicy.setBackOffPeriod(RETRY_INTERVAL_MS);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }
}


