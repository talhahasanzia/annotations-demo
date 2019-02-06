package com.testapps.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

 /**
 * This annotation will call default constructor of objects which are annotated by this.
 * This is annotation to mark fields which will be initialized at runtime.
 * RUNTIME - this makes this annotation available at runtime, means it will be shipped with actual
 * code even after builds
 * FIELD - Element type of this annotation is FIELD which means it can be used with our class
 * members, which is our current requirement
 *
 */
 @Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.FIELD)
 public @interface Init {
 }
