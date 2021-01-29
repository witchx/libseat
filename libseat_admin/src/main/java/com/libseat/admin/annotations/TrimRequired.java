package com.libseat.admin.annotations;

import java.lang.annotation.*;

/**
 * @author witch
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TrimRequired {
}