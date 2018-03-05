package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import com.github.zhurlik.domain.Installer

/**
 * A task for installing Safari browser.
 *
 * @author zhurlik@gmail.com
 */
@Deprecated
class InstallSafari extends AbstractInstall {

    InstallSafari() {
        browser = Browsers.SAFARI
        browserVersion = 'not required'

        driver = Drivers.SAFARI
        driverVersion = 'not required'

        linuxInstaller = new  Installer(
                {
                    throw new UnsupportedOperationException('Safari doesn\'t work on Linux' )
                },
                {}
        )

        windowsInstaller = new Installer(
                {
                    throw new UnsupportedOperationException('Safari doesn\'t work on Windows' )
                },
                {}
        )

        macOsInstaller = new Installer(
                {
                    // doesn't require any actions
                },
                {
                    // sudo /usr/bin/safaridriver --enable
                }
        )
    }
}
