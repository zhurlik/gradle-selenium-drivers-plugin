package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

/**
 * A task for installing Opera browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallOperaOnMac extends MacTask {
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

        System.properties['webdriver.opera.driver'] = Paths.get(target,'operadriver_mac64', 'operadriver').toString()

        logger.quiet("WebDriver $driver has been installed")
        logger.debug("Installed to: $target")
    }

    /**
     * Downloads and installs Opera browser.
     */
    private void installBrowser() {
        ant.get(src: getBrowserUrl(),
                dest: temporaryDir.path,
                skipexisting: true,
                verbose: true
        )

        // browser
        final String filename = Paths.get(new URI(getBrowserUrl()).getPath()).getFileName().toString()
        final String archive = "${temporaryDir.path}/$filename"
        logger.debug("Downloaded: $archive")
        final String target = "${project.buildDir}/browser/$browser/$browserVersion"

        extractDmg(archive, '/Volumes/Opera/Opera.app', target)

        logger.quiet("$browser has been installed")
        logger.debug("Installed to: $target")

        System.properties['webdriver.opera.bin'] = "$target/Contents/MacOS/opera".toString()
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      ftp://ftp.opera.com/pub/opera/desktop/51.0.2830.40/mac/Opera_51.0.2830.40_Setup.dmg
     *
     * @return url
     */
    private String getBrowserUrl() {
        return "ftp://ftp.opera.com/pub/opera/desktop/$browserVersion/mac/Opera_${browserVersion}_Setup.dmg"
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://github.com/operasoftware/operachromiumdriver/releases/download/v.2.33/operadriver_mac64.zip
     *
     * @return url
     */
    private String getDriverUrl() {
        return "https://github.com/operasoftware/operachromiumdriver/releases/download/v.${driverVersion}/operadriver_mac64.zip"
    }
}
