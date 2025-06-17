package se.mindphaser.threadpilot.insurance.configuration;

import com.flagsmith.FlagsmithClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationBeans {
  @Bean
  @ConditionalOnBooleanProperty(name = "feature-flags.flagsmith.enabled")
  public FlagsmithClient flagsmithClient(
      @Value("${feature-flags.flagsmith.api-url}") String apiUrl,
      @Value("${feature-flags.flagsmith.environment-key}") String environmentKey) {
    return FlagsmithClient
        .newBuilder()
        .setApiKey(environmentKey)
        .withConfiguration(com.flagsmith.config.FlagsmithConfig.newBuilder()
            .baseUri(apiUrl)
            .withLocalEvaluation(true)
            .withEnvironmentRefreshIntervalSeconds(10)
            .build()
        )
        .build();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
