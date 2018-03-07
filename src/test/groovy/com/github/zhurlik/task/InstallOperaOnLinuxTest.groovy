package com.github.zhurlik.task

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * Unit tests for {@link InstallOperaOnLinux} methods that will be invoked via {@link org.gradle.internal.reflect.JavaReflectionUtil}.
 *
 * @author zhurlik@gmail.com
 */
class InstallOperaOnLinuxTest extends BaseTest {
    @Before
    void setUp() throws Exception {
        final Project project = ProjectBuilder.builder().build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        task = project.tasks.create('testInstallChrome', InstallOperaOnLinux)
        assertNotNull(task)
    }

    @Test
    void testGetDriverUrl() {
        String res = invoke('getDriverUrl')
        assertEquals('https://github.com/operasoftware/operachromiumdriver/releases/download/v.null/operadriver_linux64.zip', res)

        task.driverVersion = '1234'
        res = invoke('getDriverUrl')
        assertEquals('https://github.com/operasoftware/operachromiumdriver/releases/download/v.1234/operadriver_linux64.zip', res)
    }

    @Test
    void testGetBrowserUrl() {
        String res = invoke('getBrowserUrl')
        assertEquals('ftp://ftp.opera.com/pub/opera/desktop/null/linux/opera-stable_null_amd64.deb', res)

        task.browserVersion = '1234'
        res = invoke('getBrowserUrl')
        assertEquals('ftp://ftp.opera.com/pub/opera/desktop/1234/linux/opera-stable_1234_amd64.deb', res)
    }
}
