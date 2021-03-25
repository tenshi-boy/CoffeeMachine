package study.CoffeeMachine.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;


import java.util.List;

@Data
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppError {
    private int status;
    private String error;
    private String message;
    private List<FieldError> fieldErrors;
    private String debugMessage;

    AppError(HttpStatus status, String message, Throwable ex) {
        this.status = status.value();
        this.message = message;
        error = status.getReasonPhrase();
        debugMessage = ex.getMessage();
    }

    AppError(HttpStatus status, String message, Throwable ex, List<FieldError> fieldErrors) {
        this.status = status.value();
        this.message = message;
        error = status.getReasonPhrase();
        debugMessage = ex.getMessage();
        this.fieldErrors = fieldErrors;
    }
}
