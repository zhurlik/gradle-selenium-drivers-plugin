package com.github.zhurlik.task

import org.apache.tools.ant.BuildException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.core.StringContains
import org.junit.Before
import org.junit.Test

import java.nio.file.Paths

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull

/**
 * Unit tests for {@link InstallPhantomJsOnMac} methods that will be invoked via {@link org.gradle.internal.reflect.JavaReflectionUtil}.
 *
 * @author zhurlik@gmail.com
 */
class InstallPhantomJsOnMacTest extends BaseTest {

    @Before
    void setUp() throws Exception {
        final Project project = ProjectBuilder.builder().build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        task = project.tasks.create('testInstallPhantomJs', InstallPhantomJsOnMac)
        assertNotNull(task)
    }

    @Test
    void testGetUrlOnGoogleCode() {
        String res = invoke('getUrlOnGoogleCode')
        assertEquals('https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-null-macosx.zip',
                res)

        task.browserVersion = '111'
        res = invoke('getUrlOnGoogleCode')
        assertEquals('https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-111-macosx.zip',
                res)
    }

    @Test
    void testGetUrlOnBitbucket() {
        String res = invoke('getUrlOnBitbucket')
        assertEquals('https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-null-macosx.zip', res)

        task.browserVersion = '111'
        res = invoke('getUrlOnBitbucket')
        assertEquals('https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-111-macosx.zip', res)
    }

    @Test
    void testDownloadInstallerWrongUrl() {
        thrown.expect(BuildException)
        thrown.expectMessage(StringContains.containsString(
                'Can\'t get https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-null-macosx.zip '))
        invoke('downloadInstaller')
    }

    @Test
    void testDownloadInstallerSkip() {
        task.browserVersion = '1.9.2-fake'
        task.project.copy {
            from this.class.getClassLoader().getResource('phantomjs-1.9.2-fake-macosx.zip')
            into task.temporaryDir
        }
        final String fileName = invoke('downloadInstaller')
        assertEquals('phantomjs-1.9.2-fake-macosx.zip', fileName)
    }

    @Test
    void testApplyWrong() {
        thrown.expect(BuildException)
        thrown.expectMessage(StringContains.containsString(
                'Can\'t get https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-null-macosx.zip '))
        apply()
        assertNull(System.properties['phantomjs.binary.path'])
    }

    @Test
    void testApplyFake() {
        task.browserVersion = '1.9.2-fake'
        task.project.copy {
            from this.class.getClassLoader().getResource('phantomjs-1.9.2-fake-macosx.zip')
            into task.temporaryDir
        }
        apply()
        assertEquals(Paths.get(task.project.buildDir.path, 'browser', 'PHANTOMJS', '1.9.2-fake',
                "phantomjs-1.9.2-fake-macosx", 'bin', 'phantomjs').toString(),
                System.properties['phantomjs.binary.path'])
        System.properties.remove('phantomjs.binary.path')
    }

    @Test
    void testApplyReal() {
        task.browserVersion = '1.9.2'
        apply()
        assertEquals(Paths.get(task.project.buildDir.path, 'browser', 'PHANTOMJS', '1.9.2',
                "phantomjs-1.9.2-macosx", 'bin', 'phantomjs').toString(),
                System.properties['phantomjs.binary.path'])
        System.properties.remove('phantomjs.binary.path')
    }
}
