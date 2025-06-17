package se.mindphaser.threadpilot.insurance.dao;

import java.util.Map;

public interface FeatureDao {
  Map<String, Boolean> getEnvironmentFeatures();
  Map<String, Boolean> getIdentityFeatures(String identity);
  void setEnvironmentFeature(String name, Boolean enabled);
  void setIdentityFeature(String identity, String featureName, Boolean enabled);
}
