package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

/**
 * A task for installing FireFox browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallFireFoxOnMac extends MacTask {
    @Input
    String browserVersion

    @Input
    String driverVersion

    private Browsers browser = Browsers.FIREFOX
    private Drivers driver = Drivers.GECKO

    @TaskAction
    void apply() {
        installBrowser()
        installDriver()
    }

    /**
     * Downloads and installs a webdriver.
     */
    private void installDriver() {
        // webdriver on Mac OS X
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
            from project.tarTree(project.resources.gzip(archive))
            into target
        }

        System.properties['webdriver.gecko.driver'] = Paths.get(target, 'geckodriver').toString()

        logger.quiet("$driver has been installed")
        logger.debug("Installed to: $target")
    }

    /**
     * Downloads and installs FireFox browser.
     */
    private void installBrowser() {
        ant.get(src: getBrowserUrl(),
                dest: temporaryDir.path,
                skipexisting: true,
                verbose: true
        )

        // browser
        final String filename = Paths.get(new URI(getBrowserUrl()).getPath()).getFileName().toString()
                .replace(' ', '%20')
        final String archive = "${temporaryDir.path}/$filename"
        logger.debug("Downloaded: $archive")
        final String target = "${project.buildDir}/browser/$browser/$browserVersion"

        extractDmg(archive, '/Volumes/Firefox/Firefox.app', target)

        logger.quiet("$browser has been installed")
        logger.debug("Installed to: $target")

        System.properties['webdriver.firefox.bin'] = "$target/Contents/MacOS/firefox".toString()
    }

    /**
     * Return url for downloading a webdriver.
     *  For example:
     *      https://github.com/mozilla/geckodriver/releases/download/v0.19.1/geckodriver-v0.19.1-macos.tar.gz
     *
     * @return
     */
    private String getDriverUrl() {
        return "https://github.com/mozilla/geckodriver/releases/download/" +
                "v$driverVersion/geckodriver-v$driverVersion-macos.tar.gz"
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://ftp.mozilla.org/pub/firefox/releases/58.0.2/mac/en-US/Firefox%2058.0.2.dmg
     *
     * @return url
     */
    private String getBrowserUrl() {
        return "https://ftp.mozilla.org/pub/firefox/releases/$browserVersion/mac/en-US/Firefox%20${browserVersion}.dmg"
    }
}
