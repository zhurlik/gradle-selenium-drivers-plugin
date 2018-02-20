package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import com.github.zhurlik.domain.Installer
import org.gradle.api.GradleException

/**
 * A task for installing Google Chrome browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallChrome extends AbstractInstall {

    InstallChrome() {
        browser = Browsers.CHROME
        driver = Drivers.UNKNOWN

        linuxInstaller = new  Installer(
                {
                    throw new GradleException('Not implemented yet')
                },
                {}
        )

        windowsInstaller = new Installer(
                {
                    //choco install googlechrome --version 64.0.3282.16700 -my
                    choco('googlechrome')
                },
                {}
        )
    }
}
