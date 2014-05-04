package de.jpaw.vertx.lib;


public interface Jsonizable {
	
    /** Serializes this object into the format implemented by the MessageComposer parameter. The method will invoke methods of the MessageComposer interface.
     *  Different implementations can be provided.
     *  
     * @param w the implementation of the serializer.
     * @throws E is usually either {@link RuntimeException}, for serializers writing to in-memory buffers, where no checked exceptions are thrown.
     */
    public <E extends Exception> void serializeSub(MessageComposer<E> w) throws E;

    /** Creates a StringBuilder and runs the MessageComposerJson against it. */
    public String encodeJson();

    /** Parses data from a stream or in-memory buffer into a preallocated object.
     * The reference to the IO stream or memory sits in the {@link MessageParser} parameter.
     * Parsers for different serialization formats have been implemented, corresponding to the serializer implementations.
     * 
     * @param p the implementation of the message parser. The generic type E is an exception which is thrown in case of I/O errors or parsing problems. Current implementations use either {@link java.io.IOException} as type for E, or {@link MessageParserException}. 
     * @throws E
     */
//    public <E extends Exception> void deserialize(MessageParser<E> p) throws E;
   
}
