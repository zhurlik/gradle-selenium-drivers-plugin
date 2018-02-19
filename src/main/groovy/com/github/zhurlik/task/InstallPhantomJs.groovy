package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import com.github.zhurlik.domain.Drivers
import org.apache.tools.ant.BuildException

import java.nio.file.Paths

/**
 * A task for installing PhantomJS browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallPhantomJs extends AbstractInstall {

    InstallPhantomJs() {
        browser = Browsers.PHANTOMJS
        driver = Drivers.UNKNOWN
    }

    /**
     * Installing PhantomJS on Windows via choco.
     */
    @Override
    void onWindows() {
        if (isWindows()) {
            //choco install phantomjs --version 2.1.1 -my
            choco('phantomjs')
        }
    }

    /**
     * Usual installation for Linux.
     * There are 2 sites for downloading:
     *  1. https://bitbucket.org/ariya/phantomjs/downloads/
     *  2. https://code.google.com/archive/p/phantomjs/downloads
     */
    @Override
    void onLinux() {
        if (isLinux()) {
            Optional.ofNullable(linuxInstaller).orElse {
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

                final String filename = Paths.get(new URI(url).getPath()).getFileName().toString()
                final String archive = "${temporaryDir.path}/$filename"
                logger.debug("Downloaded: $archive")
                final String target = "${project.buildDir}/browser/$browser/$browserVersion"
                project.copy {
                    from project.tarTree(project.resources.bzip2(archive))
                    into target
                }
                logger.quiet("$browser has been installed")
                logger.debug("Installed to: $target")
            }()
        }
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-linux-x86_64.tar.bz2
     *      https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-linux-i686.tar.bz2
     *
     * @return url
     */
    String getUrlOnBitbucket() {
        final String platform = "${is64() ? 'linux-x86_64' : 'linux-i686'}"
        return "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-$browserVersion-${platform}.tar.bz2"
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-1.9.2-linux-x86_64.tar.bz2
     *      https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-1.9.2-linux-i686.tar.bz2
     *
     * @return url
     */
    String getUrlOnGoogleCode() {
        final String platform = "${is64() ? 'linux-x86_64' : 'linux-i686'}"
        return "https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-" +
                "$browserVersion-${platform}.tar.bz2"
    }
}
