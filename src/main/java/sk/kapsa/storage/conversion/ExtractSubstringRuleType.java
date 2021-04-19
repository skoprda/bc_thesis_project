package sk.kapsa.storage.conversion;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

@RuleTypeImpl(ruleTypeSelector = "Extract substring")
public class ExtractSubstringRuleType implements RuleType{
	
	private String regex;
	
	@Override
	public Object apply(List<Object> srcAttributes) throws ConversionException{
		try {
		String onlyItem = (String)srcAttributes.get(0);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(onlyItem);
		String newValue = "";
		if(matcher.find()) {
			newValue = matcher.group(0);
		}
		return newValue;
		}catch(Exception e) {
			throw new ConversionException(e.getMessage(), e.getCause());
		}
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	
}
