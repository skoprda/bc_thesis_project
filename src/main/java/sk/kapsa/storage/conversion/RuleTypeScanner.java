package sk.kapsa.storage.conversion;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

public class RuleTypeScanner {

	private Map<String, Class<?>> ruleTypeSelectorMap = new HashMap<>();

	public RuleTypeScanner() {
		Reflections reflections = new Reflections("sk.kapsa.storage.conversion");
		Set<Class<?>> ruleTypeClasses = reflections.getTypesAnnotatedWith(RuleTypeImpl.class);
		fillRuleTypeSelectorMap(ruleTypeClasses);
	}

	public Class<?> getClassBySelector(String ruleTypeSelector) {
		return ruleTypeSelectorMap.get(ruleTypeSelector);
	}

	private String getSelectorValue(Class<?> ruleTypeClass) {
		
		return ruleTypeClass.getAnnotation(RuleTypeImpl.class).ruleTypeSelector();
		
	}

	private void fillRuleTypeSelectorMap(Set<Class<?>> ruleTypeClasses) {
		for(Class<?> ruleTypeClass: ruleTypeClasses) {
			String selectorValue = getSelectorValue(ruleTypeClass);
			ruleTypeSelectorMap.put(selectorValue, ruleTypeClass);
		}
	}
}
