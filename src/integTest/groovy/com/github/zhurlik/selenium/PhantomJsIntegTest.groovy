package com.github.zhurlik.selenium

import com.github.zhurlik.Basic
import com.github.zhurlik.task.InstallPhantomJs
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.openqa.selenium.Capabilities
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.phantomjs.PhantomJSDriverService
import org.openqa.selenium.remote.DesiredCapabilities

import java.nio.file.Path
import java.nio.file.Paths

/**
 * A real test with running a browser and a webdriver.
 *
 * @author zhurlik@gmail.com
 */
class PhantomJsIntegTest extends Basic {
    @Test
    void testMain() {

        final Path projectPath = Paths.get(new File(
                Thread.currentThread().getContextClassLoader().getResource('').path).toString(), 'phantomjs')
        final Project project = ProjectBuilder.builder()
                .withName('phantomjs')
                .withProjectDir(projectPath.toFile())
                .build()

        project.apply plugin: 'com.github.zhurlik.seleniumdrivers'

        project.task(type: InstallPhantomJs, 'installPhantomJs', {
            browserVersion '1.9.8'
        })

        final Task task = project.tasks['installPhantomJs']
        executeTask(task)

        final Capabilities caps = DesiredCapabilities.phantomjs() // or new DesiredCapabilities();
        //service_args=['--ignore-ssl-errors=true', '--ssl-protocol=any']
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, ["--ssl-protocol=any"])
        final WebDriver webDriver = new PhantomJSDriver(caps)

        webDriver.get('https://github.com/zhurlik')
        screenshot(task, webDriver.getScreenshotAs(OutputType.BYTES), 'page1')
        webDriver.findElementByXPath("//a[@href='/zhurlik/gradle-jboss-modules-plugin']").click()
        screenshot(task, webDriver.getScreenshotAs(OutputType.BYTES), 'page2')
        webDriver.close()
    }
}
