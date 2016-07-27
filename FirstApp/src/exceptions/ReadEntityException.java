package exceptions;

public class ReadEntityException extends DAOException {

	private static final long serialVersionUID = 1L;

	public ReadEntityException() {
		super();
	}

	public ReadEntityException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReadEntityException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReadEntityException(String message) {
		super(message);
	}

	public ReadEntityException(Throwable cause) {
		super(cause);
	}

}
