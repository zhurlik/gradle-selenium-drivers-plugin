package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * A set of unit tests for {@link InstallSafari}.
 *
 * @author zhurlik@gmail.com
 */
class InstallSafariTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    private InstallSafari task
    private Project project

    @Before
    void setUp() throws Exception {
        project = ProjectBuilder.builder().build()

        project.tasks.create('installSafari', InstallSafari)

        task = project.tasks['installSafari']
        assertNotNull(task)
        assertEquals(Browsers.SAFARI, task.browser)
        assertEquals(Drivers.SAFARI, task.driver)
        assertEquals('not required', task.browserVersion)
        assertEquals('not required', task.driverVersion)
    }

    @Test
    void testApply() {
        if (task.isLinux()) {
            thrown.expect(UnsupportedOperationException)
            thrown.expectMessage('Safari doesn\'t work on Linux')
            task.apply()
        }
    }

    @Test
    void testLinuxInstaller() {
        if (task.isLinux()) {
            task.linuxInstaller.driverInstaller()
        }
    }

    @Test
    void testWindowsInstaller() {
        if (task.isLinux()) {
            thrown.expect(UnsupportedOperationException)
            thrown.expectMessage('Safari doesn\'t work on Windows')
            task.windowsInstaller.driverInstaller()
            task.windowsInstaller.browserInstaller()
        }
    }

    @Test
    void testMacOsXInstaller() {
        task.macOsInstaller.driverInstaller()
        task.macOsInstaller.browserInstaller()
    }
}