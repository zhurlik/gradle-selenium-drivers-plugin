package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import com.github.zhurlik.domain.Installer
import org.apache.tools.ant.BuildException

import java.nio.file.Paths

/**
 * A task for installing PhantomJS browser.
 *
 * @author zhurlik@gmail.com
 */
@Deprecated
class InstallPhantomJs extends AbstractInstall {

    InstallPhantomJs() {
        browser = Browsers.PHANTOMJS
        driver = Drivers.PHANTOMJS // GHOST
        driverVersion = 'not required'

        linuxInstaller = new Installer(

                /**
                 * Usual installation for Linux.
                 * There are 2 sites for downloading:
                 *  1. https://bitbucket.org/ariya/phantomjs/downloads/
                 *  2. https://code.google.com/archive/p/phantomjs/downloads
                 */
                {
                    final String filename = downloadInstaller()
                    final String archive = "${temporaryDir.path}/$filename"
                    logger.debug("Downloaded: $archive")
                    final String target = "${project.buildDir}/browser/$browser/$browserVersion"
                    project.copy {
                        from project.tarTree(project.resources.bzip2(archive))
                        into target
                    }
                    logger.quiet("$browser has been installed")
                    logger.debug("Installed to: $target")

                    final String platform = "${is64() ? 'linux-x86_64' : 'linux-i686'}"
                    System.properties['phantomjs.binary.path'] = Paths.get(target,
                            "phantomjs-$browserVersion-$platform", 'bin', 'phantomjs').toString()
                },
                {}
        )

        windowsInstaller = new Installer(
                {
                    //choco install phantomjs --version 2.1.1 -my
                    choco('phantomjs', browserVersion)
                    System.properties['phantomjs.binary.path'] = Paths.get(getToolsLocation(), 'PhantomJS',
                            "phantomjs-$browserVersion-windows", 'phantomjs.exe').toString()
                },
                {}
        )

        macOsInstaller = new Installer(
                {
                    final String filename = downloadInstaller()
                    final String archive = "${temporaryDir.path}/$filename"
                    logger.debug("Downloaded: $archive")
                    final String target = "${project.buildDir}/browser/$browser/$browserVersion"
                    project.copy {
                        from project.zipTree(archive)
                        into target
                    }
                    logger.quiet("$browser has been installed")
                    logger.debug("Installed to: $target")

                    System.properties['phantomjs.binary.path'] = Paths.get(target,
                            "phantomjs-$browserVersion-macosx", 'bin', 'phantomjs').toString()
                }, {}
        )
    }

    /**
     * Downloads and stores an installer in the temoprary folder.
     *
     * @return file name
     */
    String downloadInstaller() {
        String url = null
        // first attempt on https://bitbucket.org/ariya/phantomjs/downloads/
        try {
            url = getUrlOnBitbucket()
            ant.get(src: url,
                    dest: temporaryDir.path,
                    skipexisting: true,
                    verbose: true
            )
            logger.debug('Loaded from https://bitbucket.org/ariya/phantomjs/downloads/')
        } catch (BuildException ex) {
            // second attempt on https://code.google.com/archive/p/phantomjs/downloads
            url = getUrlOnGoogleCode()
            ant.get(src: url,
                    dest: temporaryDir.path,
                    skipexisting: true,
                    verbose: true
            )
            logger.debug('Loaded from https://code.google.com/archive/p/phantomjs/downloads')
        }

        return Paths.get(new URI(url).getPath()).getFileName().toString()
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-linux-x86_64.tar.bz2
     *      https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-linux-i686.tar.bz2
     *      https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-macosx.zip
     *
     * @return url
     */
    String getUrlOnBitbucket() {
        if (isMacOsX()) {
            return "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-$browserVersion-macosx.zip"
        }

        final String platform = "${is64() ? 'linux-x86_64' : 'linux-i686'}"
        return "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-$browserVersion-${platform}.tar.bz2"
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-1.9.2-linux-x86_64.tar.bz2
     *      https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-1.9.2-linux-i686.tar.bz2
     *      https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-1.9.2-macosx.zip
     *
     * @return url
     */
    String getUrlOnGoogleCode() {
        if (isMacOsX()) {
            return "https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-" +
                    "$browserVersion-macosx.zip"
        }

        final String platform = "${is64() ? 'linux-x86_64' : 'linux-i686'}"
        return "https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-" +
                "$browserVersion-${platform}.tar.bz2"
    }
}
