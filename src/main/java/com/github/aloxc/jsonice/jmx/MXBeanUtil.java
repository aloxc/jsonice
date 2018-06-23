package com.github.aloxc.jsonice.jmx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;

/**
 * Created by aloxc on 2018/6/24 in project jsonice.
 */
public class MXBeanUtil {
    private final static Log logger = LogFactory.getLog(MXBeanUtil.class);
    public static <T extends Annotation> T getAnnotation(Class<?> cls, Class<T> annCls) {
        if (cls == Object.class) {
            return null;
        } else {
            T ann = cls.getAnnotation(annCls);
            if (ann != null) {
                return ann;
            } else {
                Class[] arr$ = cls.getInterfaces();
                int len$ = arr$.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    Class<?> itf = arr$[i$];
                    ann = getAnnotation(itf, annCls);
                    if (ann != null) {
                        return ann;
                    }
                }

                if (!cls.isInterface()) {
                    ann = getAnnotation(cls.getSuperclass(), annCls);
                    if (ann != null) {
                        return ann;
                    }
                }

                return null;
            }
        }
    }
}
