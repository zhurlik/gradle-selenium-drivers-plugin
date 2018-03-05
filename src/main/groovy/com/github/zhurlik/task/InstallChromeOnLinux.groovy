package com.github.zhurlik.task

import com.github.zhurlik.domain.Drivers
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

/**
 * A task for installing Google Chrome browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallChromeOnLinux extends LinuxTask {
    @Input
    String browserVersion

    @Input
    String driverVersion

    private Drivers driver = Drivers.CHROME

    @TaskAction
    void apply() {
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

        System.properties['webdriver.chrome.driver'] = Paths.get(target, 'chromedriver').toString()

        logger.quiet("$driver has been installed")
        logger.debug("Installed to: $target")
    }

    /**
     * Downloads and installs Chrome browser.
     */
    private void installBrowser() {
        // not yet
    }

    /**
     * Returns url for downloading a webdriver.
     *  For example:
     *      https://chromedriver.storage.googleapis.com/2.36/chromedriver_linux64.zip
     *
     * @return
     */
    private String getDriverUrl() {
        return "https://chromedriver.storage.googleapis.com/$driverVersion/chromedriver_linux64.zip"
    }
}
