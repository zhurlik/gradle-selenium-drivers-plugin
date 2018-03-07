package com.github.zhurlik.task

import org.apache.tools.ant.BuildException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.core.StringContains
import org.junit.Before
import org.junit.Test

import java.nio.file.Paths

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull

/**
 * Unit tests for {@link InstallChromeOnLinux} methods that will be invoked via {@link org.gradle.internal.reflect.JavaReflectionUtil}.
 *
 * @author zhurlik@gmail.com
 */
class InstallChromeOnLinuxTest extends BaseTest {

    @Before
    void setUp() throws Exception {
        final Project project = ProjectBuilder.builder().build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        task = project.tasks.create('testInstallChrome', InstallChromeOnLinux)
        assertNotNull(task)
    }

    @Test
    void testGetDriverUrl() {
        String res = invoke('getDriverUrl')
        assertEquals("https://chromedriver.storage.googleapis.com/null/chromedriver_linux64.zip".toString(), res)

        task.driverVersion = '1234'
        res = invoke('getDriverUrl')
        assertEquals("https://chromedriver.storage.googleapis.com/1234/chromedriver_linux64.zip".toString(), res)
    }

    @Test
    void testInstallBrowser() {
        invokeInstallBrowser()
    }

    @Test
    void testInstallDriverWrong() {
        thrown.expect(BuildException)
        thrown.expectMessage(StringContains.containsString('Can\'t get https://chromedriver.storage.googleapis.com/null/chromedriver_linux64.zip'))
        invokeInstallDriver()
        assertNull(System.properties['webdriver.chrome.driver'])
    }

    @Test
    void testInstallDriver() {
        task.driverVersion = '2.36'
        invokeInstallDriver()
        assertEquals(Paths.get(task.project.buildDir.path, 'driver', 'CHROME', '2.36', 'chromedriver').toString(),
                System.properties['webdriver.chrome.driver'])
    }

    @Test
    void testApply() {
        task.driverVersion = '2.36'
        apply()
        assertEquals(Paths.get(task.project.buildDir.path, 'driver', 'CHROME', '2.36', 'chromedriver').toString(),
                System.properties['webdriver.chrome.driver'])
    }

}
