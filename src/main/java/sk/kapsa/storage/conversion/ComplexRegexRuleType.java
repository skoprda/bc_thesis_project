package sk.kapsa.storage.conversion;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RuleTypeImpl(ruleTypeSelector = "Complex regular expression")
public class ComplexRegexRuleType implements RuleType {

	private List<RegexesObject> regex;
	private String builderString;

	@Override
	public Object apply(List<Object> srcAttributes) throws ConversionException{
		try {
		List<String> attributes = srcAttributes.stream().map(item -> item.toString()).collect(Collectors.toList());
		List<String> extractedValues = extractValues(attributes);
		String result = parseBuilderString(extractedValues);
		return result;
		}catch(Exception e) {
			throw new ConversionException(e.getMessage(), e.getCause());
		}
	}

	private List<String> extractValues(List<String> attributes) {
		List<String> extractedValues = new ArrayList<>();
		for(RegexesObject regexObject: regex) {
			String regex = regexObject.getRegex();
			regex = adaptRegexToJava(regex);
			int index = regexObject.getValueIndex();
			String actualValue = attributes.get(index);
			List<String> actualExtractedValues = applyRegex(regex, actualValue);
			extractedValues.addAll(actualExtractedValues);
		}
		return extractedValues;
	}
	
	private String adaptRegexToJava(String regex) {
		String newRegex = regex.replace("\\\\", "\\");
		return newRegex;
	}

	private List<String> applyRegex(String regex, String value) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		List<String> newValues = new ArrayList<>();
		int i = 0;
		while (matcher.find()) {
		   for (int j = 0; j <= matcher.groupCount(); j++) {
		      newValues.add(matcher.group(j));
		      i++;
		   }
		}
		return newValues;
	}

	private String parseBuilderString(List<String> extractedValues) {
		StringBuilder finalString = new StringBuilder();
		for (int i = 0; i < builderString.length(); i++) {
			if (builderString.charAt(i) == '{') {
				if (builderString.charAt(i + 1) == '{') {
					i += 1;
					finalString.append("{");
					continue;
				}
				StringBuilder numberString = new StringBuilder();
				for (int j = i + 1; j < builderString.length(); j++) {
					i = j;
					if (builderString.charAt(j) == '}')
						break;
					else
						numberString.append(builderString.charAt(j));
				}
				int number = Integer.parseInt(numberString.toString());
				finalString.append(extractedValues.get(number));
			} else {
				finalString.append(builderString.charAt(i));
			}
		}
		return finalString.toString();
	}

	public List<RegexesObject> getRegexes() {
		return regex;
	}

	public void setRegexes(List<RegexesObject> regexes) {
		this.regex = regexes;
	}

	public String getBuilderString() {
		return builderString;
	}

	public void setBuilderString(String builderString) {
		this.builderString = builderString;
	}
}

class RegexesObject{
	private String regex;
	private int valueIndex;
	
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public int getValueIndex() {
		return valueIndex;
	}
	public void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
	}
	
	
}
