package com.github.zhurlik.task

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.internal.reflect.JavaReflectionUtil
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

/**
 * Unit tests for {@link InstallPhantomJsOnLinux} methods that will be invoked via {@link JavaReflectionUtil}.
 *
 * @author zhurlik@gmail.com
 */
class InstallPhantomJsOnLinuxTest {

    private Task task

    @Before
    void setUp() throws Exception {
        final Project project = ProjectBuilder.builder().build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        task = project.tasks['installPhantomJs']
        assertNotNull(task)
    }

    @Test
    void testGetPlatform() {
        final String res = getString('getPlatform')
        assertNotNull(res)
        assertTrue(res in ['linux-x86_64', 'linux-i686'])
    }

    @Test
    void testGetUrlOnGoogleCode() {
        String res = getString('getUrlOnGoogleCode')
        final String platform = getString('getPlatform')
        assertEquals("https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-null-${platform}.tar.bz2".toString(),
                res)

        task.browserVersion = '111'
        res = getString('getUrlOnGoogleCode')
        assertEquals("https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-111-${platform}.tar.bz2".toString(),
                res)
        task.browserVersion = null
    }

    private String getString(final String method) {
        return JavaReflectionUtil.method(task, String.class, method).invoke(task)
    }
}
