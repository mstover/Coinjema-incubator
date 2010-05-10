package strategiclibrary.service.cache;

public class DuplicateObjectException extends RuntimeException {
	private static final long serialVersionUID = 1;

	public DuplicateObjectException() {
		super();
	}

	public DuplicateObjectException(String message) {
		super(message);
	}

	public DuplicateObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateObjectException(Throwable cause) {
		super(cause);
	}

}
