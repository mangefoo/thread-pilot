package se.mindphaser.threadpilot.vehicle.controller;

import se.mindphaser.threadpilot.vehicle.model.Vehicle;
import se.mindphaser.threadpilot.vehicle.service.VehicleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/vehicle")
public class VehicleController {
    private final VehicleService service;
    public VehicleController(VehicleService service) { this.service = service; }

    @GetMapping("/{regNumber}")
    public Vehicle getVehicle(@PathVariable String regNumber) {
        return service.getVehicleInfo(regNumber);
    }
}
