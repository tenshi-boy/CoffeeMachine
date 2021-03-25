package study.CoffeeMachine.exception.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
class FieldError {
    private String field;
    private String message;

    FieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
