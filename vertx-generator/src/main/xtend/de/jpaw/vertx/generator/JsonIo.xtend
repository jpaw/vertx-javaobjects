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
    	val jsonizable = Jsonizable.newTypeReference
    	
    	// erase the annotation in order to avoid a hard runtime dependency on the generator project, and therefore xtend lib / guava
        cls.annotations.findFirst[annotationTypeDeclaration == JsonIo.newTypeReference.type].remove

		// add the Jsonizable interface
		cls.implementedInterfaces = cls.implementedInterfaces + #[ jsonizable ]

		// for each of the declared fields, create the serializer call
        cls.addMethod("serializeSub") [
        	visibility = Visibility.PUBLIC
        	returnType = primitiveVoid
        	addParameter("_w", MessageComposer.newTypeReference.newWildcardTypeReferenceWithLowerBound(Exception.newTypeReference))
        	docComment = [ '''Created by JsonIoProcessor''']
        ]
        cls.addMethod("encodeJson") [
        	visibility = Visibility.PUBLIC
        	returnType = String.newTypeReference
        	body = [ '''return «toJavaCode(MessageComposerJson.newTypeReference)».encode(this);''']
        ]
    }
}

