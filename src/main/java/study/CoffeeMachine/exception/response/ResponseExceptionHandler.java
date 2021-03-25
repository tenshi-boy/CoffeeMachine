package study.CoffeeMachine.exception.response;

import org.slf4j.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import study.CoffeeMachine.exception.AppException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private Logger logger;

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppExceptions(Exception ex, WebRequest request) {
        AppError appError = new AppError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        return new ResponseEntity<>(appError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpectedExceptions(Exception ex, WebRequest request) {
        logger.error(buildLoggingMessage(ex));
        AppError appError = new AppError(HttpStatus.INTERNAL_SERVER_ERROR, messageSource.getMessage("validation.internalservererror.common.message", null, Locale.getDefault()), ex);
        return new ResponseEntity<>(appError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return returnBindingResultErrors(ex, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return returnBindingResultErrors(ex, ex.getBindingResult());
    }

    private ResponseEntity<Object> returnBindingResultErrors(Exception ex, BindingResult result) {
        List<ObjectError> allErrors = result.getAllErrors();
        List<FieldError> fieldErrors = new ArrayList<>();
        allErrors.forEach(objectError -> {
            String fieldName = "undefined";
            if (objectError instanceof org.springframework.validation.FieldError) {
                fieldName = ((org.springframework.validation.FieldError) objectError).getField();
            } else {
                if (objectError.getArguments() != null && objectError.getArguments().length > 0) {
                    for (int i = 0; i < objectError.getArguments().length; i++) {
                        if (objectError.getArguments()[i] instanceof String[]) {
                            String[] fieldsArray = (String[]) objectError.getArguments()[i];
                            fieldName = fieldsArray[0];
                        }
                    }
                }
            }
            FieldError fieldError = new FieldError(fieldName, objectError.getDefaultMessage());
            if (!fieldErrors.contains(fieldError)) {
                fieldErrors.add(fieldError);
            }
        });
        AppError appError = new AppError(HttpStatus.BAD_REQUEST, messageSource.getMessage("validation.invalid.args", null, Locale.getDefault()), ex, fieldErrors);
        return new ResponseEntity<>(appError, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn(buildLoggingMessage(ex));
        AppError appError = new AppError(HttpStatus.BAD_REQUEST, messageSource.getMessage("validation.malformed.json", null, Locale.getDefault()), ex);
        return new ResponseEntity<>(appError, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    private ResponseEntity<Object> returnBadRequestCommonMessage(Exception ex) {
        AppError appError = new AppError(HttpStatus.BAD_REQUEST, messageSource.getMessage("validation.badrequest.common.message", null, Locale.getDefault()), ex);
        return new ResponseEntity<>(appError, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        logger.error(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(buildLoggingMessage(ex));
        return returnBadRequestCommonMessage(ex);
    }

    private String buildLoggingMessage(Exception ex) {
        StringBuilder loggingMessage = new StringBuilder("Caught new exception: ").append(ex.getClass()).append(".\n ");
        loggingMessage.append("Message: ").append(ex.getMessage()).append(".\n ");
        loggingMessage.append("StackTrace: ").append(Arrays.toString(ex.getStackTrace())).append(".\n ");
        return loggingMessage.toString();
    }
}
