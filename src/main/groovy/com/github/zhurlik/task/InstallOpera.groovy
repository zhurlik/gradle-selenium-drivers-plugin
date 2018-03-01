package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import com.github.zhurlik.domain.Installer
import org.gradle.api.GradleException

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
                 * Downloading from:
                 *      https://www.opera.com/download/index.dml/?os=linux-x86-64&list=all
                 *      https://www.opera.com/download/index.dml/?os=linux-i386&list=all
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
                    throw new GradleException('Not implemented yet')
                },
                {}
        )
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://www.opera.com/download/index.dml/?os=linux-i386&ver=45.0.2552.898&local=y
     *      https://www.opera.com/download/index.dml/?os=linux-x86-64&ver=51.0.2830.34&local=y
     *
     * @return url
     */
    String getUrl() {
        final String platform = "${is64() ? 'linux-x86-64' : 'linux-i386'}"
        return "https://www.opera.com/download/index.dml/?os=$platform&ver=$browserVersion&local=y"
    }
}
