package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import com.github.zhurlik.domain.Installer
import org.gradle.process.ExecResult

import java.nio.file.Paths

/**
 * A task for installing FireFox browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallFireFox extends AbstractInstall {

    InstallFireFox() {
        browser = Browsers.FIREFOX
        driver = Drivers.GECKO

        def setupGecko = {
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
                from project.tarTree(project.resources.gzip(archive))
                into target
            }

            System.properties['webdriver.gecko.driver'] = Paths.get(target, 'geckodriver').toString()

            logger.quiet("$driver has been installed")
            logger.debug("Installed to: $target")
        }
        linuxInstaller = new Installer(
                {
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
                },
                setupGecko
        )

        windowsInstaller = new Installer(
                {
                    //choco install firefox --version 58.0.2 -my
                    choco('firefox', browserVersion)
                },
                {
                    //choco install selenium-gecko-driver --version 0.19.1.20171103 -my
                    choco('selenium-gecko-driver', driverVersion)
                    System.properties['webdriver.gecko.driver'] = Paths.get(getToolsLocation(), 'selenium',
                            'geckodriver.exe').toString()
                }
        )

        macOsInstaller = new Installer(
                {
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

                    // extracting dmg file
                    try {
                        new ByteArrayOutputStream().withCloseable { out ->
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
                                from '/Volumes/Firefox/Firefox.app'
                                into target
                            }
                            logger.quiet("DMG file has been extracted")

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
                    } catch (Exception ex) {
                        logger.warn("A problem:", ex)
                    }

                    logger.quiet("$browser has been installed")
                    logger.debug("Installed to: $target")

                    System.properties['webdriver.firefox.bin'] = "$target/Contents/MacOS/firefox".toString()
                },
                setupGecko
        )
    }

    /**
     * Return url for downloading a webdriver.
     *  For example:
     *      https://github.com/mozilla/geckodriver/releases/download/v0.19.1/geckodriver-v0.19.1-linux64.tar.gz
     *      https://github.com/mozilla/geckodriver/releases/download/v0.19.1/geckodriver-v0.19.1-linux32.tar.gz
     *      https://github.com/mozilla/geckodriver/releases/download/v0.19.1/geckodriver-v0.19.1-macos.tar.gz
     *
     * @return
     */
    String getDriverUrl() {
        final String platform = "${isMacOsX() ? 'macos' : (is64() ? 'linux64' : 'linux32')}"
        return "https://github.com/mozilla/geckodriver/releases/download/" +
                "v$driverVersion/geckodriver-v$driverVersion-${platform}.tar.gz"
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://ftp.mozilla.org/pub/firefox/releases/58.0.2/linux-x86_64/en-US/firefox-58.0.2.tar.bz2
     *      https://ftp.mozilla.org/pub/firefox/releases/58.0.2/linux-i686/en-US/firefox-58.0.2.tar.bz2
     *      https://ftp.mozilla.org/pub/firefox/releases/58.0.2/mac/en-US/Firefox%2058.0.2.dmg
     *
     * @return url
     */
    String getBrowserUrl() {
        if (isMacOsX()) {
            return "https://ftp.mozilla.org/pub/firefox/releases/$browserVersion/mac/en-US/Firefox%20${browserVersion}.dmg"
        }

        final String platform = "${is64() ? 'linux-x86_64' : 'linux-i686'}"
        return "https://ftp.mozilla.org/pub/firefox/releases/$browserVersion/$platform/en-US/firefox-${browserVersion}.tar.bz2"
    }
}
