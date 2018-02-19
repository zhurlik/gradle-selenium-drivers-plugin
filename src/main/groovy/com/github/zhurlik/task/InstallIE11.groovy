package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers

/**
 * A task for installing Internet Explorer 11 browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallIE11 extends AbstractInstall {

    InstallIE11() {
        browser = Browsers.IE
        driver = Drivers.UNKNOWN
    }

    /**
     * Installing Internet Explorer 11 on Windows via choco.
     */
    @Override
    void onWindows() {
        if (isWindows()) {
            //choco install ie11 --version 0.2 -my
            choco('ie11')
        }
    }

    /**
     * Usual installation for Linux.
     */
    @Override
    void onLinux() {
        throw new UnsupportedOperationException('Internet Explorer 11 doesn\'t on Linux' )
    }
}
