package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.gradle.api.GradleException

/**
 * A task for installing Opera browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallOpera extends AbstractInstall {

    InstallOpera() {
        browser = Browsers.OPERA
    }

    /**
     * Installing Opera on Windows via choco.
     */
    @Override
    void onWindows() {
        if (isWindows()) {
            //choco install opera --version 51.0.2830.34 -my
            choco('opera')
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
