package se.mindphaser.threadpilot.insurance.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.mindphaser.threadpilot.insurance.client.VehicleClient;
import se.mindphaser.threadpilot.insurance.dao.InsuranceDao;
import se.mindphaser.threadpilot.insurance.model.CarInsurance;
import se.mindphaser.threadpilot.insurance.model.Discount;
import se.mindphaser.threadpilot.insurance.model.Insurance;
import se.mindphaser.threadpilot.insurance.model.Insurance.InsuranceType;
import se.mindphaser.threadpilot.insurance.model.Vehicle;

@Service
public class InsuranceService {
    private final FeatureService featureService;
    private final VehicleClient vehicleClient;
    private final InsuranceDao insuranceDaoMock;

    public InsuranceService(FeatureService featureService,
                            VehicleClient vehicleClient,
                            InsuranceDao insuranceDaoMock,
                            @Value("${vehicle-service.url:http://localhost:8081}") String url) {
        this.featureService = featureService;
        this.vehicleClient = vehicleClient;
        this.insuranceDaoMock = insuranceDaoMock;
    }

    public List<Insurance> getInsurances(String personId) {
        var insurances = insuranceDaoMock.getInsuranceByPersonId(personId);

        insurances.stream()
            .filter(insurance -> insurance.getType().equals(InsuranceType.CAR))
            .map(insurance -> (CarInsurance) insurance)
            .forEach(insurance -> {
                if (featureService.isEnabled("include_vehicle_details")) {
                    Vehicle vehicle = vehicleClient.getVehicle(insurance.getRegistrationNumber());
                    insurance.setVehicle(vehicle);
                }
                if (featureService.isEnabledForIdentity(personId, "car_discount")) {
                    insurance.setDiscount(Discount.builder()
                        .percentage(42)
                        .build());
                }
            });

        return insurances;
    }
}
