package pw.dotdash.township.api.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.TypeUseLocation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NonNull
@DefaultQualifierInHierarchy
@DefaultQualifier(value = NonNull.class, locations = {
        TypeUseLocation.ALL
})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonnullByDefault {

}