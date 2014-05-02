package de.jpaw.vertx.lib;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/** Serializer which writes an object to an Appendable. */

public class MessageComposerJson implements MessageComposer<IOException> {
	protected final Appendable out;
	protected boolean needFieldSeparator = false;
	
	public MessageComposerJson(Appendable out) {
		this.out = out;
	}

	/** Checks if a field separator (',') must be written, and does so if required. Sets the separator to required for the next field. */
	protected void writeSeparator() throws IOException {
		if (needFieldSeparator)
			out.append(',');
		else
			needFieldSeparator = true;
	}

	/** Writes a quoted fieldname. We assume that no escaping is required, because all valid identifier characters in Java don't need escaping. */
	protected void writeFieldName(String fieldname) throws IOException {
		out.append('"');
		out.append(fieldname);
		out.append('"');
		out.append(':');
	}

	@Override
	public void writeNull() throws IOException {
		out.append("null");
	}
	
	@Override
	public void writeNull(String fieldname) throws IOException {
	}

	@Override
	public void writeNullCollection(String fieldname) throws IOException {
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
	public void terminateList() throws IOException {
		out.append(']');
		needFieldSeparator = true;
	}

	@Override
	public void addField(String fieldname, boolean b) throws IOException {
		writeSeparator();
		out.append(b ? "true" : "false");
	}

	@Override
	public void addField(String fieldname, char c) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, double d) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, float f) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, byte n) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, short n) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, int n) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, long n) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, List<?> s) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, String s) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, AlternativeSerializable obj)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, UUID n) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, ByteArray b) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, BigInteger n) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, BigDecimal n) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, LocalDate t) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addField(String fieldname, LocalDateTime t) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
