package com.github.zhurlik.domain

/**
 * This class contains 2 installers for Browser and WebDriver.
 *
 * @author zhurlik@gmail.com
 */
class Installer {
    protected Closure browserInstaller
    protected Closure driverInstaller

    /**
     * To be able to specify custom installers.
     *
     * @param browserInstaller to setup a browser
     * @param driverInstaller to setup a webdriver
     */
    Installer(final Closure browserInstaller, final Closure driverInstaller) {
        this.browserInstaller = browserInstaller
        this.driverInstaller = driverInstaller
    }

    /**
     * Calls both installers for Browser and WebDriver.
     */
    void install() {
        Optional.ofNullable(browserInstaller).orElse({})()
        Optional.ofNullable(driverInstaller).orElse({})()
    }
}
