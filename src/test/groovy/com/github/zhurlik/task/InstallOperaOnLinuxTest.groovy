package com.github.zhurlik.task

import org.apache.tools.ant.BuildException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.core.StringContains
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import java.nio.file.Paths

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull

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

        task = project.tasks.create('testInstallOpera', InstallOperaOnLinux)
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

    @Test
    void testInstallDriverWrong() {
        thrown.expect(BuildException)
        thrown.expectMessage(StringContains.containsString('https://github.com/operasoftware/operachromiumdriver/releases/download/v.null/operadriver_linux64.zip'))
        invokeInstallDriver()
        assertNull(System.properties['webdriver.opera.driver'])
    }

    @Test
    void testInstallDriver() {
        task.driverVersion = '2.33'
        invokeInstallDriver()
        assertEquals(Paths.get(task.project.buildDir.path, 'driver', 'OPERA', '2.33', 'operadriver_linux64',
                'operadriver').toString(), System.properties['webdriver.opera.driver'])
    }

    @Test
    void testInstallBrowserWrong() {
        thrown.expect(BuildException)
        thrown.expectMessage(StringContains.containsString('ftp://ftp.opera.com/pub/opera/desktop/null/linux/opera-stable_null_amd64.deb'))
        invokeInstallBrowser()
        assertNull(System.properties['webdriver.opera.bin'])
    }

    @Test
    @Ignore
    void testInstallBrowser() {
        task.browserVersion = '51.0.2830.40'
        invokeInstallBrowser()
        assertEquals(Paths.get(task.project.buildDir.path, 'browser', 'OPERA', '51.0.2830.40', 'opera').toString(),
                System.properties['webdriver.opera.bin'])
    }

    @Test
    @Ignore
    void testApply() {
        task.driverVersion = '2.33'
        task.browserVersion = '51.0.2830.40'
        apply()
        assertEquals(Paths.get(task.project.buildDir.path, 'driver', 'OPERA', '2.33', 'operadriver_linux64',
                'operadriver').toString(), System.properties['webdriver.opera.driver'])
        assertEquals(Paths.get(task.project.buildDir.path, 'browser', 'OPERA', '51.0.2830.40', 'opera').toString(),
                System.properties['webdriver.opera.bin'])
    }
}
