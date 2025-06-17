package se.mindphaser.threadpilot.insurance.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.mindphaser.threadpilot.insurance.client.VehicleClient;
import se.mindphaser.threadpilot.insurance.model.Vehicle;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class InsuranceControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private VehicleClient vehicleClient;

    @Test
    void testGetInsurancesWithVehicleAndDiscount() throws Exception{
        when(vehicleClient.getVehicle("XYZ123")).thenReturn(Vehicle.builder()
                .registrationNumber("XYZ123")
                .make("Toyota")
                .model("Camry")
                .year(2020)
                .build());
        var actualJson = restTemplate.getForObject("/v1/insurances/123456789012", String.class);
        var expectedJson = """
[
  {
    "id": 1,
    "type": "PET",
    "monthlyCost": 10
  },
  {
    "id": 2,
    "type": "HEALTH",
    "monthlyCost": 20
  },
  {
    "id": 3,
    "type": "CAR",
    "registrationNumber": "XYZ123",
    "discount": {
      "percentage": 42
    },
    "vehicle": {
      "registrationNumber": "XYZ123",
      "make": "Toyota",
      "model": "Camry",
      "year": 2020
    },
    "monthlyCost": 30
  }
]
""";

        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }
}
