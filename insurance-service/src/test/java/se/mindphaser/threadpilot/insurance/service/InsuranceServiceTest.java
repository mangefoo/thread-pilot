package se.mindphaser.threadpilot.insurance.service;

import java.util.concurrent.atomic.AtomicInteger;
import se.mindphaser.threadpilot.insurance.client.VehicleClient;
import se.mindphaser.threadpilot.insurance.dao.InsuranceDao;
import se.mindphaser.threadpilot.insurance.dao.InsuranceDaoMock;
import se.mindphaser.threadpilot.insurance.model.CarInsurance;
import se.mindphaser.threadpilot.insurance.model.Insurance;
import se.mindphaser.threadpilot.insurance.model.Vehicle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsuranceServiceTest {
    private static final VehicleClient vehicleClient = mock(VehicleClient.class);
    private static final InsuranceDao insuranceDaoMock = new InsuranceDaoMock();

    @BeforeAll
    static void setup() {
        when(vehicleClient.getVehicle("XYZ123")).thenReturn(Vehicle.builder()
            .id(42)
            .registrationNumber("XYZ123")
            .make("Toyota")
            .model("Camry")
            .year(2020)
            .build());
    }

    @Test
    void whenFeatureDisabled_thenNoVehicleDetails() {
        FeatureService featureService = mock(FeatureService.class);
        when(featureService.isEnabled("include_vehicle_details")).thenReturn(false);

        InsuranceService svc = new InsuranceService(featureService, vehicleClient, insuranceDaoMock, "http://does-not-matter");
        List<Insurance> ins = svc.getInsurances("1");
        ins.stream()
            .filter(insurance -> insurance.getType() == Insurance.InsuranceType.CAR)
            .map(insurance -> (CarInsurance) insurance)
            .forEach(carInsurance -> assertNull(carInsurance.getVehicle()));
    }

    @Test
    void whenFeatureEnabled_thenVehicleDetailsIncluded() {
        FeatureService featureService = mock(FeatureService.class);
        when(featureService.isEnabled("include_vehicle_details")).thenReturn(true);

        InsuranceService svc = new InsuranceService(featureService, vehicleClient, insuranceDaoMock, "http://localhost:8081");
        List<Insurance> ins = svc.getInsurances("1");
        ins.stream()
            .filter(insurance -> insurance.getType() == Insurance.InsuranceType.CAR)
            .map(insurance -> (CarInsurance) insurance)
            .forEach(carInsurance -> {
                assertNotNull(carInsurance.getVehicle());
                assertEquals("XYZ123", carInsurance.getVehicle().getRegistrationNumber());
                assertEquals("Toyota", carInsurance.getVehicle().getMake());
                assertEquals("Camry", carInsurance.getVehicle().getModel());
                assertEquals(2020, carInsurance.getVehicle().getYear());
                assertEquals(42, carInsurance.getVehicle().getId());
            });
    }

    @Test
    void whenIdentityFeatureEnabled_thenDiscountIncluded() {
        FeatureService featureService = mock(FeatureService.class);
        when(featureService.isEnabled("include_vehicle_details")).thenReturn(true);
        when(featureService.isEnabledForIdentity("123456789012", "car_discount")).thenReturn(true);

        InsuranceService svc = new InsuranceService(featureService, vehicleClient, insuranceDaoMock, "http://localhost:8081");

        List<Insurance> ins = svc.getInsurances("123456789012");
        AtomicInteger carInsuranceCount = new AtomicInteger();
        ins.stream()
            .filter(insurance -> insurance.getType() == Insurance.InsuranceType.CAR)
            .map(insurance -> (CarInsurance) insurance)
            .forEach(carInsurance -> {
                assertNotNull(carInsurance.getDiscount());
                assertEquals(42, carInsurance.getDiscount().getPercentage());
                carInsuranceCount.getAndIncrement();
            });
        assertEquals(1, carInsuranceCount.get(), "Expected 1 car insurance");
    }

    @Test
    void whenIdentityFeatureDisabled_thenDiscountExcluded() {
        FeatureService featureService = mock(FeatureService.class);
        when(featureService.isEnabled("include_vehicle_details")).thenReturn(true);
        when(featureService.isEnabledForIdentity("123456789012", "car_discount")).thenReturn(false);

        InsuranceService svc = new InsuranceService(featureService, vehicleClient, insuranceDaoMock, "http://localhost:8081");

        List<Insurance> ins = svc.getInsurances("123456789012");
        AtomicInteger carInsuranceCount = new AtomicInteger();
        ins.stream()
            .filter(insurance -> insurance.getType() == Insurance.InsuranceType.CAR)
            .map(insurance -> (CarInsurance) insurance)
            .forEach(carInsurance -> {
                assertNull(carInsurance.getDiscount());
                carInsuranceCount.getAndIncrement();
            });
        assertEquals(1, carInsuranceCount.get(), "Expected 1 car insurance");
    }
}