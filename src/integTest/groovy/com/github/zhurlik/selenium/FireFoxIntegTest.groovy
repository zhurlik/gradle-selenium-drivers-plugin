package com.github.zhurlik.selenium

import com.github.zhurlik.Basic
import com.github.zhurlik.task.InstallFireFox
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver

import java.nio.file.Path
import java.nio.file.Paths

/**
 * A real test with running a browser and a webdriver.
 *
 * @author zhurlik@gmail.com
 */
class FireFoxIntegTest extends Basic {
    @Test
    void testMain() {

        final Path projectPath = Paths.get(new File(
                Thread.currentThread().getContextClassLoader().getResource('').path).toString(), 'firefox')
        final Project project = ProjectBuilder.builder()
                .withName('firefox')
                .withProjectDir(projectPath.toFile())
                .build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        project.task(type: InstallFireFox, 'installFireFox', {
            browserVersion '58.0.2'
            driverVersion '0.19.1'
        })

        final Task task = project.tasks['installFireFox']
        executeTask(task)

        final WebDriver webDriver = new FirefoxDriver()
        webDriver.get('https://github.com/zhurlik')
        screenshot(task, webDriver.getScreenshotAs(OutputType.BYTES), 'page1')
        webDriver.findElementByXPath("//a[@href='/zhurlik/gradle-jboss-modules-plugin']").click()
        screenshot(task, webDriver.getScreenshotAs(OutputType.BYTES), 'page2')
        webDriver.quit()
    }
}
