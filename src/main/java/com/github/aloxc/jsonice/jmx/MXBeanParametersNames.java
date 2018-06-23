package com.github.aloxc.jsonice.jmx;

import java.lang.annotation.*;

/**
 * Created by aloxc on 2018/6/24 in project jsonice.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MXBeanParametersNames {
    String[] value();
}
