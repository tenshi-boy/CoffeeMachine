package study.CoffeeMachine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "commands_log")
public class CoffeeMachineCommandsLog {
    @Id
    @SequenceGenerator(name = "pk_sequence", sequenceName = "commands_log_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "pk_sequence")
    @JsonIgnore
    private Long id;
    private String command;
    private String commandData;
    private LocalDateTime time;
}
