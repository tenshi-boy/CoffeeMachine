package study.CoffeeMachine.command;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@Data
@ToString
public class MakeCoffee extends Command {
    @Range(min = 0, max = 2)
    private int milk;
    @Range(min = 0, max = 2)
    private int sugar;
}
