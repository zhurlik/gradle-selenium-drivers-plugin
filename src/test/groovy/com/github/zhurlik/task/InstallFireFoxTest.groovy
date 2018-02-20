package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.apache.tools.ant.BuildException
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
    void testUrl() {
        task.browserVersion = '123'
        if (task.isLinux()) {
            assertEquals("https://ftp.mozilla.org/pub/firefox/releases/" +
                    "123/${task.is64() ? 'linux-x86_64' : 'linux-i686'}/en-US/firefox-123.tar.bz2",
                    task.getBrowserUrl())
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
            project.copy {
                from InstallFireFoxTest.getClassLoader().getResource('firefox-fake.tar.bz2').path
                into task.temporaryDir.path
            }

            task.apply()
        }
    }
}
