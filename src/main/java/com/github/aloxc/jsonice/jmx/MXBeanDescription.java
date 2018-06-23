package com.github.aloxc.jsonice.jmx;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface MXBeanDescription {
    public String value();
}