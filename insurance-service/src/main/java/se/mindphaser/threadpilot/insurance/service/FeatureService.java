package se.mindphaser.threadpilot.insurance.service;

import java.util.Optional;

public interface FeatureService {
  boolean isEnabled(String featureName);
  void setEnabled(String featureName, boolean enabled);
  boolean isEnabledForIdentity(String identity, String featureName);
  void setEnabledForIdentity(String identity, String featureName, boolean enabled);
}
