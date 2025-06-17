package se.mindphaser.threadpilot.insurance.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class Vehicle {
    private Integer id;
    private String registrationNumber;
    private String make;
    private String model;
    private Integer year;
}
