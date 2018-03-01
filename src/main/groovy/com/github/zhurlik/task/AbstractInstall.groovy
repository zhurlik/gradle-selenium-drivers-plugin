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
    private final static OperatingSystem OS =  OperatingSystem.current()

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

    @Input
    Installer macOsInstaller

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
        return OS.isLinux()
    }

    /**
     * Checks that current OS is Mac OS X.
     *
     * @return true when OS is Mac OS X
     */
    protected boolean isMacOsX() {
        return OS.isMacOsX()
    }

    /**
     * Checks that current OS is Windows.
     *
     * @return true when OS is Windows
     */
    protected boolean isWindows() {
        return OS.isWindows()
    }

    /**
     * Prints browser name and version.
     */
    protected void info() {
        logger.quiet('Installing...')
        logger.quiet("Browser: ${browser.toString().toLowerCase()} $browserVersion")
        logger.quiet("WebDriver: ${driver.toString().toLowerCase()} $driverVersion")
    }

    /**
     *  Is 64 or 32 bit system.
     *
     * @return true when 64 bit system
     */
    protected boolean is64() {
        return isMacOsX() || OS.getNativePrefix().contains('64')
    }

    /**
     * Common method for installing a package on Windows.
     * See https://chocolatey.org/docs/commandsinstall.
     *
     * @param packageName name
     * @param packageVersion version
     */
    protected void choco(final String packageName, final String packageVersion) {
        try {
            new ByteArrayOutputStream().withCloseable { out ->
                ExecResult res = project.exec {
                    commandLine 'cmd', '/c', "choco install $packageName --version $packageVersion -my"
                    standardOutput = out
                    ignoreExitValue = true
                }

                final String log = out.toString()
                if (res.exitValue == 0) { // success
                    logger.quiet("$packageName has been installed")
                    logger.debug("Intsallation log: $log")
                } else { // failure
                    logger.error("A problem during installation: $log")
                    res.rethrowFailure()
                }
            }
        } catch (Exception ex) {
            throw new GradleException("$packageName is not installed:", ex)
        }
    }

    /**
     *  A wrapper to invoke Get-ToolsLocation function via powershell.
     *  See https://chocolatey.org/docs/helpers-get-tools-location
     *
     * @return result of Get-ToolsLoacation, default is c:\tools
     */
    protected String getToolsLocation() {
        new ByteArrayOutputStream().withCloseable { out ->
            try {
                ExecResult res = project.exec {
                    commandLine 'powershell',
                            'Set-ExecutionPolicy -ExecutionPolicy Unrestricted -Confirm:$False;',
                            "Import-Module ${System.env['ChocolateyInstall']}\\helpers\\chocolateyInstaller.psm1 -Force;",
                            'Get-ToolsLocation'
                    standardOutput = out
                    ignoreExitValue = true
                }

                if (res.exitValue == 0) { // success
                    final String value = out.toString().trim()
                    logger.debug("Get-ToolsLocation: ${value}")
                    return value
                } else { // failure
                    logger.error("There is a problem with Get-ToolsLocation: ${out.toString()}")
                }
            } catch (Exception ex) {
                logger.error('There is a problem with Get-ToolsLocation:', ex)
            }

            // default
            return 'c:\\tools'
        }
    }

    /**
     * Extracts a dmg file using mounting/unmounting into a special folder.
     *
     * @param archive a dmg file
     * @param innerPath what should be copied
     * @param target where will be extracted
     */
    protected void extractDmg(final String archive, final String innerPath, final String target){
        // extracting dmg file
        new ByteArrayOutputStream().withCloseable { out ->
            // mount
            ExecResult res = project.exec {
                commandLine 'hdiutil', 'attach', archive
                standardOutput = out
                ignoreExitValue = true
            }

            String log = out.toString()
            if (res.exitValue == 0) { // success
                logger.quiet("DMG file has been mounted")
                logger.debug("Log: $log")
            } else {
                logger.error("A problem with mounting dmg file: $log")
                res.rethrowFailure()
            }

            // copy files
            project.copy {
                from innerPath
                into target
            }
            logger.quiet("DMG file has been extracted")

            // unmount
            res = project.exec {
                commandLine 'hdiutil', 'detach', log.readLines().last().find('/dev/disk1s[0-9]')
                standardOutput = out
                ignoreExitValue = true
            }

            log = out.toString()
            if (res.exitValue == 0) { // success
                logger.quiet("DMG file has been unmounted")
                logger.debug("Log: $log")
            } else {
                logger.error("A problem with unmounting dmg file: $log")
                res.rethrowFailure()
            }
        }
    }

    protected void install() {
        info()
        onWindows()
        onLinux()
        onMacOsX()
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

    /**
     * Executes {@link Installer} on Mac OS X.
     */
    private void onMacOsX() {
        if (isMacOsX()) {
            macOsInstaller.install()
        }
    }
}
