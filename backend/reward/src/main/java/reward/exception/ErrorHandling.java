package reward.exception;

public class ErrorHandling {
    // Exception enums
    public enum ExceptionType {
        USERNOTFOUND("Error: User not found"),
        USEREXISTS("Error: User already exists"),
        MISSINGCREDITVALUE("Error: Missing points and coins"),
        MISSINGPRODUCTINFO("Error: Missing product info"),
        WRONGCOMMANDTYPE("Error: This command should not be returning any value"),
        PRODUCTNOTFOUND("Error: Product does not exist"),
        PRODUCTEXISTS("Error: Product already exists"),
        INVALIDPRODUCTNAME("Error: Invalid product name: product name exists"),
        NOTENOUGHCOINS("Error: User does not have enough coins");

        private final String errorMessage;

        ExceptionType(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public static class RewardException extends Exception {
        private final ExceptionType exceptionType;

        public RewardException(ExceptionType exceptionType) {
            super(exceptionType.getErrorMessage());
            this.exceptionType = exceptionType;
        }

        public ExceptionType getExceptionType() {
            return exceptionType;
        }
    }
}