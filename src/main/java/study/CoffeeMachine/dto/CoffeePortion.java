package study.CoffeeMachine.dto;

import lombok.Data;

@Data
public class CoffeePortion {
    private final int water = 1;
    private final int coffee = 1;
    private int milk;
    private int sugar;
}
