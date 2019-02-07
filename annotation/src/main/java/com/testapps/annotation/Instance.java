package com.testapps.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This will generate a sample method with Prefix field
 * <p>
 * Annotation that will be used in code generation for the fields it marked with.
 * SOURCE - Means this annotation will be discarded from the code after build is completed
 * we can call this compile time annotation they usually are used in code generation
 * TYPE - Element type of this annotation is TYPE which means it can be used with our class
 * type definitions
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Instance {
}
