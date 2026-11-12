package net.mofusya.ornatelib.util.annotation;


import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@FieldsAreNonNullByDefault
@MethodsReturnNonNullByDefault
@ParametersAreNonNullByDefault
@TypeQualifierDefault({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EverythingAreNonNullByDefault {
}
