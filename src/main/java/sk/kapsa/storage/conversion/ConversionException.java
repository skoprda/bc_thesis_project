package sk.kapsa.storage.conversion;

public class ConversionException extends Exception {
	
	
	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}
	public ConversionException(Exception ex) {
		super(ex.getMessage(), ex.getCause());
	}
}
