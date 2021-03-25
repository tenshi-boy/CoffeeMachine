package study.CoffeeMachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.CoffeeMachine.entity.CoffeeMachineInfo;

@Repository
public interface CoffeeMachineInfoRepository extends JpaRepository<CoffeeMachineInfo, Long> {
}
