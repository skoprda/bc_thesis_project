package sk.kapsa.storage.mongo.services;

public class WrapperMalformedException extends RuntimeException {

	private static final long serialVersionUID = 3819492397899716496L;

	public WrapperMalformedException(String message) {
		super(message);
	}

	public WrapperMalformedException() {
	}

	public WrapperMalformedException(Throwable cause) {
		super(cause);
	}
}
