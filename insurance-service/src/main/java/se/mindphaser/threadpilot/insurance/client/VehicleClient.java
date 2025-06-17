package se.mindphaser.threadpilot.insurance.client;

import se.mindphaser.threadpilot.insurance.model.Vehicle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VehicleClient {
  private final RestTemplate restTemplate;
  private final String url;

  VehicleClient(RestTemplate restTemplate,
      @Value("${vehicle-service.url:http://localhost:8081}") String url) {
    this.restTemplate = restTemplate;
    this.url = url + "/v1/vehicle/";
  }

  public Vehicle getVehicle(String vehicleId) {
    return restTemplate.getForObject(url + vehicleId, Vehicle.class);
  }
}
