package study.CoffeeMachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.CoffeeMachine.entity.CoffeeMachineCommandsLog;

@Repository
public interface CoffeeMachineCommandsLogRepository extends JpaRepository<CoffeeMachineCommandsLog, Long> {
}
