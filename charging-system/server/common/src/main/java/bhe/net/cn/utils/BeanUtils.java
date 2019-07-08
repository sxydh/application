package bhe.net.cn.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum BeanUtils {
    ;
    static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    private BeanUtils() {

    }

    public static void main(String[] args) {

    }

    public static void PrintFields(Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field f : fields) {
            String fieldName = f.getName();
            try {
                Method m = target.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                System.out.printf("%-40s%-20s%-10s%n", BeanUtils.class, fieldName + ":", m.invoke(target));
            } catch (NoSuchMethodException e) {
                LOGGER.info(e.getLocalizedMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param source
     * @param target
     * @param scope
     *            <ul>
     *            <li>1: copy all
     *            <li>0: copy not empty values
     *            </ul>
     */
    public static void copyProps(Object source, Object target, int scope) {
        Field[] fs_s = source.getClass().getDeclaredFields();
        Field[] fs_t = target.getClass().getDeclaredFields();
        for (Field f_s : fs_s) {
            try {
                String fN_s = f_s.getName();
                Method m_s = source.getClass().getMethod("get" + fN_s.substring(0, 1).toUpperCase() + fN_s.substring(1));
                Object val_s = m_s.invoke(source);
                for (Field f_t : fs_t) {
                    String fN_t = f_t.getName();
                    Method m_t = target.getClass().getMethod("set" + fN_t.substring(0, 1).toUpperCase() + fN_t.substring(1), f_t.getType());
                    if (fN_s.equals(fN_t)) {
                        if (scope == 1) {
                            m_t.invoke(target, val_s);
                        } else if (val_s != null && StringUtils.isNotEmpty(val_s.toString())) {
                            m_t.invoke(target, val_s);
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                LOGGER.info(e.getLocalizedMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param target
     * @param status
     *            <ul>
     *            <li>1: have no empty value
     *            <li>0.5: have not empty value
     *            <li>0: all value is empty
     *            </ul>
     * @param excludes
     * @return
     */
    public static boolean fieldsStatus(Object target, int status, String... excludes) {
        int notEmptyCount = 0;
        int excludesCount = 0;
        boolean result = false;
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field f : fields) {
            String fieldName = f.getName();
            boolean invoke = true;
            for (String exclude : excludes) {
                if (fieldName.equals(exclude)) {
                    excludesCount++;
                    invoke = false;
                    break;
                }
            }
            if (invoke) {
                try {
                    Method m = target.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                    Object value = m.invoke(target);
                    if (value != null && StringUtils.isNotEmpty(value.toString())) {
                        notEmptyCount++;
                    }
                } catch (NoSuchMethodException e) {
                    LOGGER.info(e.getLocalizedMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (status == 0) {
            if (notEmptyCount == 0)
                result = true;
        } else if (status == 0.5) {
            if (notEmptyCount > 0 && notEmptyCount < fields.length - excludesCount)
                result = true;
        } else if (status == 1) {
            if (notEmptyCount == fields.length - excludesCount)
                result = true;
        } else {
            throw new IllegalArgumentException();
        }
        return result;
    }

    public static Object getFieldValue(Object object, String fieldName) {
        Object result = null;
        try {
            Method m = object.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            result = m.invoke(object);
        } catch (NoSuchMethodException e) {
            LOGGER.info(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, Class<?>> getFieldNames_Types(Class<?> clazz) {
        Map<String, Class<?>> map = new HashMap<>();
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }
        for (Field f : fields) {
            map.put(f.getName(), f.getType());
        }
        return map;
    }
}
