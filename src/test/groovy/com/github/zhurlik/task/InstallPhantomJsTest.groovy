package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
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
 * A set of unit tests for {@link InstallPhantomJs}.
 *
 * @author zhurlik@gmail.com
 */
class InstallPhantomJsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    private InstallPhantomJs task
    private Project project

    @Before
    void setUp() throws Exception {
        project = ProjectBuilder.builder().build()

        project.tasks.create('installPhantomJs', InstallPhantomJs)

        task = project.tasks['installPhantomJs']
        assertNotNull(task)
        assertEquals(Browsers.PHANTOMJS, task.browser)
    }

    @Test
    void testApply() {
        if (task.isLinux()) {
            thrown.expect(BuildException)
            thrown.expectMessage(StringContains.containsString('Can\'t get ' +
                    'https://storage.googleapis.com/google-code-archive-downloads/v2/' +
                    'code.google.com/phantomjs/phantomjs-bad-linux-x86_64.tar.bz2 to '))
            task.browserVersion = 'bad'

            task.apply()
        }
    }

    @Test
    void testUrlOnBitbucket() {
        task.browserVersion = 123
        if (task.isLinux()) {
            assertEquals("https://bitbucket.org/ariya/phantomjs/downloads/" +
                    "phantomjs-123-${task.is64() ? 'linux-x86_64' : 'linux-i686'}.tar.bz2",
                    task.getUrlOnBitbucket())
        }
    }

    @Test
    void testUrlOnGoogleCode() {
        task.browserVersion = 321
        if (task.isLinux()) {
            assertEquals("https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/" +
                    "phantomjs-321-${task.is64() ? 'linux-x86_64' : 'linux-i686'}.tar.bz2",
                    task.getUrlOnGoogleCode())
        }
    }

    @Test
    void testApplyUseSkipDownloading() {
        if (task.isLinux()) {
            final String platform = "${task.is64() ? 'linux-x86_64' : 'linux-i686'}"
            // bitbucket
            task.browserVersion = '2.1.1-fake'
            project.copy {
                from InstallFireFoxTest.getClassLoader().getResource('phantomjs-2.1.1-fake-linux-x86_64.tar.bz2').path
                into task.temporaryDir.path
            }

            task.apply()
            assertEquals("${task.project.buildDir}/browser/${Browsers.PHANTOMJS}/2.1.1-fake/phantomjs-${task.browserVersion}-${platform}/bin/phantomjs".toString(),
                    System.properties['phantomjs.binary.path'])
            System.properties.remove('phantomjs.binary.path')
            // google code
            task.browserVersion = '1.9.2-fake'
            project.copy {
                from InstallFireFoxTest.getClassLoader().getResource('phantomjs-1.9.2-fake-linux-x86_64.tar.bz2').path
                into task.temporaryDir.path
            }

            task.apply()

            assertEquals("${task.project.buildDir}/browser/${Browsers.PHANTOMJS}/1.9.2-fake/phantomjs-${task.browserVersion}-${platform}/bin/phantomjs".toString(),
                    System.properties['phantomjs.binary.path'])
        }
    }

    @Test
    void testWindowsInstaller() {
        if (task.isLinux()) {
            thrown.expect(GradleException)
            thrown.expectMessage('phantomjs is not installed:')
            task.windowsInstaller.driverInstaller()
            task.windowsInstaller.browserInstaller()
        }
    }
}