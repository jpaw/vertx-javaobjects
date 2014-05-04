package de.jpaw.vertx.tests;

import de.jpaw.vertx.generator.JsonIo
import java.time.LocalDateTime

@JsonIo
public class JsonDemo1 {
	int myInteger;
	String myString;
	LocalDateTime myTimestamp;
}