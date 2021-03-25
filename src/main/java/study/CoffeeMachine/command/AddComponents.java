package study.CoffeeMachine.command;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@Data
@ToString
public class AddComponents extends Command {
    @Range(min = 0, max = 999)
    private int water;
    @Range(min = 0, max = 999)
    private int coffee;
    @Range(min = 0, max = 999)
    private int milk;
    @Range(min = 0, max = 999)
    private int sugar;
}