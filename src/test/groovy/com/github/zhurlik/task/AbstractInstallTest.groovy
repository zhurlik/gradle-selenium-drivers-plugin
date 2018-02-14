package com.github.zhurlik.task

import org.gradle.api.Project
import org.gradle.internal.impldep.org.codehaus.plexus.interpolation.os.Os
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

/**
 * For testing {@link AbstractInstall}.
 *
 * @author zhurlik@gmail.com
 */
class AbstractInstallTest {
    @Test
    void testIsLinux() {
        final Project project = ProjectBuilder.builder().build()
        project.tasks.create('testTask', TestTask)

        final TestTask task = project.tasks['testTask']
        assertEquals(Os.isFamily(Os.FAMILY_UNIX), task.isLinux())
    }

    private static class TestTask extends AbstractInstall{}
}
