package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Installer
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.internal.impldep.org.codehaus.plexus.interpolation.os.Os
import org.gradle.internal.os.OperatingSystem
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * For testing {@link AbstractInstall}.
 *
 * @author zhurlik@gmail.com
 */
class AbstractInstallTest {
    private TestTask task

    @Before
    void setUp() throws Exception {
        final Project project = ProjectBuilder.builder().build()
        project.tasks.create('testTask', TestTask)

        task = project.tasks['testTask']
        assertNotNull(task)
    }

    @Test
    void testIsLinux() {
        assertEquals(Os.isFamily(Os.FAMILY_UNIX), task.isLinux())
    }

    @Test
    void testIsWindows() {
        assertEquals(Os.isFamily(Os.FAMILY_WINDOWS), task.isWindows())
    }

    @Test
    void testIsMacOsX() {
        assertEquals(Os.isFamily(Os.FAMILY_MAC), task.isMacOsX())
    }

    @Test
    void testArch() {
        assertEquals(!OperatingSystem.current().nativePrefix.contains('32'), task.is64())
    }

    @Test
    void testInfo() {
        task.info()
    }

    @Test
    void testInstall() {
        task.install()
    }

    @Test(expected = GradleException)
    void testChoco() {
        task.browser = Browsers.CHROME
        task.choco('test-package', '1.0')
    }

    @Test
    void testGetToolsLocation() {
        assertEquals('c:\\tools', task.getToolsLocation())
    }

    private static class TestTask extends AbstractInstall {
        TestTask() {
            linuxInstaller = new Installer({}, {})
            windowsInstaller = new Installer({}, {})
        }
    }
}
