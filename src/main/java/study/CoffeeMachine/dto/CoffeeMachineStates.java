package study.CoffeeMachine.dto;

import lombok.Getter;

@Getter
public enum CoffeeMachineStates {
    LIKE_NEW("Like a new"), TIME_TO_WASH("It's time to wash");

    private final String value;

    CoffeeMachineStates(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
