package study.CoffeeMachine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import study.CoffeeMachine.dto.CoffeeMachineStates;

import javax.persistence.*;

@Data
@Entity
@Table(name = "info")
public class CoffeeMachineInfo {
    @Id
    @JsonIgnore
    private Long id;
    @Enumerated(EnumType.STRING)
    private CoffeeMachineStates state;
    @Transient
    private Long availableCoffeePortions;
    private Long water;
    private Long coffee;
    private Long sugar;
    private Long milk;

    @PostLoad
    @PostUpdate
    @PostPersist
    private void calculateAvailableCoffeePortions (){
        if(water != null && coffee != null) {
            if (water > coffee && !coffee.equals(availableCoffeePortions)) {
                availableCoffeePortions = coffee;
            } else if (!water.equals(availableCoffeePortions)) {
                availableCoffeePortions = water;
            }
        }
    }
}
