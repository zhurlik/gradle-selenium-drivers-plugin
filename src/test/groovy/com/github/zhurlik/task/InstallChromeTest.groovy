package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * A set of unit tests for {@link InstallChrome}.
 *
 * @author zhurlik@gmail.com
 */
class InstallChromeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    private InstallChrome task
    private Project project

    @Before
    void setUp() throws Exception {
        project = ProjectBuilder.builder().build()

        project.tasks.create('installChrome', InstallChrome)

        task = project.tasks['installChrome']
        assertNotNull(task)
        assertEquals(Browsers.CHROME, task.browser)
    }

    @Test
    void testApply() {
        if (task.isLinux()) {
            thrown.expect(GradleException)
            thrown.expectMessage('Not implemented yet')
            task.browserVersion = 'bad'

            task.apply()
        }
    }

    @Test
    void testLinuxInstaller() {
        if (task.isLinux()) {
            task.driverVersion = '2.35'
            task.linuxInstaller.driverInstaller()
            assertEquals("${task.project.buildDir.path}/driver/${task.driver}/${task.driverVersion}/chromedriver".toString(),
                    System.properties['webdriver.chrome.driver'])
            System.properties.remove('webdriver.chrome.driver')
        }
    }

    @Test
    void testWindowsInstaller() {
        if (task.isLinux()) {
            thrown.expect(GradleException)
            thrown.expectMessage('selenium-chrome-driver is not installed:')
            task.windowsInstaller.driverInstaller()
            task.windowsInstaller.browserInstaller()
        }
    }

    @Test
    void tstGetDriverUrl() {
        if (task.isLinux() || task.isMacOsX()) {
            task.driverVersion = 1122
            assertEquals("https://chromedriver.storage.googleapis.com/1122/chromedriver_${task.isMacOsX() ? 'mac' : 'linux'}64.zip".toString(),
                    task.getDriverUrl())
        }
    }
}