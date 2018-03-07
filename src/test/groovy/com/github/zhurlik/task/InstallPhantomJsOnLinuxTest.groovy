package com.github.zhurlik.task

import org.apache.tools.ant.BuildException
import org.gradle.api.Project
import org.gradle.internal.reflect.JavaReflectionUtil
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.core.StringContains
import org.junit.Before
import org.junit.Test

import java.nio.file.Paths

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue

/**
 * Unit tests for {@link InstallPhantomJsOnLinux} methods that will be invoked via {@link JavaReflectionUtil}.
 *
 * @author zhurlik@gmail.com
 */
class InstallPhantomJsOnLinuxTest extends BaseTest {

    @Before
    void setUp() throws Exception {
        final Project project = ProjectBuilder.builder().build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        task = project.tasks.create('testInstallPhantomJs', InstallPhantomJsOnLinux)
        assertNotNull(task)
    }

    @Test
    void testGetPlatform() {
        final String res = invoke('getPlatform')
        assertNotNull(res)
        assertTrue(res in ['linux-x86_64', 'linux-i686'])
    }

    @Test
    void testGetUrlOnGoogleCode() {
        String res = invoke('getUrlOnGoogleCode')
        final String platform = invoke('getPlatform')
        assertEquals("https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-null-${platform}.tar.bz2".toString(),
                res)

        task.browserVersion = '111'
        res = invoke('getUrlOnGoogleCode')
        assertEquals("https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-111-${platform}.tar.bz2".toString(),
                res)
    }

    @Test
    void testGetUrlOnBitbucket() {
        String res = invoke('getUrlOnBitbucket')
        final String platform = invoke('getPlatform')
        assertEquals("https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-null-${platform}.tar.bz2".toString(),
                res)

        task.browserVersion = '123'
        res = invoke('getUrlOnBitbucket')
        assertEquals("https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-123-${platform}.tar.bz2".toString(),
                res)
    }

    @Test
    void testDownloadInstallerWrongUrl() {
        thrown.expect(BuildException)
        thrown.expectMessage(StringContains.containsString(
                'Can\'t get https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-null-linux-x86_64.tar.bz2 '))
        invoke('downloadInstaller')
    }

    @Test
    void testDownloadInstallerSkip() {
        task.browserVersion = '1.9.2-fake'
        task.project.copy {
            from this.class.getClassLoader().getResource('phantomjs-1.9.2-fake-linux-x86_64.tar.bz2')
            into task.temporaryDir
        }
        final String fileName = invoke('downloadInstaller')
        assertEquals('phantomjs-1.9.2-fake-linux-x86_64.tar.bz2', fileName)
    }

    @Test
    void testApplyWrong() {
        thrown.expect(BuildException)
        thrown.expectMessage(StringContains.containsString(
                'Can\'t get https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-null-linux-x86_64.tar.bz2 '))
        apply()
        assertNull(System.properties['phantomjs.binary.path'])
    }

    @Test
    void testApplyFake() {
        task.browserVersion = '1.9.2-fake'
        task.project.copy {
            from this.class.getClassLoader().getResource('phantomjs-1.9.2-fake-linux-x86_64.tar.bz2')
            into task.temporaryDir
        }
        apply()
        final String platform = invoke('getPlatform')
        assertEquals(Paths.get(task.project.buildDir.path, 'browser', 'PHANTOMJS', '1.9.2-fake',
        "phantomjs-1.9.2-fake-$platform", 'bin', 'phantomjs').toString(),
                System.properties['phantomjs.binary.path'])
        System.properties.remove('phantomjs.binary.path')
    }

    @Test
    void testApplyReal() {
        task.browserVersion = '1.9.2'
        apply()
        final String platform = invoke('getPlatform')
        assertEquals(Paths.get(task.project.buildDir.path, 'browser', 'PHANTOMJS', '1.9.2',
                "phantomjs-1.9.2-$platform", 'bin', 'phantomjs').toString(),
                System.properties['phantomjs.binary.path'])
        System.properties.remove('phantomjs.binary.path')
    }
}
