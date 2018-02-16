package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.gradle.api.GradleException

/**
 * A task for installing Google Chrome browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallChrome extends AbstractInstall {

    InstallChrome() {
        browser = Browsers.CHROME
    }

    /**
     * Installing Google Chrome on Windows via choco.
     */
    @Override
    void onWindows() {
        if (isWindows()) {
            //choco install googlechrome --version 64.0.3282.16700 -my
            choco('googlechrome')
        }
    }

    /**
     * Usual installation for Linux.
     */
    @Override
    void onLinux() {
        throw new GradleException('Not implemented yet')
//        if (isLinux()) {
//            Optional.ofNullable(linuxInstaller).orElse {
//                ant.get(src: getUrl(),
//                        dest: temporaryDir.path,
//                        skipexisting: true,
//                        verbose: true
//                )
//
//                final String filename = Paths.get(new URI(url).getPath()).getFileName().toString()
//                final String archive = "${temporaryDir.path}/$filename"
//                logger.debug("Downloaded: $archive")
//                final String target = "${project.buildDir}/browser/$browser/$browserVersion"
//                project.copy {
//                    from project.tarTree(project.resources.bzip2(archive))
//                    into target
//                }
//                logger.quiet("FireFox has been installed")
//                logger.debug("Installed to: $target")
//            }()
//        }
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
