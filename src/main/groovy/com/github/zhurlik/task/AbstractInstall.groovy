package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import com.github.zhurlik.domain.Installer
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.ExecResult

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

    @Input
    Drivers driver

    @Input
    String driverVersion

    @Input
    Installer linuxInstaller

    @Input
    Installer windowsInstaller

    @TaskAction
    void apply() {
        install()
    }

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
        logger.quiet('Installing...')
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

    /**
     * Common method for installing a package on Windows.
     * See https://chocolatey.org/docs/commandsinstall.
     *
     * @param packageName
     */
    protected void choco(final String packageName) {
        try {
            new ByteArrayOutputStream().withCloseable { out ->
                ExecResult res = project.exec {
                    commandLine 'cmd', '/c', "choco install $packageName --version $browserVersion -my"
                    standardOutput = out
                    ignoreExitValue = true
                }

                final String log = out.toString()
                if (res.exitValue == 0) { // success
                    logger.quiet("$browser has been installed")
                    logger.debug("Intsallation log: $log")
                } else { // failure
                    logger.error("A problem during installation: $log")
                    res.rethrowFailure()
                }
            }
        } catch (Exception ex) {
            throw new GradleException("$browser is not installed:", ex)
        }
    }

    protected void install() {
        info()
        onWindows()
        onLinux()
    }

    /**
     * Executes {@link Installer} on Windows.
     */
    private void onWindows() {
        if (isWindows()) {
            windowsInstaller.install()
        }
    }

    /**
     * Executes {@link Installer} on Linux.
     */
    private void onLinux() {
        if (isLinux()) {
            linuxInstaller.install()
        }
    }
}
