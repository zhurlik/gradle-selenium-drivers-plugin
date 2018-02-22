package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import com.github.zhurlik.domain.Installer

/**
 * A task for installing Internet Explorer 11 browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallIE11 extends AbstractInstall {

    InstallIE11() {
        browser = Browsers.IE
        driver = Drivers.IE

        linuxInstaller = new  Installer(
                {
                    throw new UnsupportedOperationException('Internet Explorer 11 doesn\'t on Linux' )
                },
                {}
        )

        windowsInstaller = new Installer(
                {
                    //choco install ie11 --version 0.2 -my
                    choco('ie11', browserVersion)
                },
                {
                    //choco install selenium-ie-driver --version 3.8.0 -my
                    choco('selenium-ie-driver', driverVersion)
                }
        )
    }
}
