package de.jpaw.vertx.tests;

import de.jpaw.vertx.generator.JsonIo
import java.time.LocalDateTime
import java.util.List
import java.util.UUID
import de.jpaw.vertx.lib.ByteArray

@JsonIo
public class JsonDemo1 {
	int myInteger;
	String myString;
	LocalDateTime myTimestamp;
}

@JsonIo
public class JsonTransientDemo1 {
	transient int myInteger;
	String myString;
	transient LocalDateTime myTimestamp;
}

@JsonIo
public class JsonListDemo1 {
	List<String> myList;
	UUID myString;
}


@JsonIo
public class JsonBinaryDemo1 {
	ByteArray data;
}