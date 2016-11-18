package org.edu.util;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class NullAwareBeanUtilsBean extends BeanUtilsBean {

    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if (value == null)
            return;
        if ((value instanceof Set) && (((Set) value).isEmpty()))
            super.copyProperty(dest, name, value);
    }
}
