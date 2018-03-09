package com.github.zhurlik.task

import org.gradle.api.Task
import org.gradle.internal.os.OperatingSystem
import org.gradle.internal.reflect.JavaReflectionUtil
import org.junit.Rule
import org.junit.rules.ExpectedException

/**
 * Common methods for unit tests.
 *
 * @author zhurlik@gmail.com
 */
abstract class BaseTest {
    private static final OperatingSystem OS = OperatingSystem.current()
    protected Task task

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    /**
     * Wrapper to invoke a private method that returns String.
     *
     * @param method name
     * @return result
     */
    protected String invoke(final String method) {
        return JavaReflectionUtil.method(task, String.class, method).invoke(task)
    }

    /**
     * Wrapper to invoke 'apply' method.
     *
     */
    protected void apply() {
        JavaReflectionUtil.method(task, Object.class, 'apply').invoke(task)
    }

    /**
     * Wrapper to invoke 'installDriver' method.
     *
     */
    protected void invokeInstallDriver() {
        JavaReflectionUtil.method(task, Object.class, 'installDriver').invoke(task)
    }

    /**
     * Wrapper to invoke 'installBrowser' method.
     *
     */
    protected void invokeInstallBrowser() {
        JavaReflectionUtil.method(task, Object.class, 'installBrowser').invoke(task)
    }

    /**
     * When OS is Linux.
     * @return true for Linux
     */
    protected boolean isLinux() {
        OS.isLinux()
    }

    /**
     * When OS is Linux.
     * @return true for Mac OS X
     */
    protected boolean isMac() {
        OS.isMacOsX()
    }

    /**
     *  Is 64 or 32 bit system.
     *
     * @return true when 64 bit system
     */
    protected boolean is64() {
        return isMac() || OS.getNativePrefix().contains('64')
    }
}
