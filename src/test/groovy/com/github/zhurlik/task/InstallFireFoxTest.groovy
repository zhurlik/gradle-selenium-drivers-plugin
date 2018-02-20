package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import org.apache.tools.ant.BuildException
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.core.StringContains
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * A set of unit tests for {@link InstallFireFox}.
 *
 * @author zhurlik@gmail.com
 */
class InstallFireFoxTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    private InstallFireFox task
    private Project project

    @Before
    void setUp() throws Exception {
        project = ProjectBuilder.builder().build()

        project.tasks.create('installFireFox', InstallFireFox)

        task = project.tasks['installFireFox']
        assertNotNull(task)
        assertEquals(Browsers.FIREFOX, task.browser)
    }

    @Test
    void testBrowserUrl() {
        task.browserVersion = '123'
        if (task.isLinux()) {
            assertEquals("https://ftp.mozilla.org/pub/firefox/releases/" +
                    "123/${task.is64() ? 'linux-x86_64' : 'linux-i686'}/en-US/firefox-123.tar.bz2",
                    task.getBrowserUrl())
        }
    }

    @Test
    void testDriverUrl() {
        task.driverVersion = '123'
        assertEquals("https://github.com/mozilla/geckodriver/releases/download/" +
                "v123/geckodriver-v123-${task.is64() ? 'linux64': 'linux32'}.tar.gz",
                task.getDriverUrl())
    }

    @Test
    void testWindowsInstaller() {
        if (task.isLinux()) {
            thrown.expect(GradleException)
            thrown.expectMessage('FIREFOX is not installed:')
            task.windowsInstaller.driverInstaller()
            task.windowsInstaller.browserInstaller()
        }
    }

    @Test
    void testApply() {
        if (task.isLinux()) {
            thrown.expect(BuildException)
            thrown.expectMessage(StringContains.containsString('Can\'t get ' +
                    'https://ftp.mozilla.org/pub/firefox/releases/bad/linux-x86_64/en-US/firefox-bad.tar.bz2 '))
            task.browserVersion = 'bad'

            task.apply()
        }
    }

    @Test
    void testApplyUseSkipDownloading() {
        if (task.isLinux()) {
            task.browserVersion = 'fake'
            task.driverVersion = '0.19.1'
            project.copy {
                from InstallFireFoxTest.getClassLoader().getResource('firefox-fake.tar.bz2').path
                into task.temporaryDir.path
            }

            task.apply()
            assertEquals("${task.project.buildDir}/browser/${Browsers.FIREFOX}/fake/firefox/firefox".toString(),
                    System.properties['webdriver.firefox.bin'])
        }
    }

    @Test
    void testInstallDriver() {
        if (task.isLinux()) {
            task.driverVersion = '0.19.1'
            task.linuxInstaller.driverInstaller()
            assertEquals("${task.project.buildDir}/driver/${Drivers.GECKO}/0.19.1/geckodriver".toString(),
                    System.properties['webdriver.gecko.driver'])
        }
    }
}
