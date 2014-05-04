package de.jpaw.vertx.tests;

import org.testng.annotations.Test;
import java.time.LocalDateTime

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
}