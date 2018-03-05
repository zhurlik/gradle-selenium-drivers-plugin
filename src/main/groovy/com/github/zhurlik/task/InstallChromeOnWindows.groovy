package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

/**
 * A task for installing Google Chrome browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallChromeOnWindows extends WindowsTask {
    @Input
    String browserVersion

    @Input
    String driverVersion

    private Browsers browser = Browsers.CHROME
    private Drivers driver = Drivers.CHROME

    @TaskAction
    void apply() {
        installBrowser()
        installDriver()
    }

    /**
     * Downloads and installs a webdriver.
     */
    private void installDriver() {
        //choco install selenium-chrome-driver --version 2.35 -my
        choco('selenium-chrome-driver', driverVersion)
        System.properties['webdriver.chrome.driver'] = Paths.get(getToolsLocation(), 'selenium',
                'chromedriver.exe').toString()

    }

    /**
     * Downloads and installs Chrome browser.
     */
    private void installBrowser() {
        //choco install googlechrome --version 64.0.3282.16700 -my
        choco('googlechrome', browserVersion)
    }
}
