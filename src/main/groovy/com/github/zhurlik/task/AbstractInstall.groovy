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
     *
     * @return
     */
    protected boolean isLinux() {
        return OperatingSystem.current().isLinux()
    }

    /**
     *
     * @return
     */
    protected boolean isWindows() {
        return OperatingSystem.current().isWindows()
    }

    /**
     *
     */
    protected void info() {
        logger.quiet("Browser: $browser")
        logger.quiet("Version: $browserVersion")
    }

    /**
     *
     * @return
     */
    protected boolean is64() {
        return OperatingSystem.current().getNativePrefix().contains('64')
    }
}
