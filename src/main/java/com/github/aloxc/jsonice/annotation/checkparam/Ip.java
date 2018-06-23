package com.github.aloxc.jsonice.annotation.checkparam;

import java.lang.annotation.*;

/**
 * Created by aloxc on 2017/3/1 in project tianya_bbs.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Ip {
    /**
     * 做参数验证的时候取值的字段名称
     */
    String key() default "ip";


    /**
     * 做参数验证的时候先后顺序，值越小代表验证顺序越在前面验证
     */
    int index() default 1;

    /**
     * 是否是从header中取userId
     */
    boolean fromHeader() default true;

    boolean nullable() default true;
}
