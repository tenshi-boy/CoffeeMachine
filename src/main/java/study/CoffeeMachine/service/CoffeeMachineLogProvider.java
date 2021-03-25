package study.CoffeeMachine.service;

import study.CoffeeMachine.entity.CoffeeMachineCommandsLog;

import java.util.List;

public interface CoffeeMachineLogProvider {
    List<CoffeeMachineCommandsLog> getLogs(int page, int size);
}
