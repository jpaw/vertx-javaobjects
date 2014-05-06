package de.jpaw.vertx.lib;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.UUID;


/** Serializer which writes an object to an Appendable.
 * An instance of this class is not multithreaded. A separate instance is required per stream, because it holds internal state. */

public class MessageComposerJson implements MessageComposer<IOException> {
	protected final Appendable out;
	protected final boolean writeNulls;
	protected final boolean fullEnumMode = false;
	
	protected boolean needFieldSeparator = false;
	
	public MessageComposerJson(Appendable out) {
		this.out = out;
		this.writeNulls = false;
	}

	public MessageComposerJson(Appendable out, boolean writeNulls) {
		this.out = out;
		this.writeNulls = writeNulls;
	}
	
	
	/** shorthand method. Creates its own serializer and a StringBuilder as buffer and runs the serializer on the passed object. */
	public static String encode(Jsonizable obj) {
		StringBuilder buffer = new StringBuilder(500);
		MessageComposerJson writer = new MessageComposerJson(buffer);
		try {
			writer.addField(null, obj);
			writer.terminateMessage();
		} catch (IOException e) {
			// cannot happen on a StringBuilder, or...?
			throw new RuntimeException("IOException writing to StringBuilder!?", e);
		}
		return buffer.toString();
	}

	/** Checks if a field separator (',') must be written, and does so if required. Sets the separator to required for the next field. */
	protected void writeSeparator() throws IOException {
		if (needFieldSeparator)
			out.append(',');
		else
			needFieldSeparator = true;
	}

	/** Writes a quoted string. We know that we don't need escaping. */
	protected void writeStringUnescaped(String s) throws IOException {
		out.append('"');
		out.append(s);
		out.append('"');
	}

	/** Writes a quoted fieldname. We assume that no escaping is required, because all valid identifier characters in Java don't need escaping. */
	protected void writeFieldName(String fieldname) throws IOException {
		writeSeparator();
		writeStringUnescaped(fieldname);
		out.append(':');
	}

	/** Writes a quoted fieldname, if not in an array, or a separator only. fieldname is passed as null, if this is a List element */
	protected void writeOptionalFieldName(String fieldname) throws IOException {
		if (fieldname == null) {
			// inside array: must write without a name
			writeSeparator();
		} else {
			writeFieldName(fieldname);
		}
	}

	protected void writeOptionalString(String fieldname, String s) throws IOException {
		if (fieldname == null) {
			// must write a null without a name
			writeSeparator();
			out.append(s == null ? "null" : s);
		} else if (s != null) {
			writeFieldName(fieldname);
			out.append(s);
		} else if (writeNulls) {
			writeFieldName(fieldname);
			out.append("null");
		}
	}
	
	protected void writeOptionalQuotedString(String fieldname, String s) throws IOException {
		if (fieldname == null) {
			// must write a null without a name
			writeSeparator();
			if (s == null)
				out.append("null");
			else
				writeStringUnescaped(s);
		} else if (s != null) {
			writeFieldName(fieldname);
			writeStringUnescaped(s);
		} else if (writeNulls) {
			writeFieldName(fieldname);
			out.append("null");
		}
	}
	
	@Override
	public void writeNull() throws IOException {
		out.append("null");
	}
	
	@Override
	public void writeNull(String fieldname) throws IOException {
		if (fieldname == null) {
			// must write a null without a name
			writeSeparator();
			out.append("null");
		} else if (writeNulls) {
			writeFieldName(fieldname);
			out.append("null");
		}
	}

	@Override
	public void writeNullCollection(String fieldname) throws IOException {
		if (writeNulls) {
			writeFieldName(fieldname);
			out.append("null");
		}
	}

	@Override
	public void writeSuperclassSeparator() throws IOException {
	}

	@Override
	public void terminateObject() throws IOException {
		out.append('}');
		needFieldSeparator = true;
	}

	@Override
	public void terminateMessage() throws IOException {
		out.append('\r');
		out.append('\n');
		needFieldSeparator = false;
	}
	
	@Override
	public void terminateList() throws IOException {
		out.append(']');
		needFieldSeparator = true;
	}

	@Override
	public void addField(String fieldname, boolean b) throws IOException {
		writeOptionalFieldName(fieldname);
		out.append(b ? "true" : "false");
	}

	@Override
	public void addField(String fieldname, char c) throws IOException {
		writeOptionalFieldName(fieldname);
		JsonEscaper.outputEscapedString(out, String.valueOf(c));
	}

	@Override
	public void addField(String fieldname, double d) throws IOException {
		writeOptionalFieldName(fieldname);
		out.append(Double.toString(d));
	}

	@Override
	public void addField(String fieldname, float f) throws IOException {
		writeOptionalFieldName(fieldname);
		out.append(Float.toString(f));
	}

	@Override
	public void addField(String fieldname, byte n) throws IOException {
		writeOptionalFieldName(fieldname);
		out.append(Byte.toString(n));
	}

	@Override
	public void addField(String fieldname, short n) throws IOException {
		writeOptionalFieldName(fieldname);
		out.append(Short.toString(n));
	}

	@Override
	public void addField(String fieldname, int n) throws IOException {
		writeOptionalFieldName(fieldname);
		out.append(Integer.toString(n));
	}

	@Override
	public void addField(String fieldname, long n) throws IOException {
		writeOptionalFieldName(fieldname);
		out.append(Long.toString(n));
	}

	@Override
	public void addField(String fieldname, List<?> l) throws IOException {
		if (l == null) {
			writeNullCollection(fieldname);
		} else {
			if (fieldname == null) {
				// must write a null without a name
				writeSeparator();
			} else {
				writeFieldName(fieldname);
			}
			out.append('[');
			needFieldSeparator = false;
		}
	}

	@Override
	public void addField(String fieldname, String s) throws IOException {
		if (fieldname == null) {
			// must write a null without a name
			writeSeparator();
			if (s == null)
				out.append("null");
			else
				JsonEscaper.outputEscapedString(out, s);
		} else if (s != null) {
			writeFieldName(fieldname);
			JsonEscaper.outputEscapedString(out, s);
		} else if (writeNulls) {
			writeFieldName(fieldname);
			out.append("null");
		} // else don't write at all
	}

	// called for not-null elements only
	protected void startObject(String fieldname, Jsonizable obj) throws IOException {
		out.append('{');
		// create the class canonical name as a special field "@type", to be compatible to json-io
		writeStringUnescaped("@type");
		out.append(':');
		writeStringUnescaped(obj.getClass().getCanonicalName());
		needFieldSeparator = true;
		obj.serializeSub(this);
		terminateObject();
	}
	@Override
	public void addField(String fieldname, Jsonizable obj) throws IOException {
		if (fieldname == null) {
			// must write a null without a name
			writeSeparator();
			if (obj == null)
				out.append("null");
			else
				startObject(fieldname, obj);
		} else if (obj != null) {
			writeFieldName(fieldname);
			startObject(fieldname, obj);
		} else if (writeNulls) {
			writeFieldName(fieldname);
			out.append("null");
		} // else don't write at all
		
	}

	@Override
	public void addField(String fieldname, UUID n) throws IOException {
		writeOptionalQuotedString(fieldname, n == null ? null : n.toString());
	}

	@Override
	public void addField(String fieldname, ByteArray b) throws IOException {
		writeOptionalQuotedString(fieldname, b == null ? null : new String(b.getBase64asByte()));
	}

	@Override
	@Deprecated
	public void addField(String fieldname, byte [] b) throws IOException {
		// TODO: encode() is much faster than encodeToString(). We should switch to that, especially as we need byte [] in the end anyway.
		writeOptionalQuotedString(fieldname, b == null ? null : Base64.getEncoder().encodeToString(b));
	}

	@Override
	public void addField(String fieldname, BigInteger n) throws IOException {
		writeOptionalQuotedString(fieldname, n == null ? null : n.toString());
	}

	@Override
	public void addField(String fieldname, BigDecimal n) throws IOException {
		writeOptionalQuotedString(fieldname, n == null ? null : n.toString());
	}

	@Override
	public void addField(String fieldname, LocalDate t) throws IOException {
		writeOptionalQuotedString(fieldname, t == null ? null : DateTimeFormatter.ISO_LOCAL_DATE.format(t));
	}

	@Override
	public void addField(String fieldname, LocalDateTime t) throws IOException {
		writeOptionalQuotedString(fieldname, t == null ? null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(t));
	}

	/** Writes information of an enum instance. In "full" mode, the class name is written as field "@enum", the token itself as field "name".
	 * In "brief" mode, it just outputs the name as the field value. */
	protected void enumOut(Enum<?> e) throws IOException {
		if (fullEnumMode) {
			out.append("{\"@enum\":");
			writeStringUnescaped(e.getClass().getCanonicalName());
			out.append(",\"name\":");
			writeStringUnescaped(e.name());
			out.append('}');
		} else {
			writeStringUnescaped(e.name());
		}
	}
	
	@Override
	public void addField(String fieldname, Enum<?> e) throws IOException {
		if (fieldname == null) {
			// must write a null without a name
			writeSeparator();
			if (e == null)
				out.append("null");
			else
				enumOut(e);
		} else if (e != null) {
			writeFieldName(fieldname);
			enumOut(e);
		} else if (writeNulls) {
			writeFieldName(fieldname);
			out.append("null");
		}
	}

}
