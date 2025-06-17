package se.mindphaser.threadpilot.insurance.dao;

import se.mindphaser.threadpilot.insurance.model.Insurance;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface InsuranceDao {
  List<Insurance> getInsuranceByPersonId(String personId);
}
