package se.mindphaser.threadpilot.insurance.service;

import se.mindphaser.threadpilot.insurance.dao.FeatureDao;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@ConditionalOnBooleanProperty(name = "feature-flags.flagsmith.enabled", havingValue = false, matchIfMissing = true)
public class FeatureServiceLocal implements FeatureService {

  private final FeatureDao featureDao;

  @Override
  public boolean isEnabled(String featureName) {
    return featureDao.getEnvironmentFeatures().getOrDefault(featureName, false);
  }

  @Override
  public void setEnabled(String featureName, boolean enabled) {
    featureDao.setEnvironmentFeature(featureName, enabled);
  }

  @Override
  public boolean isEnabledForIdentity(String identity, String featureName) {
    return featureDao.getIdentityFeatures(identity).getOrDefault(featureName, false);
  }

  @Override
  public void setEnabledForIdentity(String identity, String featureName, boolean enabled) {
    featureDao.setIdentityFeature(identity, featureName, enabled);
  }
}
