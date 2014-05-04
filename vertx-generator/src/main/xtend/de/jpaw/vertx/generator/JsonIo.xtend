package de.jpaw.vertx.generator;

import de.jpaw.vertx.lib.Jsonizable
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.Visibility
import de.jpaw.vertx.lib.MessageComposerJson
import de.jpaw.vertx.lib.MessageComposer

@Active(JsonIoProcessor)
annotation JsonIo {}

class JsonIoProcessor extends AbstractClassProcessor {

    override doTransform(MutableClassDeclaration cls, extension TransformationContext context) {
		val overrideAnnotation = Override.newTypeReference.type
		    	
    	// erase the annotation in order to avoid a hard runtime dependency on the generator project, and therefore xtend lib / guava
        cls.annotations.findFirst[annotationTypeDeclaration == JsonIo.newTypeReference.type].remove

		// add the Jsonizable interface
		cls.implementedInterfaces = cls.implementedInterfaces + #[ Jsonizable.newTypeReference ]

		// create a method, which, for each of the declared fields, performs the serializer call
        cls.addMethod("serializeSub") [
        	visibility = Visibility.PUBLIC
        	returnType = primitiveVoid
        	val exceptionParam = addTypeParameter("E", Exception.newTypeReference)
        	addAnnotation(overrideAnnotation)
        	addParameter("writer", MessageComposer.newTypeReference(exceptionParam.newTypeReference))
        	exceptions = #[ exceptionParam.newTypeReference ]
        	docComment = '''Created by JsonIoProcessor
        	method signature should be
        	public <E extends Exception> void serializeSub(final MessageComposer<E> writer) throws E {
        	'''
        	body = [ '''
        		«IF cls.extendedClass != null && cls.extendedClass != Object.newTypeReference»
        			super.serializeSub(writer);
        			writer.writeSuperclassSeparator();
        		«ENDIF»
        		«FOR fld: cls.declaredFields»
        			writer.addField("«fld.simpleName»", this.«fld.simpleName»);
        		«ENDFOR»
        	''']
        ]
        // just sugar, it forwards to a static method in the class MessageComposerJson
        cls.addMethod("encodeJson") [
        	visibility = Visibility.PUBLIC
        	returnType = String.newTypeReference
        	addAnnotation(overrideAnnotation)
        	docComment = '''Created by JsonIoProcessor'''
        	body = [ '''return «toJavaCode(MessageComposerJson.newTypeReference)».encode(this);''']
        ]
        for (fld: cls.declaredFields) {
        	// create a setter
        	cls.addMethod("set" + fld.simpleName.toFirstUpper) [
	        	visibility = Visibility.PUBLIC
    	    	returnType = primitiveVoid
				addParameter(fld.simpleName, fld.type)
				body = [ '''this.«fld.simpleName» = «fld.simpleName»;''' ]        		
        	]
        	// create a getter
        	cls.addMethod("get" + fld.simpleName.toFirstUpper) [
	        	visibility = Visibility.PUBLIC
    	    	returnType = fld.type
				body = [ '''return «fld.simpleName»;''' ]        		
        	]
        }
    }
}

