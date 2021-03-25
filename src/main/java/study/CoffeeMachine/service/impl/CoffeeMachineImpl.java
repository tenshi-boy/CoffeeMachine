package study.CoffeeMachine.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.CoffeeMachine.command.AddComponents;
import study.CoffeeMachine.command.CleanMachine;
import study.CoffeeMachine.command.Command;
import study.CoffeeMachine.command.MakeCoffee;
import study.CoffeeMachine.entity.CoffeeMachineCommandsLog;
import study.CoffeeMachine.entity.CoffeeMachineInfo;
import study.CoffeeMachine.dto.CoffeeMachineStates;
import study.CoffeeMachine.dto.CoffeePortion;
import study.CoffeeMachine.exception.AppException;
import study.CoffeeMachine.repository.CoffeeMachineCommandsLogRepository;
import study.CoffeeMachine.repository.CoffeeMachineInfoRepository;
import study.CoffeeMachine.service.CoffeeMachine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class CoffeeMachineImpl implements CoffeeMachine {
    private final ObjectWriter objectWriter;
    private final CoffeeMachineInfoRepository coffeeMachineInfoRepository;
    private final CoffeeMachineCommandsLogRepository coffeeMachineCommandsLogRepository;

    @Autowired
    public CoffeeMachineImpl(ObjectWriter objectWriter,
                             CoffeeMachineInfoRepository coffeeMachineInfoRepository,
                             CoffeeMachineCommandsLogRepository coffeeMachineCommandsLogRepository) {
        this.objectWriter = objectWriter;
        this.coffeeMachineInfoRepository = coffeeMachineInfoRepository;
        this.coffeeMachineCommandsLogRepository = coffeeMachineCommandsLogRepository;
    }

    @Override
    @Transactional
    public CoffeeMachineInfo getOrCreateNewInfo() {
        CoffeeMachineInfo info =  coffeeMachineInfoRepository.findById(1L).orElse(null);
        if(info == null) {
            info = new CoffeeMachineInfo();
            info.setId(1L);
            info.setCoffee(0L);
            info.setWater(0L);
            info.setMilk(0L);
            info.setSugar(0L);
            info.setState(CoffeeMachineStates.LIKE_NEW);
            return coffeeMachineInfoRepository.save(info);
        } else {
            return info;
        }
    }

    private void writeCommandToLog(Command cmd) {
        try {
            CoffeeMachineCommandsLog log = new CoffeeMachineCommandsLog();
            log.setCommand(cmd.getClass().getSimpleName());
            String json = objectWriter.writeValueAsString(cmd);
            log.setCommandData(json);
            log.setTime(LocalDateTime.now());
            coffeeMachineCommandsLogRepository.save(log);
        } catch (JsonProcessingException e){
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public CoffeePortion makeCoffee(MakeCoffee cmd) {
        writeCommandToLog(cmd);
        CoffeeMachineInfo info = getOrCreateNewInfo();
        List<String> notEnough = new ArrayList<>();
        if(info.getCoffee() - 1L >= 0) {
            info.setCoffee(info.getCoffee() - 1L);
        } else {
            notEnough.add("coffee");
        }
        if(info.getWater() - 1L >= 0) {
            info.setWater(info.getWater() - 1L);
        } else {
            notEnough.add("water");
        }
        if(info.getMilk() - cmd.getMilk() >= 0) {
            info.setMilk(info.getMilk() - cmd.getMilk());
        } else {
            notEnough.add("milk");
        }
        if(info.getSugar() - cmd.getSugar() >= 0) {
            info.setSugar(info.getSugar() - cmd.getSugar());
        } else {
            notEnough.add("sugar");
        }
        if(notEnough.size() > 0)
            throw new AppException("There are not enough components to make coffee. Check: " + String.join(", ", notEnough));
        if(Math.random() < 0.3) {
            info.setState(CoffeeMachineStates.TIME_TO_WASH);
        }
        coffeeMachineInfoRepository.save(info);
        CoffeePortion portion = new CoffeePortion();
        portion.setMilk(cmd.getMilk());
        portion.setSugar(cmd.getSugar());
        return portion;
    }

    @Override
    @Transactional
    public CoffeeMachineInfo addComponents(AddComponents cmd) {
        writeCommandToLog(cmd);
        CoffeeMachineInfo info = getOrCreateNewInfo();
        info.setWater(info.getWater() + cmd.getWater());
        info.setCoffee(info.getCoffee() + cmd.getCoffee());
        info.setMilk(info.getMilk() + cmd.getMilk());
        info.setSugar(info.getSugar() + cmd.getSugar());
        if (Math.random() < 0.1) {
            info.setState(CoffeeMachineStates.TIME_TO_WASH);
        }
        return coffeeMachineInfoRepository.save(info);
    }

    @Override
    @Transactional
    public CoffeeMachineInfo cleanMachine(CleanMachine cmd) {
        writeCommandToLog(cmd);
        CoffeeMachineInfo info = getOrCreateNewInfo();
        info.setState(CoffeeMachineStates.LIKE_NEW);
        return coffeeMachineInfoRepository.save(info);
    }
}
