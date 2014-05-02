package de.jpaw.vertx.lib;

public class JsonEscaper {
	public static char[] hextab = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	public static String[] jsonEscapes = new String[128];
	// initialize the escape sequences
	static {
		jsonEscapes['\b'] = "\\b";
		jsonEscapes['\f'] = "\\f";
		jsonEscapes['\r'] = "\\r";
		jsonEscapes['\n'] = "\\n";
		jsonEscapes['\t'] = "\\t";
		jsonEscapes['\"'] = "\\\"";
		jsonEscapes['\\'] = "\\\\";
		for (int i = 0; i < 32; ++i) {
			if (jsonEscapes[i] == null)
				jsonEscapes[i] = "\\u00" + hextab[i/16] + hextab[i & 15];
		}
	}
}
