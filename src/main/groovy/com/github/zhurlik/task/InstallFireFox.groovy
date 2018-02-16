package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers

import java.nio.file.Paths

/**
 * A task for installing FireFox browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallFireFox extends AbstractInstall {

    InstallFireFox() {
        browser = Browsers.FIREFOX
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

                final String filename = Paths.get(new URI(url).getPath()).getFileName().toString()
                final String archive = "${temporaryDir.path}/$filename"
                logger.debug("Downloaded: $archive")
                final String target = "${project.buildDir}/browser/$browser/$browserVersion"
                project.copy {
                    from project.tarTree(project.resources.bzip2(archive))
                    into target
                }
                logger.quiet("FireFox has been installed")
                logger.debug("Installed to: $target")
            }()
        }
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
