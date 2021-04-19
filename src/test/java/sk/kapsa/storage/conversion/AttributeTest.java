package sk.kapsa.storage.conversion;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AttributeTest {

	@Test
	public void subPathTest1() {
		List<String> pathItems = Arrays.asList("data", "0", "actorAndName", "actor");
		Attribute testPath = new Attribute(pathItems);
		Attribute testSubPath = testPath.subPath(0, 2);
		List<String> expectedPath = Arrays.asList("data", "0");
		assertEquals(testSubPath.getPathItems(), expectedPath);
	}
	@Test
	public void subPathTest2() {
		List<String> pathItems = Arrays.asList("data", "0", "actorAndName", "actor");
		Attribute testPath = new Attribute(pathItems);
		Attribute testSubPath = testPath.subPath(0, 0);
		List<String> expectedPath = Arrays.asList();
		assertEquals(testSubPath.getPathItems(), expectedPath);
	}
	@Test
	public void subPathTest3() {
		List<String> pathItems = Arrays.asList("data", "0", "actorAndName", "actor");
		Attribute testPath = new Attribute(pathItems);
		Attribute testSubPath = testPath.subPath(0, 4);
		List<String> expectedPath = Arrays.asList("data", "0", "actorAndName", "actor");
		assertEquals(testSubPath.getPathItems(), expectedPath);
	}
	
	@Test
	public void commonPrefixTest1() {
		List<String> pathItems1 = Arrays.asList("data", "0", "actorAndName", "actor");
		Attribute testPath1 = new Attribute(pathItems1);
		List<String> pathItems2 = Arrays.asList("data", "0", "actorAndName", "name", "age");
		Attribute testPath2 = new Attribute(pathItems2);
		Attribute testPrefix = testPath1.commonPrefix(testPath2);
		List<String> expectedPath = Arrays.asList("data", "0", "actorAndName");
		assertEquals(testPrefix.getPathItems(), expectedPath);
	}
	@Test
	public void commonPrefixTest2() {
		List<String> pathItems1 = Arrays.asList("data", "0", "actorAndName", "actor");
		Attribute testPath1 = new Attribute(pathItems1);
		List<String> pathItems2 = Arrays.asList("data", "0");
		Attribute testPath2 = new Attribute(pathItems2);
		Attribute testPrefix = testPath1.commonPrefix(testPath2);
		List<String> expectedPath = Arrays.asList("data", "0");
		assertEquals(testPrefix.getPathItems(), expectedPath);
	}
	@Test
	public void commonPrefixTest3() {
		List<String> pathItems1 = Arrays.asList("data", "0", "actorAndName", "actor");
		Attribute testPath1 = new Attribute(pathItems1);
		List<String> pathItems2 = Arrays.asList("data", "0");
		Attribute testPath2 = new Attribute(pathItems2);
		Attribute testPrefix = testPath2.commonPrefix(testPath1);
		List<String> expectedPath = Arrays.asList("data", "0");
		assertEquals(testPrefix.getPathItems(), expectedPath);
	}
	
	

}
