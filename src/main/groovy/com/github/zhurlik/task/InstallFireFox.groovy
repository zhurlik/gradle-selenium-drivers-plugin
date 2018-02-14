package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.gradle.api.tasks.TaskAction

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

    @TaskAction
    void apply() {
        logger.quiet('Installing...')
        info()

        if(isLinux()) {
            ant.get(src: getUrl(),
                    dest: temporaryDir.path,
                    skipexisting: true,
                    verbose: true
            )

            String filename = Paths.get(new URI(url).getPath()).getFileName().toString()
            project.copy {
                from project.tarTree(project.resources.bzip2("${temporaryDir.path}/$filename"))
                into "${project.buildDir}/browser/$browser/$browserVersion"
            }
        }

        // TODO: via chocolatey
        if (isWindows()) {
            throw new UnsupportedOperationException()
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
