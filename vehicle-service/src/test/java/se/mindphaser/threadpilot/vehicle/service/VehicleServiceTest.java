package se.mindphaser.threadpilot.vehicle.service;

import se.mindphaser.threadpilot.vehicle.model.Vehicle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VehicleServiceTest {
    @Test
    void testGetVehicleInfo() {
        VehicleService svc = new VehicleService();
        Vehicle v = svc.getVehicleInfo("ABC123");
        assertEquals("ABC123", v.getRegistrationNumber());
        assertNotNull(v.getMake());
        assertTrue(v.getYear() > 0);
    }
}
