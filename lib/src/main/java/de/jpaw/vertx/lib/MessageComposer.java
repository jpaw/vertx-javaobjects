 /*
  * Copyright 2014 Michael Bischoff
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *   http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.jpaw.vertx.lib;

// Imports of types which are used beyond the Java primitives and their wrappers 
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The MessageComposer interface.
 * It describes the methods a serialization implementation must support.
 *
 * @author Michael Bischoff (jpaw.de)
 */

public interface MessageComposer<E extends Exception> {

    // serialization methods: structure
    public void writeNull() throws E;             					// write a null field as array element (list field)
    public void writeNull(String fieldname) throws E;             	// write a null field
    public void writeNullCollection(String fieldname) throws E;   	// the whole collection is null
    public void writeSuperclassSeparator() throws E;				// written between fields of a superclass and fields of the current class, also at end of the last class
    public void terminateObject() throws E;							// written at end of an object
    public void terminateList() throws E;							// end of a list
    public void terminateMessage() throws E;						// can be used to create a record terminator (CRLF here)
//  public void terminateMap() throws E;							// TODO

    // serialization methods: field type specific

    // primitives
    void addField(String fieldname, boolean b) throws E;
    void addField(String fieldname, char c) throws E;
    void addField(String fieldname, double d) throws E;
    void addField(String fieldname, float f) throws E;
    void addField(String fieldname, byte n) throws E;
    void addField(String fieldname, short n) throws E;
    void addField(String fieldname, int n) throws E;
    void addField(String fieldname, long n) throws E;

    // natively supported classes
    void addField(String fieldname, Enum<?> e) throws E;			// enums are transferred using their name
    void addField(String fieldname, List<?> s) throws E;			// mutable, but immutable derivates exist (guava ImmutableList)
    void addField(String fieldname, String s) throws E;
    void addField(String fieldname, Jsonizable obj) throws E;
    void addField(String fieldname, UUID n) throws E;
    void addField(String fieldname, ByteArray b) throws E;
//    void addField(String fieldname, byte [] b) throws E;			// inherently mutable. Use ByteArray instead!
    void addField(String fieldname, BigInteger n) throws E;
    void addField(String fieldname, BigDecimal n) throws E;
    void addField(String fieldname, LocalDate t) throws E;
    void addField(String fieldname, LocalDateTime t) throws E;
}
