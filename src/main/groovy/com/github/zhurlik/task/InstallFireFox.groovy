package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers

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
    }

    /**
     * Installing FireFox on Windows via choco.
     */
    @Override
    void onWindows() {
        if (isWindows()) {
            //choco install firefox --version 58.0.2 -my
            choco('firefox')
        }
    }

    /**
     * Usual installation for Linux.
     */
    @Override
    void onLinux() {
        if (isLinux()) {
            Optional.ofNullable(linuxInstaller).orElse {
                ant.get(src: getUrl(),
                        dest: temporaryDir.path,
                        skipexisting: true,
                        verbose: true
                )

                // browser
                String filename = Paths.get(new URI(url).getPath()).getFileName().toString()
                String archive = "${temporaryDir.path}/$filename"
                logger.debug("Downloaded: $archive")
                String target = "${project.buildDir}/browser/$browser/$browserVersion"
                project.copy {
                    from project.tarTree(project.resources.bzip2(archive))
                    into target
                }
                logger.quiet("$browser has been installed")
                logger.debug("Installed to: $target")

                System.properties['webdriver.firefox.bin'] = Paths.get(target, 'firefox', 'firefox').toString()

                // webdriver
                ant.get(src: getDriverUrl(),
                        dest: temporaryDir.path,
                        skipexisting: true,
                        verbose: true
                )

                filename = Paths.get(new URI(getDriverUrl()).getPath()).getFileName().toString()
                archive = "${temporaryDir.path}/$filename"
                logger.debug("Downloaded: $archive")
                target = "${project.buildDir}/driver/$driver/$driverVersion"
                project.copy {
                    from project.tarTree(project.resources.gzip(archive))
                    into target
                }

                System.properties['webdriver.gecko.driver'] = Paths.get(target, 'geckodriver').toString()

                logger.quiet("$driver has been installed")
                logger.debug("Installed to: $target")
            }()
        }
    }

    String getDriverUrl() {
        return 'https://github.com/mozilla/geckodriver/releases/download/v0.19.1/geckodriver-v0.19.1-linux64.tar.gz'
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://ftp.mozilla.org/pub/firefox/releases/58.0.2/linux-x86_64/en-US/firefox-58.0.2.tar.bz2
     *      https://ftp.mozilla.org/pub/firefox/releases/58.0.2/linux-i686/en-US/firefox-58.0.2.tar.bz2
     *
     * @return url
     */
    String getUrl() {
        final String platform = "${is64() ? 'linux-x86_64' : 'linux-i686'}"
        return "https://ftp.mozilla.org/pub/firefox/releases/$browserVersion/$platform/en-US/firefox-${browserVersion}.tar.bz2"
    }
}
