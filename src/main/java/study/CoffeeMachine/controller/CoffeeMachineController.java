package study.CoffeeMachine.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import study.CoffeeMachine.command.*;
import study.CoffeeMachine.entity.CoffeeMachineCommandsLog;
import study.CoffeeMachine.entity.CoffeeMachineInfo;
import study.CoffeeMachine.dto.CoffeePortion;
import study.CoffeeMachine.service.CoffeeMachine;
import study.CoffeeMachine.service.CoffeeMachineLogProvider;

import javax.validation.Valid;
import java.util.List;


@RestController
@Api(value = "/", description = "Операции с кофемашиной")
public class CoffeeMachineController {
    private final CoffeeMachine coffeeMachine;
    private final CoffeeMachineLogProvider coffeeMachineLogProvider;

    @Autowired
    public CoffeeMachineController(CoffeeMachine coffeeMachine, CoffeeMachineLogProvider coffeeMachineLogProvider) {
        this.coffeeMachine = coffeeMachine;
        this.coffeeMachineLogProvider = coffeeMachineLogProvider;
    }

    @ApiOperation(value = "Информация о кофемашине")
    @GetMapping(value = "/")
    public CoffeeMachineInfo getInfo() {
        return coffeeMachine.getOrCreateNewInfo();
    }

    @ApiOperation(value = "Архив действий над кофемашиной")
    @PostMapping(value = "/commands/list/")
    public List<CoffeeMachineCommandsLog> getCommandsLog(@RequestParam int page, @RequestParam int size) {
        return coffeeMachineLogProvider.getLogs(page, size);
    }

    @ApiOperation(value = "Заварить кофе")
    @PostMapping(value = "/commands/make-coffee/")
    public CoffeePortion makeCoffee(@Valid MakeCoffee cmd) {
        return coffeeMachine.makeCoffee(cmd);
    }

    @ApiOperation(value = "Очистка кофемашины")
    @PostMapping(value = "/commands/clean-machine/")
    public CoffeeMachineInfo cleanMachine(@Valid CleanMachine cmd) {
        return coffeeMachine.cleanMachine(cmd);
    }

    @ApiOperation(value = "Добавить компоненты")
    @PostMapping(value = "/commands/add-components/")
    public CoffeeMachineInfo addComponents(@Valid AddComponents cmd) {
        return coffeeMachine.addComponents(cmd);
    }
}