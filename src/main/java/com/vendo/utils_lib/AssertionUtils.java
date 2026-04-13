package com.vendo.utils_lib;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class AssertionUtils {

    private static final Logger LOGGER = Logger.getLogger(AssertionUtils.class.getName() );

    public static void assertFrom(Object entity, Object target) {
        assertFrom(entity, target, new String[0]);
    }

    public static void assertFrom(Object entity, Object target, String... skipFields) {
        Map<String, Object> entityData = mapObject(entity);
        Map<String, Object> targetData = mapObject(target);

        for (Map.Entry<String, Object> entry : targetData.entrySet()) {
            Object actual = entityData.get(entry.getKey());

            if (skipFields.length > 0 && StringUtils.contains(entry.getKey(), skipFields)) continue;

            if (!entityData.containsKey(entry.getKey())) {
                throw new IllegalArgumentException(
                        "Missing field in entity: " + entry.getKey()
                );
            }

            if (!Objects.equals(entry.getValue(), actual)) {
                throw new IllegalArgumentException(
                        "Mismatch for field: " + entry.getKey() +
                                ", expected: " + entry.getValue() +
                                ", actual: " + actual
                );
            }
        }
    }

    private static Map<String, Object> mapObject(Object o) {
        Map<String, Object> data = new HashMap<>();

        Class<?> dtoClass = o.getClass();
        Field[] fields = dtoClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                data.put(field.getName(), field.get(o));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unmapped target value: " + field.getName());
            }
        }

        return data;
    }
}
