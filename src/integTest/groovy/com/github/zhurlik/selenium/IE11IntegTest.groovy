package com.github.zhurlik.selenium

import com.github.zhurlik.Basic
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.ie.InternetExplorerDriver

import java.nio.file.Path
import java.nio.file.Paths

/**
 * A real test with running a browser and a webdriver.
 *
 * @author zhurlik@gmail.com
 */
class IE11IntegTest extends Basic {
    @Test
    void testMain() {

        final Path projectPath = Paths.get(new File(
                Thread.currentThread().getContextClassLoader().getResource('').path).toString(), 'ie11')
        final Project project = ProjectBuilder.builder()
                .withName('ie11')
                .withProjectDir(projectPath.toFile())
                .build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        project.tasks['installIE11'].browserVersion = '0.2'
        project.tasks['installIE11'].driverVersion = '3.8.0'

        final Task task = project.tasks['installIE11']
        executeTask(task)

        final WebDriver webDriver = new InternetExplorerDriver()

        webDriver.get('https://github.com/zhurlik')
        screenshot(task, webDriver.getScreenshotAs(OutputType.BYTES), 'page1')
        webDriver.findElementByXPath("//a[@href='/zhurlik/gradle-jboss-modules-plugin']").click()
        screenshot(task, webDriver.getScreenshotAs(OutputType.BYTES), 'page2')
        webDriver.quit()
    }
}
