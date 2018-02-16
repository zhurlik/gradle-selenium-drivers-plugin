package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.gradle.api.GradleException

/**
 * A task for installing Google Chrome browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallChrome extends AbstractInstall {

    InstallChrome() {
        browser = Browsers.CHROME
    }

    /**
     * Installing Google Chrome on Windows via choco.
     */
    @Override
    void onWindows() {
        if (isWindows()) {
            //choco install googlechrome --version 64.0.3282.16700 -my
            choco('googlechrome')
        }
    }

    /**
     * Usual installation for Linux.
     */
    @Override
    void onLinux() {
        throw new GradleException('Not implemented yet')
    }
}
