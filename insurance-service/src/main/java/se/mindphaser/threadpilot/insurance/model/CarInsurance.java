package se.mindphaser.threadpilot.insurance.model;

import static se.mindphaser.threadpilot.insurance.model.Insurance.InsuranceType.CAR;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class CarInsurance extends Insurance {

  private String registrationNumber;
  private Vehicle vehicle;

  @Override
  public InsuranceType getType() {
    return CAR;
  }

  @Override
  public Integer getMonthlyCost() {
    return 30;
  }
}
