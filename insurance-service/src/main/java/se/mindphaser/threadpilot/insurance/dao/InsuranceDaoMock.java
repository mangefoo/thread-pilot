package se.mindphaser.threadpilot.insurance.dao;

import java.util.List;
import org.springframework.stereotype.Service;
import se.mindphaser.threadpilot.insurance.model.CarInsurance;
import se.mindphaser.threadpilot.insurance.model.HealthInsurance;
import se.mindphaser.threadpilot.insurance.model.Insurance;
import se.mindphaser.threadpilot.insurance.model.PetInsurance;

@Service
public class InsuranceDaoMock implements InsuranceDao {
  public List<Insurance> getInsuranceByPersonId(String personId) {
    return List.of(
        PetInsurance.builder().id(1).build(),
        HealthInsurance.builder().id(2).build(),
        CarInsurance.builder()
            .id(3)
            .registrationNumber("XYZ123")
            .build()
    );
  }
}
