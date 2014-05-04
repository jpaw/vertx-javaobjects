package de.jpaw.vertx.lib;

public interface Jsonizable {
    /** Serializes this object into the format implemented by the MessageComposer parameter. The method will invoke methods of the MessageComposer interface for every member field, and also for some metadata. Class headers itself are assumed to have been serialized before.
     *  Different implementations are provided with the bonaparte library, for ASCII-like formats (bonaparte) or binary formats plugging into the standard Java {@link java.io.Serializable}/{@link java.io.Externalizable} interface.
     *  
     * @param w the implementation of the serializer.
     * @throws E is usually either {@link RuntimeException}, for serializers writing to in-memory buffers, where no checked exceptions are thrown, or {@link java.io.IOException}, for serializers writing to streams. 
     */
    public <E extends Exception> void serializeSub(MessageComposer<E> w) throws E;
    
    /** Parses data from a stream or in-memory buffer into a preallocated object.
     * The reference to the IO stream or memory sits in the {@link MessageParser} parameter.
     * Parsers for different serialization formats have been implemented, corresponding to the serializer implementations.
     * 
     * @param p the implementation of the message parser. The generic type E is an exception which is thrown in case of I/O errors or parsing problems. Current implementations use either {@link java.io.IOException} as type for E, or {@link MessageParserException}. 
     * @throws E
     */
//    public <E extends Exception> void deserialize(MessageParser<E> p) throws E;
   
}
