package com.github.aloxc.jsonice.jmx;

import javax.management.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aloxc on 2018/6/24 in project jsonice.
 */
public class AloxcStandardMXBean extends StandardMBean {

    private static final Map<String, Class<?>> primCls = new HashMap<>();


    static{
        primCls.put(Boolean.TYPE.toString().toLowerCase(), Boolean.TYPE);
        primCls.put(Character.TYPE.toString().toLowerCase(), Character.TYPE);
        primCls.put(Byte.TYPE.toString().toLowerCase(), Byte.TYPE);
        primCls.put(Short.TYPE.toString().toLowerCase(), Short.TYPE);
        primCls.put(Integer.TYPE.toString().toLowerCase(), Integer.TYPE);
        primCls.put(Long.TYPE.toString().toLowerCase(), Long.TYPE);
        primCls.put(Float.TYPE.toString().toLowerCase(), Float.TYPE);
        primCls.put(Double.TYPE.toString().toLowerCase(), Double.TYPE);
    }


    public <T> AloxcStandardMXBean(T implementation, Class<T> mbeanInterface)
            throws NotCompliantMBeanException {
        super(implementation, mbeanInterface);
    }


    @Override protected String getDescription(MBeanAttributeInfo info) {
        String str = super.getDescription(info);

        String methodName = (info.isIs() ? "is" : "get") + info.getName();

        try {
            // Recursively get method.
            Method mtd = findMethod(getMBeanInterface(), methodName, new Class[]{});

            if (mtd != null) {
                MXBeanDescription desc = mtd.getAnnotation(MXBeanDescription.class);

                if (desc != null) {
                    str = desc.value();

                    assert str != null : "Failed to find method: " + mtd;
                    assert str.trim().length() > 0 : "Method description cannot be empty: " + mtd;

                    // Enforce proper English.
                    assert Character.isUpperCase(str.charAt(0)) == true :
                            "Description must start with upper case: " + str;

                    assert str.charAt(str.length() - 1) == '.' : "Description must end with period: " + str;
                }
            }
        }
        catch (SecurityException e) {
            // No-op. Default value will be returned.
        }

        return str;
    }


    @Override protected String getDescription(MBeanInfo info) {
        String str = super.getDescription(info);
        MXBeanDescription desc = MXBeanUtil.getAnnotation(getMBeanInterface(), MXBeanDescription.class);

        if (desc != null) {
            str = desc.value();

            assert str != null;
            assert str.trim().length() > 0;

            // Enforce proper English.
            assert Character.isUpperCase(str.charAt(0)) == true : str;
            assert str.charAt(str.length() - 1) == '.' : str;
        }

        return str;
    }

    @Override protected String getDescription(MBeanOperationInfo info) {
        String str = super.getDescription(info);

        try {
            Method m = getMethod(info);

            MXBeanDescription desc = m.getAnnotation(MXBeanDescription.class);

            if (desc != null) {
                str = desc.value();

                assert str != null;
                assert str.trim().length() > 0;

                // Enforce proper English.
                assert Character.isUpperCase(str.charAt(0)) == true : str;
                assert str.charAt(str.length() - 1) == '.' : str;
            }
        }
        catch (SecurityException | ClassNotFoundException e) {
            // No-op. Default value will be returned.
        }

        return str;
    }

    @Override protected String getDescription(MBeanOperationInfo op, MBeanParameterInfo param, int seq) {
        String str = super.getDescription(op, param, seq);

        try {
            Method m = getMethod(op);

            MXBeanParametersDescriptions decsAnn = m.getAnnotation(MXBeanParametersDescriptions.class);

            if (decsAnn != null) {
                assert decsAnn.value() != null;
                assert seq < decsAnn.value().length;

                str = decsAnn.value()[seq];

                assert str != null;
                assert str.trim().length() > 0;

                // Enforce proper English.
                assert Character.isUpperCase(str.charAt(0)) == true : str;
                assert str.charAt(str.length() - 1) == '.' : str;
            }
        }
        catch (SecurityException | ClassNotFoundException e) {
            // No-op. Default value will be returned.
        }

        return str;
    }

    @Override protected String getParameterName(MBeanOperationInfo op, MBeanParameterInfo param, int seq) {
        String str = super.getParameterName(op, param, seq);

        try {
            Method m = getMethod(op);

            MXBeanParametersNames namesAnn = m.getAnnotation(MXBeanParametersNames.class);

            if (namesAnn != null) {
                assert namesAnn.value() != null;
                assert seq < namesAnn.value().length;

                str = namesAnn.value()[seq];

                assert str != null;
                assert str.trim().length() > 0;
            }
        }
        catch (SecurityException | ClassNotFoundException e) {
            // No-op. Default value will be returned.
        }

        return str;
    }
    private Method getMethod(MBeanOperationInfo op) throws ClassNotFoundException, SecurityException {
        String methodName = op.getName();

        MBeanParameterInfo[] signature = op.getSignature();

        Class<?>[] params = new Class<?>[signature.length];

        for (int i = 0; i < signature.length; i++) {
            // Parameter type is either a primitive type or class. Try both.
            Class<?> type = primCls.get(signature[i].getType().toLowerCase());

            if (type == null)
                type = Class.forName(signature[i].getType());

            params[i] = type;
        }

        return findMethod(getMBeanInterface(), methodName, params);
    }

    @SuppressWarnings("unchecked")
    private Method findMethod(Class itf, String methodName, Class[] params) {
        assert itf.isInterface() == true;

        Method res = null;

        // Try to get method from given interface.
        try {
            res = itf.getDeclaredMethod(methodName, params);

            if (res != null)
                return res;
        }
        catch (NoSuchMethodException e) {
            // No-op. Default value will be returned.
        }

        // Process recursively super interfaces.
        Class[] superItfs = itf.getInterfaces();

        for (Class superItf: superItfs) {
            res = findMethod(superItf, methodName, params);

            if (res != null)
                return res;
        }

        return res;
    }
}