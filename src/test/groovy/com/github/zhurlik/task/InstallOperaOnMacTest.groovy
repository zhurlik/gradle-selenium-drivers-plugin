package com.github.zhurlik.task

import org.apache.tools.ant.BuildException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.core.StringContains
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull

/**
 * Unit tests for {@link InstallOperaOnMac} methods that will be invoked via {@link org.gradle.internal.reflect.JavaReflectionUtil}.
 *
 * @author zhurlik@gmail.com
 */
class InstallOperaOnMacTest extends BaseTest {

    @Before
    void setUp() throws Exception {
        final Project project = ProjectBuilder.builder().build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        task = project.tasks.create('testInstallOpera', InstallOperaOnMac)
        assertNotNull(task)
    }

    @Test
    void testGetDriverUrl() {
        String res = invoke('getDriverUrl')
        assertEquals('https://github.com/operasoftware/operachromiumdriver/releases/download/v.null/operadriver_mac64.zip', res)

        task.driverVersion = '1234'
        res = invoke('getDriverUrl')
        assertEquals('https://github.com/operasoftware/operachromiumdriver/releases/download/v.1234/operadriver_mac64.zip', res)
    }

    @Test
    void testGetBrowserUrl() {
        String res = invoke('getBrowserUrl')
        assertEquals('ftp://ftp.opera.com/pub/opera/desktop/null/mac/Opera_null_Setup.dmg', res)

        task.browserVersion = '1234'
        res = invoke('getBrowserUrl')
        assertEquals('ftp://ftp.opera.com/pub/opera/desktop/1234/mac/Opera_1234_Setup.dmg', res)
    }

    @Test
    void testInstallDriverWrong() {
        thrown.expect(BuildException)
        thrown.expectMessage(StringContains.containsString('Can\'t get https://github.com/operasoftware/operachromiumdriver/releases/download/v.null/operadriver_mac64.zip '))
        invokeInstallDriver()
        assertNull(System.properties['webdriver.opera.driver'])
    }

    @Test
    void testInstallBrowserWrong() {
        thrown.expect(BuildException)
        thrown.expectMessage(StringContains.containsString('ftp://ftp.opera.com/pub/opera/desktop/null/mac/Opera_null_Setup.dmg'))
        invokeInstallBrowser()
        assertNull(System.properties['webdriver.opera.bin'])
    }
}
