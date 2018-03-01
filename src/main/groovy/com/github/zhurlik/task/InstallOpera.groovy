package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import com.github.zhurlik.domain.Installer

import java.nio.file.Paths

/**
 * A task for installing Opera browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallOpera extends AbstractInstall {

    InstallOpera() {
        browser = Browsers.OPERA
        driver = Drivers.OPERA

        linuxInstaller = new  Installer(
                /**
                 * Usual installation for Linux.
                 * Downloading from: ftp://ftp.opera.com/pub/opera/desktop/
                 */
                {
                    ant.get(src: getUrl(),
                            dest: temporaryDir.path,
                            skipexisting: true,
                            verbose: true
                    )

                    final String filename = Paths.get(new URI(getUrl()).getPath()).getFileName().toString()
                    final String archive = "${temporaryDir.path}/$filename"
                    logger.debug("Downloaded: $archive")
                    final String target = "${project.buildDir}/browser/$browser/$browserVersion"
                    project.copy {
                        from project.tarTree(project.resources.bzip2(archive))
                        into target
                    }
                    logger.quiet("$browser has been installed")
                    logger.debug("Installed to: $target")
                },
                {}
        )

        windowsInstaller = new Installer(
                {
                    //choco install opera --version 51.0.2830.34 -my
                    choco('opera', browserVersion)
                    // TODO: check for other versions
                    System.properties['webdriver.opera.bin'] = "C:\\Program Files\\Opera\\${browserVersion}_0\\opera.exe".toString()
                },
                {
                    //choco install selenium-opera-driver --version 2.33 -my
                    choco('selenium-opera-driver', driverVersion)
                    System.properties['webdriver.opera.driver'] = Paths.get(getToolsLocation(), 'selenium',
                            'operadriver.exe').toString()
                }
        )

        macOsInstaller = new Installer(
                {
                    ant.get(src: getUrl(),
                            dest: temporaryDir.path,
                            skipexisting: true,
                            verbose: true
                    )

                    // browser
                    final String filename = Paths.get(new URI(getUrl()).getPath()).getFileName().toString()
                    final String archive = "${temporaryDir.path}/$filename"
                    logger.debug("Downloaded: $archive")
                    final String target = "${project.buildDir}/browser/$browser/$browserVersion"

                    extractDmg(archive, '/Volumes/Opera/Opera.app', target)

                    logger.quiet("$browser has been installed")
                    logger.debug("Installed to: $target")

                    System.properties['webdriver.opera.bin'] = "$target/Contents/MacOS/opera".toString()
                },
                {}
        )
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      ftp://ftp.opera.com/pub/opera/desktop/51.0.2830.40/mac/Opera_51.0.2830.40_Setup.dmg
     *
     * @return url
     */
    String getUrl() {
        if (isMacOsX()) {
            return "ftp://ftp.opera.com/pub/opera/desktop/$browserVersion/mac/Opera_${browserVersion}_Setup.dmg"
        }

        final String platform = "${is64() ? 'linux-x86-64' : 'linux-i386'}"
        return "https://www.opera.com/download/index.dml/?os=$platform&ver=$browserVersion&local=y"
    }
}
