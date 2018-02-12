package com.github.zhurlik

import org.gradle.api.Project
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull

/**
 * Unit tests to check {@link SeleniumDriversPlugin}.
 *
 * @author zhurlik@gmail.com
 */
class SeleniumDriversPluginTest {
    @Test
    void testMain() {

        final Project project = ProjectBuilder.builder()
                .withName('test-project')
                .build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        assertNull(project.pluginManager.findPlugin('com.github.zhurlik.seleniumdrivers-wrong-id'))

        final AppliedPlugin plugin = project.pluginManager.findPlugin('com.github.zhurlik.seleniumdrivers')
        assertNotNull(plugin)
        assertEquals('com.github.zhurlik.seleniumdrivers', plugin.id)
        assertEquals('seleniumdrivers', plugin.name)
        assertEquals('com.github.zhurlik', plugin.namespace)
    }
}
