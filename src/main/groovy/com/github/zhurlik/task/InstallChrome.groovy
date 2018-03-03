package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import com.github.zhurlik.domain.Installer
import org.gradle.api.GradleException

import java.nio.file.Paths

/**
 * A task for installing Google Chrome browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallChrome extends AbstractInstall {

    InstallChrome() {
        browser = Browsers.CHROME
        driver = Drivers.CHROME

        def setupChromeDriver = {
            // webdriver on Linux and Mac OS X
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

            System.properties['webdriver.chrome.driver'] = Paths.get(target,
                    "chromedriver_${isMacOsX() ? 'mac' : 'linux'}64",
                    'chromedriver').toString()

            logger.quiet("$driver has been installed")
            logger.debug("Installed to: $target")
        }

        linuxInstaller = new  Installer(
                {
                    throw new GradleException('Not implemented yet')
                },
                setupChromeDriver
        )

        windowsInstaller = new Installer(
                {
                    //choco install googlechrome --version 64.0.3282.16700 -my
                    choco('googlechrome', browserVersion)
                },
                {
                    //choco install selenium-chrome-driver --version 2.35 -my
                    choco('selenium-chrome-driver', driverVersion)
                    System.properties['webdriver.chrome.driver'] = Paths.get(getToolsLocation(), 'selenium',
                            'chromedriver.exe').toString()

                }
        )

        macOsInstaller = new Installer(
                {

                },
                setupChromeDriver
        )
    }

    /**
     * Returns url for downloading a webdriver.
     *  For example:
     *      https://chromedriver.storage.googleapis.com/2.36/chromedriver_linux64.zip
     *      https://chromedriver.storage.googleapis.com/2.36/chromedriver_mac64.zip
     *
     * @return
     */
    String getDriverUrl() {
        return "https://chromedriver.storage.googleapis.com/$driverVersion/chromedriver_${isMacOsX() ? 'macos' : 'linux'}64.zip"
    }
}
