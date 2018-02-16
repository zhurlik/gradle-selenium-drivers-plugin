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
            thrown.expect(GradleException)
            thrown.expectMessage('Not implemented yet')
            task.browserVersion = 'bad'

            task.apply()
        }
    }
}