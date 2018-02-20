package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
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
    void testApply() {
        if (task.isLinux()) {
            thrown.expect(GradleException)
            thrown.expectMessage(StringContains.containsString('Cannot expand TAR '))
            task.browserVersion = 'bad'

            task.apply()
        }
    }

    @Test
    void testWindowsInstaller() {
        if (task.isLinux()) {
            thrown.expect(GradleException)
            thrown.expectMessage('OPERA is not installed:')
            task.windowsInstaller.driverInstaller()
            task.windowsInstaller.browserInstaller()
        }
    }

    @Test
    void testUrl() {
        task.browserVersion = '12222'
        assertEquals('https://www.opera.com/download/index.dml/?os=linux-x86-64&ver=12222&local=y', task.getUrl())
    }
}