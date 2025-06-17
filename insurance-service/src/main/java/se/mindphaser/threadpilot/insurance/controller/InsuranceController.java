package se.mindphaser.threadpilot.insurance.controller;

import se.mindphaser.threadpilot.insurance.model.Insurance;
import se.mindphaser.threadpilot.insurance.service.InsuranceService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/insurances")
public class InsuranceController {
    private final InsuranceService service;
    public InsuranceController(InsuranceService svc) { this.service = svc; }

    @GetMapping("/{personId}")
    public List<Insurance> getInsurances(@PathVariable String personId) {
        return service.getInsurances(personId);
    }
}
