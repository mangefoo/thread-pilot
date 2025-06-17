package se.mindphaser.threadpilot.insurance.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public abstract class Insurance {
    public Insurance() {}

    public enum InsuranceType {
        CAR, PET, HEALTH
    }

    private Integer id;
    private InsuranceType type;
    private Discount discount;

    public abstract InsuranceType getType();
    public abstract Integer getMonthlyCost();
}
