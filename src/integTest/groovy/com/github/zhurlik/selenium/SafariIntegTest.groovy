package com.github.zhurlik.selenium

import com.github.zhurlik.Basic
import com.github.zhurlik.task.InstallSafari
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.safari.SafariDriver

import java.nio.file.Path
import java.nio.file.Paths

/**
 * A real test with running a browser and a webdriver.
 *
 * @author zhurlik@gmail.com
 */
class SafariIntegTest extends Basic {
    @Test
    void testMain() {

        final Path projectPath = Paths.get(new File(
                Thread.currentThread().getContextClassLoader().getResource('').path).toString(), 'safari')
        final Project project = ProjectBuilder.builder()
                .withName('safari')
                .withProjectDir(projectPath.toFile())
                .build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        project.task(type: InstallSafari, 'installSafari', {
        })

        final Task task = project.tasks['installSafari']
        executeTask(task)

        final WebDriver webDriver = new SafariDriver()

        webDriver.get('https://github.com/zhurlik')
        screenshot(task, webDriver.getScreenshotAs(OutputType.BYTES), 'page1')
        webDriver.findElementByXPath("//a[@href='/zhurlik/gradle-jboss-modules-plugin']").click()
        screenshot(task, webDriver.getScreenshotAs(OutputType.BYTES), 'page2')
        webDriver.close()
    }
}
