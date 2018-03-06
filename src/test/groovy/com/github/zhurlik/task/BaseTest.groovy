package com.github.zhurlik.task

import org.gradle.internal.reflect.JavaReflectionUtil

abstract class BaseTest {

    /**
     * Wrapper to invoke a private method that returns String.
     *
     * @param method name
     * @return result
     */
    protected String getString(final String method) {
        return JavaReflectionUtil.method(task, String.class, method).invoke(task)
    }
}
