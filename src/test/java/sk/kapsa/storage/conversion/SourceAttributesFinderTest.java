package sk.kapsa.storage.conversion;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SourceAttributesFinderTest {


	private Document item = Document.parse("{data: [{actorAndRole: {actor: {name: 'John', rewards:[{year: 2015, movie: 'pulp fiction'}, {year: 2018, movie: 'pulp fiction 2'}]}, role: 'George'}},"
			+ "{actorAndRole: {actor: {name: 'Rick', rewards:[{year: 2015, movie: 'Gladiator'}, {year: 2018, movie: 'Gladiator 2'}]}, role: 'Manfred'}},"
			+ "{actorAndRole: {actor: {name: 'Julia', rewards:[{year: 2015, movie: 'Pretty woman'}]}, role: 'Julia'}}]}");
	
	private Document item2 = Document.parse("{data: [{actorAndRole: {actor: {name: 'John', rewards:[{year: 2015, movie: 'pulp fiction'}, {year: 2018, movie: 'pulp fiction 2'}]}, role: 'George'}},"
			+ "{actorAndRole: {actor: {name: 'Rick', rewards:[{year: 2015, movie: 'Gladiator'}, {year: 2018, movie: 'Gladiator 2'}]}, role: 'Manfred'}},"
			+ "{actorAndRole: {actor: {name: 'Julia', rewards:[{year: 2015, movie: 'Pretty woman'}]}, role: 'Julia'}},"
			+ "{actorAndRole: {actor: {name: 'Julia 2', rewards:[{year: 2015, movie: 'Pretty woman'},{year: 2015, movie: 'Pretty woman'},{year: 2015, movie: 'Pretty woman'},{year: 2015, movie: 'Pretty woman'}]}, role: 'Julia'}}]}]}");
	
	
	@Test
	void getLongestPrefixTest1() {
		List<String> pathItems1 = Arrays.asList("data","0","actorAndRole");
		List<String> pathItems2 = Arrays.asList("data","0","actorAndRole", "actor", "name");
		List<String> pathItems3 = Arrays.asList("data","0","actorAndRole", "actor");		
		Attribute source1 = new Attribute(pathItems1);
		Attribute source2 = new Attribute(pathItems2);
		Attribute destination = new Attribute(pathItems3);
		List<Attribute> sources = Arrays.asList(source1, source2);		
		
		SourceAttributesFinderTestPurpose generatorTest = new SourceAttributesFinderTestPurpose(item, sources, destination);
		Attribute longestPrefix = generatorTest.getLongestPrefix();
		assertEquals(longestPrefix.getPathItems(), Arrays.asList("data","0","actorAndRole", "actor"));
		
	}
	@Test
	void getLongestPrefixTest2() {
		List<String> pathItems1 = Arrays.asList("data","0","actorAndRole");
		List<String> pathItems2 = Arrays.asList("data","0","actorAndRole", "actor", "name");
		List<String> pathItems3 = Arrays.asList("data","0","actorAndRole", "actor", "newAttribute");		
		Attribute source1 = new Attribute(pathItems1);
		Attribute source2 = new Attribute(pathItems2);
		Attribute destination = new Attribute(pathItems3);
		List<Attribute> sources = Arrays.asList(source1, source2);		
		
		SourceAttributesFinderTestPurpose generatorTest = new SourceAttributesFinderTestPurpose(item, sources, destination);
		Attribute longestPrefix = generatorTest.getLongestPrefix();
		assertEquals(longestPrefix.getPathItems(), Arrays.asList("data","0","actorAndRole", "actor"));
		
	}
	@Test
	void getLongestPrefixTest3() {
		List<String> pathItems1 = Arrays.asList("data","0","actorAndRole", "actor", "name");
		List<String> pathItems2 = Arrays.asList("data","0","actorAndRole", "actor", "rewards", "0", "year");
		List<String> pathItems3 = Arrays.asList("data","0","actorAndRole", "actor", "rewards", "0", "year");		
		Attribute source1 = new Attribute(pathItems1);
		Attribute source2 = new Attribute(pathItems2);
		Attribute destination = new Attribute(pathItems3);
		List<Attribute> sources = Arrays.asList(source1, source2);		
		
		SourceAttributesFinderTestPurpose generatorTest = new SourceAttributesFinderTestPurpose(item, sources, destination);
		Attribute longestPrefix = generatorTest.getLongestPrefix();
		assertEquals(longestPrefix.getPathItems(), Arrays.asList("data","0","actorAndRole", "actor", "rewards", "0", "year"));
		
	}
	@Test
	void getLongestPrefixTest4() {
		List<String> pathItems1 = Arrays.asList("data","0","actorAndRole", "actor", "name");
		List<String> pathItems2 = Arrays.asList("data","0","actorAndRole", "actor", "rewards", "0", "year");
		List<String> pathItems3 = Arrays.asList("data","0","actorAndRole", "actor", "rewards", "1", "year");		
		Attribute source1 = new Attribute(pathItems1);
		Attribute source2 = new Attribute(pathItems2);
		Attribute destination = new Attribute(pathItems3);
		List<Attribute> sources = Arrays.asList(source1, source2);		
		
		SourceAttributesFinderTestPurpose generatorTest = new SourceAttributesFinderTestPurpose(item, sources, destination);
		Attribute longestPrefix = generatorTest.getLongestPrefix();
		assertEquals(longestPrefix.getPathItems(), Arrays.asList("data","0","actorAndRole", "actor", "rewards"));
		
	}
	
	@Test
	void calculateCommonPrefixListCountTest1() {
		List<String> pathItems1 = Arrays.asList("data","0","actorAndRole");
		List<String> pathItems2 = Arrays.asList("data","0","actorAndRole", "actor", "rewards", "0", "year");
		List<String> pathItems3 = Arrays.asList("data","0","actorAndRole", "actor", "newAttribute");		
		Attribute source1 = new Attribute(pathItems1);
		Attribute source2 = new Attribute(pathItems2);
		Attribute destination = new Attribute(pathItems3);
		List<Attribute> sources = Arrays.asList(source1, source2);		
		
		SourceAttributesFinderTestPurpose generatorTest = new SourceAttributesFinderTestPurpose(item, sources, destination);
		Map<Attribute, Integer> commonPrefixListCountTest = generatorTest.calculateCommonPrefixListCount();
		List<Integer> countsTest = new ArrayList<>(commonPrefixListCountTest.values());
		List<Integer> expectedCounts = Arrays.asList(1,1,1);
		assertEquals(expectedCounts, countsTest);	
	}
	@Test
	void calculateCommonPrefixListCountTest2() {
		List<String> pathItems1 = Arrays.asList("data","0","actorAndRole", "actor", "name");
		List<String> pathItems2 = Arrays.asList("data","0","actorAndRole", "actor", "rewards", "0", "year");
		List<String> pathItems3 = Arrays.asList("data","0","actorAndRole", "actor", "rewards", "0", "year");		
		Attribute source1 = new Attribute(pathItems1);
		Attribute source2 = new Attribute(pathItems2);
		Attribute destination = new Attribute(pathItems3);
		List<Attribute> sources = Arrays.asList(source1, source2);		
		
		SourceAttributesFinderTestPurpose generatorTest = new SourceAttributesFinderTestPurpose(item, sources, destination);
		Map<Attribute, Integer> commonPrefixListCountTest = generatorTest.calculateCommonPrefixListCount();
		List<Integer> countsTest = new ArrayList<>(commonPrefixListCountTest.values());
		List<Integer> expectedCounts = Arrays.asList(1,2,2);
		assertEquals(expectedCounts, countsTest );	
	}
	
	@Test
	void generatorTest1() {
		List<String> pathItems1 = Arrays.asList("data","0","actorAndRole", "actor", "name");
		List<String> pathItems2 = Arrays.asList("data","0","actorAndRole", "actor", "rewards", "0", "year");
		List<String> pathItems3 = Arrays.asList("data","0","actorAndRole", "actor", "rewards", "0", "year");		
		Attribute source1 = new Attribute(pathItems1);
		Attribute source2 = new Attribute(pathItems2);
		Attribute destination = new Attribute(pathItems3);
		List<Attribute> sources = Arrays.asList(source1, source2);		
		
		SourceAttributesFinderTestPurpose generatorTest = new SourceAttributesFinderTestPurpose(item, sources, destination);
		List<SourceOccurence> resultTest = new ArrayList<>();
		while(generatorTest.hasNext()) {
			resultTest.add(generatorTest.next());
		}
		assertEquals(5, resultTest.size());
	}
	@Test
	void generatorTest2() {
		List<String> pathItems1 = Arrays.asList("data","0","actorAndRole", "actor", "name");
		List<String> pathItems2 = Arrays.asList("data","0","actorAndRole");
		List<String> pathItems3 = Arrays.asList("data","0","actorAndRole");		
		Attribute source1 = new Attribute(pathItems1);
		Attribute source2 = new Attribute(pathItems2);
		Attribute destination = new Attribute(pathItems3);
		List<Attribute> sources = Arrays.asList(source1, source2);		
		
		SourceAttributesFinderTestPurpose generatorTest = new SourceAttributesFinderTestPurpose(item, sources, destination);
		List<SourceOccurence> resultTest = new ArrayList<>();
		while(generatorTest.hasNext()) {
			resultTest.add(generatorTest.next());
		}
		assertEquals(3, resultTest.size());
	}
	@Test
	void generatorTest3() {
		List<String> pathItems1 = Arrays.asList("data","0","actorAndRole", "actor", "name");
		List<String> pathItems2 = Arrays.asList("data","0","actorAndRole", "actor", "rewards", "0", "year");
		List<String> pathItems3 = Arrays.asList("data","0","actorAndRole", "actor", "rewards", "0", "year");		
		Attribute source1 = new Attribute(pathItems1);
		Attribute source2 = new Attribute(pathItems2);
		Attribute destination = new Attribute(pathItems3);
		List<Attribute> sources = Arrays.asList(source1, source2);		
		
		SourceAttributesFinderTestPurpose generatorTest = new SourceAttributesFinderTestPurpose(item2, sources, destination);
		List<SourceOccurence> resultTest = new ArrayList<>();
		while(generatorTest.hasNext()) {
			resultTest.add(generatorTest.next());
		}
		assertEquals(9, resultTest.size());
	}
	@Test
	void generatorTest4() {
		List<String> pathItems1 = Arrays.asList("data","0","actorAndRole", "actor", "name");
		List<String> pathItems2 = Arrays.asList("data","0","actorAndRole", "actor", "role");	
		Attribute source1 = new Attribute(pathItems1);
		Attribute destination = new Attribute(pathItems2);
		List<Attribute> sources = Arrays.asList(source1);		
		
		SourceAttributesFinderTestPurpose generatorTest = new SourceAttributesFinderTestPurpose(item2, sources, destination);
		List<SourceOccurence> resultTest = new ArrayList<>();
		while(generatorTest.hasNext()) {
			resultTest.add(generatorTest.next());
		}
		assertEquals(4, resultTest.size());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
