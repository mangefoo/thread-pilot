package se.mindphaser.threadpilot.insurance.model;

import static se.mindphaser.threadpilot.insurance.model.Insurance.InsuranceType.HEALTH;
import static se.mindphaser.threadpilot.insurance.model.Insurance.InsuranceType.PET;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import se.mindphaser.threadpilot.insurance.model.Insurance.InsuranceType;

@Data
@SuperBuilder
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class HealthInsurance extends Insurance {
  @Override
  public InsuranceType getType() {
    return HEALTH;
  }

  @Override
  public Integer getMonthlyCost() {
    return 20;
  }
}
