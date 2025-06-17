package se.mindphaser.threadpilot.vehicle.service;

import se.mindphaser.threadpilot.vehicle.model.Vehicle;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    public Vehicle getVehicleInfo(String regNumber) {
        return Vehicle.builder()
                .id(1)
                .registrationNumber(regNumber)
                .make("Toyota")
                .model("Corolla")
                .year(2020)
                .build();
    }
}
