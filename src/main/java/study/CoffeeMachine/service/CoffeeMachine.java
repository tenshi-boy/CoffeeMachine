package study.CoffeeMachine.service;

import study.CoffeeMachine.command.AddComponents;
import study.CoffeeMachine.command.CleanMachine;
import study.CoffeeMachine.command.MakeCoffee;
import study.CoffeeMachine.entity.CoffeeMachineInfo;
import study.CoffeeMachine.dto.CoffeePortion;

public interface CoffeeMachine {
    CoffeeMachineInfo getOrCreateNewInfo();
    CoffeePortion makeCoffee(MakeCoffee cmd);
    CoffeeMachineInfo addComponents(AddComponents cmd);
    CoffeeMachineInfo cleanMachine(CleanMachine cmd);
}
