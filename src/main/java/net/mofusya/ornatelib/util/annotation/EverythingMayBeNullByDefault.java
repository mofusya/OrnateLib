package net.mofusya.ornatelib.util.annotation;


import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@FieldsMayBeNullByDefault
@MethodsMayReturnNullByDefault
@ParametersMayBeNullByDefault
@TypeQualifierDefault({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EverythingMayBeNullByDefault {
}
