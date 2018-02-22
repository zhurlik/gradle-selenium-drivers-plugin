package com.github.zhurlik.selenium

import com.github.zhurlik.Basic
import com.github.zhurlik.task.InstallOpera
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.internal.os.OperatingSystem
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.opera.OperaDriver
import org.openqa.selenium.opera.OperaOptions

import java.nio.file.Path
import java.nio.file.Paths

/**
 * A real test with running a browser and a webdriver.
 *
 * @author zhurlik@gmail.com
 */
class OperaIntegTest extends Basic {
    @Test
    void testMain() {

        final Path projectPath = Paths.get(new File(
                Thread.currentThread().getContextClassLoader().getResource('').path).toString(), 'opera')
        final Project project = ProjectBuilder.builder()
                .withName('opera')
                .withProjectDir(projectPath.toFile())
                .build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        project.task(type: InstallOpera, 'installOpera', {
            browserVersion '51.0.2830.34'
            driverVersion '2.33'
        })

        final Task task = project.tasks['installOpera']
        executeTask(task)

        final OperaOptions operaOptions = new OperaOptions()

        if (OperatingSystem.current().isWindows()) {
            operaOptions.setBinary('C:\\Program Files\\Opera\\51.0.2830.34_0\\opera.exe')
        }
        final WebDriver webDriver = new OperaDriver(operaOptions)
        webDriver.get('https://github.com/zhurlik')
        screenshot(task, webDriver.getScreenshotAs(OutputType.BYTES), 'page1')
        webDriver.findElementByXPath("//a[@href='/zhurlik/gradle-jboss-modules-plugin']").click()
        screenshot(task, webDriver.getScreenshotAs(OutputType.BYTES), 'page2')
        webDriver.close()
    }
}
