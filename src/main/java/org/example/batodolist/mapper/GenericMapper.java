package org.example.batodolist.mapper;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class GenericMapper {

    public static <S, T> T map(S source, Class<T> targetClass) {
        if (source == null) return null;

        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            copy(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Mapping error: " + e.getMessage(), e);
        }
    }

    public static <S, T> void copy(S source, T target) {
        if (source == null || target == null) return;

        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = target.getClass().getDeclaredFields();

        for (Field sf : sourceFields) {
            try {
                sf.setAccessible(true);
                Object value = sf.get(source);

                for (Field tf : targetFields) {
                    if (tf.getName().equals(sf.getName())
                            && tf.getType().isAssignableFrom(sf.getType())) {

                        tf.setAccessible(true);
                        tf.set(target, value);
                        break;
                    }
                }

            } catch (Exception ignored) {}
        }
    }
}
