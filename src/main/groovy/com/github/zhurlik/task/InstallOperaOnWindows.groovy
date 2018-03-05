package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

/**
 * A task for installing Opera browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallOperaOnWindows extends WindowsTask {
    @Input
    String browserVersion

    @Input
    String driverVersion

    private Browsers browser = Browsers.OPERA
    private Drivers driver = Drivers.OPERA

    @TaskAction
    void apply() {
        installBrowser()
        installDriver()
    }

    /**
     * Downloads and installs a webdriver.
     */
    private void installDriver() {
        //choco install selenium-opera-driver --version 2.33 -my
        choco('selenium-opera-driver', driverVersion)
        System.properties['webdriver.opera.driver'] = Paths.get(getToolsLocation(), 'selenium',
                'operadriver.exe').toString()
    }

    /**
     * Downloads and installs Opera browser.
     */
    private void installBrowser() {
        //choco install opera --version 51.0.2830.34 -my
        choco('opera', browserVersion)
        // TODO: check for other versions
        System.properties['webdriver.opera.bin'] = "C:\\Program Files\\Opera\\${browserVersion}_0\\opera.exe".toString()
    }
}
