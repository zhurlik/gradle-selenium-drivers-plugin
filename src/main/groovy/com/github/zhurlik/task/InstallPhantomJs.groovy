package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.gradle.api.GradleException

/**
 * A task for installing PhantomJS browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallPhantomJs extends AbstractInstall {

    InstallPhantomJs() {
        browser = Browsers.PHANTOMJS
    }

    /**
     * Installing PhantomJS on Windows via choco.
     */
    @Override
    void onWindows() {
        if (isWindows()) {
            //choco install phantomjs --version 2.1.1 -my
            choco('phantomjs')
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
