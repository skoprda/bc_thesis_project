package sk.kapsa.storage.conversion;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import sk.kapsa.storage.mongo.entities.RuleWrapper;

public class DataConverter {

	private RuleTypeScanner ruleTypeScanner;
	private static final Logger logger = LoggerFactory.getLogger(DataConverter.class);

	public DataConverter() {
		ruleTypeScanner = new RuleTypeScanner();
	}

	private Object createNewAttributeValue(List<Object> srcAttributesValues, Rule rule) {

		String ruleType = rule.getRuleTypeSelector();
		Object ruleSpecific = rule.getRuleSpecific();
		ObjectMapper mapper = new ObjectMapper();
		Class<?> ruleTypeClass = ruleTypeScanner.getClassBySelector(ruleType);
		RuleType ruleTypeObject = (RuleType) mapper.convertValue(ruleSpecific, ruleTypeClass);
		Object newValue = null;
		try {
			newValue = ruleTypeObject.apply(srcAttributesValues);
		} catch (ConversionException e) {
			logger.warn("values: " + srcAttributesValues + " caused ConversionException.");
		}
		return newValue;
	}

	private Document createNewItem(Document item, Rule rule, SourceOccurence srcOccurence) {
		Document newItem = item;
		Attribute destination = srcOccurence.getDestination();
		List<Attribute> sources = srcOccurence.getSources();
		if (rule.getRuleTypeSelector().equals("Delete attribute")) {
			newItem = AttributeSearcher.deleteAttributes(newItem, sources);
		} else {
			List<Object> srcAttributes = AttributeSearcher.getSourceAttributes(newItem, sources);
			Object destinationAttr = createNewAttributeValue(srcAttributes, rule);
			if (destinationAttr != null)
				newItem = AttributeSearcher.injectAttribute(newItem, destination, destinationAttr);
		}
		return newItem;
	}

	private Document convertOne(Document item, List<Rule> rules) {
		Document newItem = item;
		for (int i = 0; i < rules.size(); i++) {
			Rule rule = rules.get(i);
			Attribute destinationExample = rule.getDestination();
			List<Attribute> sourcesExample = rule.getSource();
			SourceAttributesFinder finder = new SourceAttributesFinder(newItem, sourcesExample,
					destinationExample);
			while (finder.hasNext()) {
				SourceOccurence srcDestination = finder.next();
				newItem = createNewItem(item, rule, srcDestination);
			}
		}
		return newItem;
	}

	public List<Document> process(FindIterable<Document> data, int upTo, RuleWrapper ruleWrapper) {
		List<Rule> rulesUpTo =  ruleWrapper.getRulesUpTo(upTo);
		List<Document> convertedData = new ArrayList<>();
		data.map(item -> {
			return convertOne(item, rulesUpTo);
		}).into(convertedData);
		return convertedData;
	}

	public StreamingResponseBody streamProcess(FindIterable<Document> data, int upTo, RuleWrapper ruleWrapper) {
		StreamingResponseBody stream = out -> {
			Block<Document> send = item -> {
				try (ObjectOutputStream oos = new ObjectOutputStream(out)) {

					oos.writeObject(item);
				} catch (IOException e) {
					System.err.println("Daco je zle s outputstreamom");
				}
			};
			data.forEach(send);
		};
		return stream;
	}

}
