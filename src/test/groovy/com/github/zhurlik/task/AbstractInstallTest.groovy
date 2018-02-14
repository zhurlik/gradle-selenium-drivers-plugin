package com.github.zhurlik.task

import org.gradle.api.Project
import org.gradle.internal.impldep.org.codehaus.plexus.interpolation.os.Os
import org.gradle.internal.os.OperatingSystem
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

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
    void testArch() {
        assertEquals(!OperatingSystem.current().nativePrefix.contains('32'), task.is64())
    }

    @Test
    void testInfo() {
        task.info()
    }

    private static class TestTask extends AbstractInstall{}
}
