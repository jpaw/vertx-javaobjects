package de.jpaw.vertx.tests;

import org.testng.annotations.Test;
import java.time.LocalDateTime
import java.util.UUID
import de.jpaw.vertx.lib.ByteArray
import org.vertx.java.core.json.JsonObject

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


	@Test
	def public void testBinary1() {
		val DATA = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."; 
		val test1 = new JsonBinaryDemo1 => [
			data = new ByteArray(DATA.bytes)
		]
		
		val json = test1.encodeJson
		System.out.println('''Result of json with binary data is «json»!''')
		
		// see the reference
		val j = new JsonObject => [
			putBoolean("mybool", true)
			putBinary("mybytes", DATA.bytes)
			putString("nothere", null)
		]
		System.out.println('''Result of json with binary data is «j.encode»!''')
		
	} 

}