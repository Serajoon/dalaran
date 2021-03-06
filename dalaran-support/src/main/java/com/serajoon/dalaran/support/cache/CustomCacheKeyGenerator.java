package com.serajoon.dalaran.support.cache;

import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;

public class CustomCacheKeyGenerator implements KeyGenerator {

    @NotNull
    @Override
    public Object generate(@NotNull Object target, @NotNull Method method, @NotNull Object... params) {
        return new CacheKey(method.getDeclaringClass(), method.getName(), params);
    }

    /**
     * Like {@link org.springframework.cache.interceptor.SimpleKey} but considers the method.
     */
    @ToString
    static final class CacheKey {

        private final Class<?> clazz;
        private final String methodName;
        private final Object[] params;
        private final int hashCode;


        /**
         * Initialize a key.
         *
         * @param clazz      the receiver class
         * @param methodName the method name
         * @param params     the method parameters
         */
        CacheKey(Class<?> clazz, String methodName, Object[] params) {
            this.clazz = clazz;
            this.methodName = methodName;
            this.params = params;
            this.hashCode = Arrays.deepHashCode(params);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CacheKey)) {
                return false;
            }
            CacheKey other = (CacheKey) obj;
            if (this.hashCode != other.hashCode) {
                return false;
            }
            return this.clazz.equals(other.clazz)
                    && this.methodName.equals(other.methodName)
                    && Arrays.deepEquals(this.params, other.params);
        }
    }
}
