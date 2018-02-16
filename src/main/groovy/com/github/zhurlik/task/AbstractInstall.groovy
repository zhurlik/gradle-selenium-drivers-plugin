package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.internal.os.OperatingSystem

/**
 * An abstract class for extracting common methods.
 *
 * @author zhurlik@gmail.com
 */
abstract class AbstractInstall extends DefaultTask {

    @Input
    String browserVersion

    @Input
    Browsers browser

    /**
     * Optional to be able to specify own actions for installing.
     */
    Closure linuxInstaller
    Closure windowsInstaller

    /**
     * Checks that current OS is Linux.
     *
     * @return true when OS is Linux
     */
    protected boolean isLinux() {
        return OperatingSystem.current().isLinux()
    }

    /**
     * Checks that current OS is Windows.
     *
     * @return true when OS is Windows
     */
    protected boolean isWindows() {
        return OperatingSystem.current().isWindows()
    }

    /**
     * Prints browser name and version.
     */
    protected void info() {
        logger.quiet("Browser: ${browser.toString().toLowerCase()} $browserVersion")
    }

    /**
     *  Is 64 or 32 bit system.
     *
     * @return true when 64 bit system
     */
    protected boolean is64() {
        return OperatingSystem.current().getNativePrefix().contains('64')
    }
}
