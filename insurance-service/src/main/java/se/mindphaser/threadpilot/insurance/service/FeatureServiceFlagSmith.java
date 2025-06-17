package se.mindphaser.threadpilot.insurance.service;

import com.flagsmith.FlagsmithClient;
import com.flagsmith.models.Flags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnBooleanProperty(name = "feature-flags.flagsmith.enabled")
public class FeatureServiceFlagSmith implements FeatureService {
  private final FlagsmithClient flagsmithClient;
  private Flags flags;

  public FeatureServiceFlagSmith(FlagsmithClient flagsmithClient) {
    this.flagsmithClient = flagsmithClient;
  }

  @Override
  public boolean isEnabled(String featureName) {
    try {
      return flagsmithClient.getEnvironmentFlags().isFeatureEnabled(featureName);
    } catch (Exception e) {
      log.error("Error checking feature '{}' status: {}", featureName, e.getMessage(), e);
      return false;
    }
  }

  @Override
  public void setEnabled(String featureName, boolean enabled) {
    throw new UnsupportedOperationException("Feature flags are managed by FlagSmith and cannot be set directly.");
  }

  @Override
  public boolean isEnabledForIdentity(String identity, String featureName) {
    try {
      return flagsmithClient.getIdentityFlags(identity).isFeatureEnabled(featureName);
    } catch (Exception e) {
      log.error("Error fetching identity traits for '{}': {}", identity, e.getMessage(), e);
    }

    return false;
  }

  @Override
  public void setEnabledForIdentity(String identity, String featureName, boolean enabled) {
    throw new UnsupportedOperationException("Feature flags are managed by FlagSmith and cannot be set directly.");
  }
}
