package study.CoffeeMachine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import study.CoffeeMachine.entity.CoffeeMachineCommandsLog;
import study.CoffeeMachine.repository.CoffeeMachineCommandsLogRepository;
import study.CoffeeMachine.service.CoffeeMachineLogProvider;


import java.util.List;

@Service
public class CoffeeMachineLogProviderImpl implements CoffeeMachineLogProvider {
    private final CoffeeMachineCommandsLogRepository coffeeMachineCommandsLogRepository;

    @Autowired
    public CoffeeMachineLogProviderImpl(CoffeeMachineCommandsLogRepository coffeeMachineCommandsLogRepository) {
        this.coffeeMachineCommandsLogRepository = coffeeMachineCommandsLogRepository;
    }

    @Override
    public List<CoffeeMachineCommandsLog> getLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "time"));
        return coffeeMachineCommandsLogRepository.findAll(pageable).getContent();
    }
}
