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
class InstallFireFoxOnLinux extends LinuxTask {
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
        final String archive = "${temporaryDir.path}/$filename"
        logger.debug("Downloaded: $archive")
        final String target = "${project.buildDir}/browser/$browser/$browserVersion"
        project.copy {
            from project.tarTree(project.resources.bzip2(archive))
            into target
        }
        logger.quiet("$browser has been installed")
        logger.debug("Installed to: $target")

        System.properties['webdriver.firefox.bin'] = Paths.get(target, 'firefox', 'firefox').toString()
    }

    /**
     * Return url for downloading a webdriver.
     *  For example:
     *      https://github.com/mozilla/geckodriver/releases/download/v0.19.1/geckodriver-v0.19.1-linux64.tar.gz
     *      https://github.com/mozilla/geckodriver/releases/download/v0.19.1/geckodriver-v0.19.1-linux32.tar.gz
     *
     * @return
     */
    private String getDriverUrl() {
        final String platform = "${is64() ? 'linux64' : 'linux32'}"
        return "https://github.com/mozilla/geckodriver/releases/download/" +
                "v$driverVersion/geckodriver-v$driverVersion-${platform}.tar.gz"
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://ftp.mozilla.org/pub/firefox/releases/58.0.2/linux-x86_64/en-US/firefox-58.0.2.tar.bz2
     *      https://ftp.mozilla.org/pub/firefox/releases/58.0.2/linux-i686/en-US/firefox-58.0.2.tar.bz2
     *
     * @return url
     */
    private String getBrowserUrl() {
        final String platform = "${is64() ? 'linux-x86_64' : 'linux-i686'}"
        return "https://ftp.mozilla.org/pub/firefox/releases/$browserVersion/$platform/en-US/firefox-${browserVersion}.tar.bz2"
    }
}
