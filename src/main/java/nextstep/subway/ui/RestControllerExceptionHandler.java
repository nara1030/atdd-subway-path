package nextstep.subway.ui;

import nextstep.subway.applicaion.dto.ErrorResponse;
import nextstep.subway.exception.FailToAddSectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler(FailToAddSectionException.class)
    public ResponseEntity<ErrorResponse> handleFailToAddSectionException(FailToAddSectionException e){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getErrorMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.valueOf(errorResponse.getHttpStatusCode()));
    }
}
