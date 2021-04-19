package sk.kapsa.storage.conversion;

import java.util.List;

public interface RuleType {

	Object apply(List<Object> srcAttributes) throws ConversionException;
}
