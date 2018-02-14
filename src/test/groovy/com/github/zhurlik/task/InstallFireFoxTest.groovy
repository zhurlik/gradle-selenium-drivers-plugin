package com.github.zhurlik.task

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * A set of unit tests for {@link InstallFireFox}.
 *
 * @author zhurlik@gmail.com
 */
class InstallFireFoxTest {
    private InstallFireFox task
    private Project project

    @Before
    void setUp() throws Exception {
        project = ProjectBuilder.builder().build()

        project.tasks.create('installFireFox', InstallFireFox)

        task = project.tasks['installFireFox']
        assertNotNull(task)
    }

    @Test
    void testUrl() {
        if (task.is64() && task.isLinux()) {
            assertEquals('https://ftp.mozilla.org/pub/firefox/releases/null/linux-x86_64/en-US/firefox-null.tar.bz2', task.url)
        }
    }
}
