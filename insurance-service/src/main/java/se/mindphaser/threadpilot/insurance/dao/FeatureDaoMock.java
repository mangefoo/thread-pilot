package se.mindphaser.threadpilot.insurance.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class FeatureDaoMock implements FeatureDao {

  private final Map<String, Boolean> environmentFeatures;

  private final Map<String, Map<String, Boolean>> identityFeatures;

  FeatureDaoMock() {
    environmentFeatures = new ConcurrentHashMap<>();
    environmentFeatures.put("include_vehicle_details", true);
    identityFeatures = new ConcurrentHashMap<>();
    identityFeatures.put("123456789012", Map.of("car_discount", true));
  }

  @Override
  public Map<String, Boolean> getEnvironmentFeatures() {
    return environmentFeatures;
  }

  @Override
  public Map<String, Boolean> getIdentityFeatures(String identity) {
    if (identityFeatures.containsKey(identity)) {
      return identityFeatures.get(identity);
    }

    return Map.of();
  }

  @Override
  public void setEnvironmentFeature(String name, Boolean enabled) {
    environmentFeatures.put(name, enabled);
  }

  @Override
  public void setIdentityFeature(String identity, String featureName, Boolean enabled) {
    identityFeatures.computeIfAbsent(identity, k -> new ConcurrentHashMap<>())
        .put(featureName, enabled);
  }
}
