package de.jpaw.vertx.tests;

import org.testng.annotations.Test;
import java.time.LocalDateTime
import java.util.UUID

class JsonDemo1Test {
	
	@Test
	def public void test1() {
		val test1 = new JsonDemo1 => [
			myInteger = 42
			myString = "Hello, world!"
			myTimestamp = LocalDateTime.now
		]
		
		val json = test1.encodeJson
		System.out.println('''Result of json out is «json»!''')
	} 

	@Test
	def public void testTransient1() {
		val test1 = new JsonTransientDemo1 => [
			myInteger = 42
			myString = "Hello, world!"
			myTimestamp = LocalDateTime.now
		]
		
		val json = test1.encodeJson
		System.out.println('''Result of json transient is «json»!''')
	} 

	@Test
	def public void testList1() {
		val test1 = new JsonListDemo1 => [
			myList = #[ "Hello", "World", "How", "are", "you", "?" ]
			myString = UUID.randomUUID
		]
		
		val json = test1.encodeJson
		System.out.println('''Result of json with list and UUID is «json»!''')
	} 


}