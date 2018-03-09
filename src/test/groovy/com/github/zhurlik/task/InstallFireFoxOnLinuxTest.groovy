package com.github.zhurlik.task

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull


/**
 * Unit tests for {@link InstallFireFoxOnLinux} methods that will be invoked via {@link org.gradle.internal.reflect.JavaReflectionUtil}.
 *
 * @author zhurlik@gmail.com
 */
class InstallFireFoxOnLinuxTest extends BaseTest {
    private String platform

    @Before
    void setUp() throws Exception {
        final Project project = ProjectBuilder.builder().build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        task = project.tasks.create('testInstallFireFox', InstallFireFoxOnLinux)
        assertNotNull(task)
        platform = is64() ? 'linux64' : 'linux32'
    }

    @Test
    void testGetDriverUrl() {
        String res = invoke('getDriverUrl')
        assertEquals("https://github.com/mozilla/geckodriver/releases/download/vnull/geckodriver-vnull-${platform}.tar.gz".toString(), res)

        task.driverVersion = '1234'
        res = invoke('getDriverUrl')
        assertEquals("https://github.com/mozilla/geckodriver/releases/download/v1234/geckodriver-v1234-${platform}.tar.gz".toString(), res)
    }

    @Test
    void testGetBrowserUrl() {
        String res = invoke('getDriverUrl')
        assertEquals("https://github.com/mozilla/geckodriver/releases/download/vnull/geckodriver-vnull-${platform}.tar.gz".toString(), res)

        task.driverVersion = '1234'
        res = invoke('getDriverUrl')
        assertEquals("https://github.com/mozilla/geckodriver/releases/download/v1234/geckodriver-v1234-${platform}.tar.gz".toString(), res)
    }
}
