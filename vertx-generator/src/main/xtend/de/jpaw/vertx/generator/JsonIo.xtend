package de.jpaw.vertx.generator;

import de.jpaw.vertx.lib.Jsonizable
import de.jpaw.vertx.lib.MessageComposer
import de.jpaw.vertx.lib.MessageComposerJson
import java.util.List
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.Visibility

@Active(JsonIoProcessor)
annotation JsonIo {}

class JsonIoProcessor extends AbstractClassProcessor {

    override doTransform(MutableClassDeclaration cls, extension TransformationContext context) {
        val overrideAnnotation = Override.newTypeReference.type
        val list = List.newTypeReference
        
        // erase the annotation in order to avoid a hard runtime dependency on the generator project, and therefore xtend lib / guava
        cls.annotations.findFirst[annotationTypeDeclaration == JsonIo.newTypeReference.type].remove

        // add the Jsonizable interface
        cls.implementedInterfaces = cls.implementedInterfaces + #[ Jsonizable.newTypeReference ]

        // plausi check on all the List types, need the element as a generic arg
        cls.declaredFields.filter[!transient && list.isAssignableFrom(type) && type.actualTypeArguments.size != 1]
            .forEach[addError("Must have a generic parameter which corresponds to the element type")]
            
        // create a method, which, for each of the declared fields, performs the serializer call
        cls.addMethod("serializeSub") [
            visibility = Visibility.PUBLIC
            returnType = primitiveVoid
            val exceptionParam = addTypeParameter("E", Exception.newTypeReference)
            addAnnotation(overrideAnnotation)
            addParameter("writer", MessageComposer.newTypeReference(exceptionParam.newTypeReference))
            exceptions = #[ exceptionParam.newTypeReference ]
            docComment = '''Created by JsonIoProcessor'''
            body = [ '''
                «IF cls.extendedClass != null && cls.extendedClass != Object.newTypeReference»
                    super.serializeSub(writer);
                    writer.writeSuperclassSeparator();
                «ENDIF»
                «FOR fld: cls.declaredFields»
                    «IF !fld.transient»
                        writer.addField("«fld.simpleName»", this.«fld.simpleName»);
                        «IF List.newTypeReference.isAssignableFrom(fld.type) && fld.type.actualTypeArguments.size == 1»
                            if (this.«fld.simpleName» != null) {
                                for («toJavaCode(fld.type.actualTypeArguments.get(0))» _i0 : this.«fld.simpleName»)
                                    writer.addField(null, _i0);
                                writer.terminateList();
                            }
                        «ENDIF»
                    «ENDIF»
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
            // we want lean and these are just DTOs. Do public fields and no getters / setters. If not, uncomment the code below 
            fld.visibility = Visibility.PUBLIC
//            fld.visibility = Visibility.PRIVATE
//            // create a setter
//            cls.addMethod("set" + fld.simpleName.toFirstUpper) [
//                visibility = Visibility.PUBLIC
//                returnType = primitiveVoid
//                addParameter(fld.simpleName, fld.type)
//                body = [ '''this.«fld.simpleName» = «fld.simpleName»;''' ]                
//            ]
//            // create a getter
//            cls.addMethod("get" + fld.simpleName.toFirstUpper) [
//                visibility = Visibility.PUBLIC
//                returnType = fld.type
//                body = [ '''return «fld.simpleName»;''' ]                
//            ]
        }
    }
}

