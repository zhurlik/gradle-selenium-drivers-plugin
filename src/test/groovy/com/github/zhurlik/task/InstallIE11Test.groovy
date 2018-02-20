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
 * A set of unit tests for {@link InstallIE11}.
 *
 * @author zhurlik@gmail.com
 */
class InstallIE11Test {

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    private InstallIE11 task
    private Project project

    @Before
    void setUp() throws Exception {
        project = ProjectBuilder.builder().build()

        project.tasks.create('installIE11', InstallIE11)

        task = project.tasks['installIE11']
        assertNotNull(task)
        assertEquals(Browsers.IE, task.browser)
    }

    @Test
    void testApply() {
        if (task.isLinux()) {
            thrown.expect(UnsupportedOperationException)
            thrown.expectMessage('Internet Explorer 11 doesn\'t on Linux')
            task.browserVersion = 'bad'

            task.apply()
        }
    }

    @Test
    void testWindowsInstaller() {
        if (task.isLinux()) {
            thrown.expect(GradleException)
            thrown.expectMessage('IE is not installed:')
            task.windowsInstaller.driverInstaller()
            task.windowsInstaller.browserInstaller()
        }
    }
}