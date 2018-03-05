package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

/**
 * A task for installing FireFox browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallFireFoxOnWindows extends WindowsTask {
    @Input
    String browserVersion

    @Input
    String driverVersion

    private Browsers browser = Browsers.FIREFOX
    private Drivers driver = Drivers.GECKO

    @TaskAction
    void apply() {
        installBrowser()
        installDriver()
    }

    /**
     * Downloads and installs a webdriver.
     */
    private void installDriver() {
        //choco install selenium-gecko-driver --version 0.19.1.20171103 -my
        choco('selenium-gecko-driver', driverVersion)
        System.properties['webdriver.gecko.driver'] = Paths.get(getToolsLocation(), 'selenium',
                'geckodriver.exe').toString()
    }

    /**
     * Downloads and installs FireFox browser.
     */
    private void installBrowser() {
        //choco install firefox --version 58.0.2 -my
        choco('firefox', browserVersion)
    }
}
