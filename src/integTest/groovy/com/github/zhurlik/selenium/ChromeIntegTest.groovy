package com.github.zhurlik.selenium

import com.github.zhurlik.Basic
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

import java.nio.file.Path
import java.nio.file.Paths

/**
 * A real test with running a browser and a webdriver.
 *
 * @author zhurlik@gmail.com
 */
class ChromeIntegTest extends Basic {
    @Test
    void testMain() {

        final Path projectPath = Paths.get(new File(
                Thread.currentThread().getContextClassLoader().getResource('').path).toString(), 'chrome')
        final Project project = ProjectBuilder.builder()
                .withName('chrome')
                .withProjectDir(projectPath.toFile())
                .build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        project.task(type: InstallChrome, 'installChrome', {
            browserVersion '64.0.3282.16800'
            driverVersion '2.35'
        })

        final Task task = project.tasks['installChrome']
        executeTask(task)

        final WebDriver webDriver = new ChromeDriver()
        webDriver.get('https://github.com/zhurlik')
        webDriver.findElementByXPath("//a[@href='/zhurlik/gradle-swagger-plugin']").click()
        webDriver.close()
    }
}
