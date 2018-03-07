package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import org.apache.tools.ant.BuildException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

/**
 * A task for installing Opera browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallOperaOnLinux extends LinuxTask {

    @Input
    String browserVersion

    @Input
    String driverVersion

    private Browsers browser = Browsers.OPERA
    private Drivers driver = Drivers.OPERA

    @TaskAction
    void apply() {
        installBrowser()
        installDriver()
    }

    /**
     * Downloads and installs a webdriver.
     */
    private void installDriver() {
        // webdriver on Linux
        ant.get(src: getDriverUrl(),
                dest: temporaryDir.path,
                skipexisting: true,
                verbose: true
        )

        final String filename = Paths.get(new URI(getDriverUrl()).getPath()).getFileName().toString()
        final String archive = "${temporaryDir.path}/$filename"
        logger.debug("Downloaded: $archive")
        final String target = "${project.buildDir}/driver/$driver/$driverVersion"
        project.copy {
            from project.zipTree(archive)
            into target
        }

        System.properties['webdriver.opera.driver'] = Paths.get(target, 'operadriver_linux64', 'operadriver').toString()

        logger.quiet("WebDriver $driver has been installed")
        logger.debug("Installed to: $target")
    }

    /**
     * Downloads and installs Opera browser.
     */
    private void installBrowser() {
        /**
         * Usual installation for Linux.
         * Downloading from: ftp://ftp.opera.com/pub/opera/desktop/
         */
        ant.get(src: getBrowserUrl(),
                dest: temporaryDir.path,
                skipexisting: true,
                verbose: true,
                ignoreerrors: true
        )

        final String filename = Paths.get(new URI(getBrowserUrl()).getPath()).getFileName().toString()
        final String archive = "${temporaryDir.path}/$filename"

        if (!project.file(archive).exists()) {
            throw new BuildException("Can't get ${getBrowserUrl()}")
        }

        logger.debug("Downloaded: $archive")
        final String target = "${project.buildDir}/browser/$browser/$browserVersion"

        // TODO: when deb or rpm?
        // extracting deb file
        extractDeb(archive, 'usr/lib/x86_64-linux-gnu/opera', target)
        logger.quiet("$browser has been installed")
        logger.debug("Installed to: $target")

        System.properties['webdriver.opera.bin'] = "$target/opera".toString()
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      ftp://ftp.opera.com/pub/opera/desktop/51.0.2830.40/linux/opera-stable_51.0.2830.40_amd64.deb
     *      ftp://ftp.opera.com/pub/opera/desktop/51.0.2830.40/linux/opera-stable_51.0.2830.40_amd64.rpm
     *
     * @return url
     */
    private String getBrowserUrl() {
        // TODO: when deb or rpm?
        return "ftp://ftp.opera.com/pub/opera/desktop/$browserVersion/linux/opera-stable_${browserVersion}_amd64.deb"
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://github.com/operasoftware/operachromiumdriver/releases/download/v.2.33/operadriver_linux64.zip
     *
     * @return url
     */
    private String getDriverUrl() {
        return "https://github.com/operasoftware/operachromiumdriver/releases/download/v.${driverVersion}/operadriver_linux64.zip"
    }
}
