package com.testapps.processor;

import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.testapps.annotation.Instance;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Annotation processor. This code will run when the project is being "Build"
 */

// Mark supported annotations
@SupportedAnnotationTypes("com.testapps.annotation.Instance")
public class InstanceProcessor extends AbstractProcessor {

    private final String V4_FRAGMENT = "android.support.v4.app.Fragment";
    // Bundle class specification
    private final ClassName classBundle = ClassName.get("android.os", "Bundle");


    // some objects that we get in "init" method

    // to send messages to application
    private Messager messager;

    // for package name resolutions and related utils
    private Elements elementUtils;

    // for file writing operations
    private Filer filer;

    // init implementation, runs 1st time the processor starts
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {

        // get these references from Current Processing Environment
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();


    }


    // where the code generation will occur
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        // get elements from current environment which are annotated with @Instance
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Instance.class)) {

            // only support annotation on class types.
            // this annotation can only be used on classes so if it is not throw errors

            if (element.getKind() != ElementKind.CLASS) {
                // tell messager object to print message, this object holds reference to current environment.
                messager.printMessage(Diagnostic.Kind.ERROR, getErrorMessage(element));
            } else {

                // found element was CLASS now proceed

                // get element type
                TypeElement typeElement = (TypeElement) element;

                // for our example, we only support Fragment, so we put an extra check on this
                if (!typeElement.getSuperclass().toString().equals(V4_FRAGMENT)) {

                    // the element that was annotated was not of type Support Fragment so throw an error
                    messager.printMessage(Diagnostic.Kind.ERROR, getFragmentError(element, typeElement));

                } else {

                    // get package that is used to create class
                    PackageElement pkg = elementUtils.getPackageOf(element);

                    // class specification that will be generated, the name will be "<FragmentName>Instance"
                    TypeSpec.Builder classSpec = TypeSpec
                            .classBuilder(element.getSimpleName().toString() + "Instance")
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

                    // The Fragment class on which the annotation was used, this is fully qualified name of class with package specifications
                    ClassName classFragment = ClassName.get(pkg.getQualifiedName().toString(), element.getSimpleName().toString());


                    // Method specification, this method name will be "get" so call will looks like "<FragmentName>Instance.get()"
                    // this method will be static so call will look like "FragmentInstance.get()"
                    MethodSpec methodSpec =
                            //method name
                            MethodSpec.methodBuilder("get")
                                    // method modifiers
                                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                    // method parameters
                                    .addParameter(classBundle, "bundle")
                                    // method return type, that is "Fragment type"
                                    .returns(classFragment)
                                    // now method code, this is statement Fragment fragment = new Fragment();
                                    .addStatement("$T fragment = new $T()", classFragment, classFragment)
                                    // statement fragment.setArguments( bundle ) // bundle that was passed  in parameter
                                    .addStatement("fragment.setArguments(bundle)")
                                    // return statement that is : return fragment;
                                    .addStatement("return fragment")
                                    .build();

                    // add above method to our class specification
                    classSpec.addMethod(methodSpec);


                    // write this class as .java class using filer in package as specified by "pkg" of target class
                    // this file will be created in /build directory
                    try {
                        JavaFile.builder(
                                // package where class is to be written
                                pkg.getQualifiedName().toString(),
                                // class specification
                                classSpec.build())
                                // write these to a java file in package mentioned above
                                .build().writeTo(filer);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        // flag true that we have handled the processing
        return true;
    }


    // tells supported annotation type to compiler
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(Instance.class.getCanonicalName());
    }

    // important, default version is 6, so this will result in SOURCE version not supported error
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    private String getFragmentError(Element element, TypeElement typeElement) {
        return "Annotation on " + element.getSimpleName() + ". Only v4 Fragments are supported. Expected: " + V4_FRAGMENT + ", Found: " + typeElement.getSuperclass().toString();
    }

    private String getErrorMessage(Element element) {
        return "This annotation can be only applied to class. Found " + element.getKind().name() + " on definition \"" + element.getSimpleName() + "\"";
    }


}
