package nextstep.subway.exception;

public class FailToAddSectionException extends RuntimeException {
    private String errorMessage;

    public FailToAddSectionException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
