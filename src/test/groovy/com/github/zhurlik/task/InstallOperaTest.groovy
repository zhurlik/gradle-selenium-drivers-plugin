package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.apache.tools.ant.BuildException
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.core.StringContains
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * A set of unit tests for {@link InstallOpera}.
 *
 * @author zhurlik@gmail.com
 */
class InstallOperaTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    private InstallOpera task
    private Project project

    @Before
    void setUp() throws Exception {
        project = ProjectBuilder.builder().build()

        project.tasks.create('installOpera', InstallOpera)

        task = project.tasks['installOpera']
        assertNotNull(task)
        assertEquals(Browsers.OPERA, task.browser)
    }

    @Test
    @Ignore
    void testApply() {
        if (task.isLinux()) {
            thrown.expect(BuildException)
            thrown.expectMessage(StringContains.containsString('Can\'t get ftp://ftp.opera.com/pub/opera/desktop/bad/linux/opera-stable_bad_amd64.deb '))
            task.browserVersion = 'bad'

            task.apply()
        }
    }

    @Test
    void testLinuxInstaller() {
        if (task.isLinux()) {
            task.driverVersion = '2.33'
            task.linuxInstaller.driverInstaller()

            assertEquals("${task.project.buildDir}/driver/${task.driver}/${task.driverVersion}/operadriver_${task.isMacOsX() ? 'mac' : 'linux'}64/operadriver".toString(),
                    System.properties['webdriver.opera.driver'])
            System.properties.remove('webdriver.opera.driver')
        }
    }

    @Test
    void testWindowsInstaller() {
        if (task.isLinux()) {
            thrown.expect(GradleException)
            thrown.expectMessage('selenium-opera-driver is not installed:')
            task.windowsInstaller.driverInstaller()
            task.windowsInstaller.browserInstaller()
        }
    }

    @Test
    void testBrowserUrl() {
        if (task.isLinux()) {
            task.browserVersion = '12222'
            assertEquals('ftp://ftp.opera.com/pub/opera/desktop/12222/linux/opera-stable_12222_amd64.deb', task.getBrowserUrl())
        }
    }

    @Test
    void testDriverUrl() {
        task.driverVersion = '123'
        assertEquals("https://github.com/operasoftware/operachromiumdriver/releases/download/v.123/operadriver_${task.isMacOsX() ? 'mac' : 'linux'}64.zip".toString(),
                task.getDriverUrl())
    }
}