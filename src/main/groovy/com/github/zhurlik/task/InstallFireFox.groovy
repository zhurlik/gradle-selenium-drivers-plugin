package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
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
    }

    @TaskAction
    void apply() {
        logger.quiet('Installing...')
        info()
        // unzip
        onLinux()
        // via chocolatey
        onWindows()
    }

    /**
     * Installing FireFox on Windows via choco.
     * See https://chocolatey.org/docs/commandsinstall.
     */
    private void onWindows() {
        if (isWindows()) {
            Optional.ofNullable(windowsInstaller).orElse {
                //choco install firefox --version 58.0.2 -my
                try {
                    new ByteArrayOutputStream().withCloseable { out ->
                        ExecResult res = project.exec {
                            commandLine 'cmd', '/c', "choco install firefox --version ${browserVersion} -my"
                            standardOutput = out
                            ignoreExitValue = true
                        }

                        final String log = out.toString()
                        if (res.exitValue == 0) { // success
                            logger.quiet("FireFox has been installed")
                            logger.debug("Intsallation log: $log")
                        } else { // failure
                            logger.error("A problem during installation: $log")
                            res.rethrowFailure()
                        }
                    }
                } catch (Exception ex) {
                    throw new GradleException('FireFox is not installed:', ex)
                }
            }()
        }
    }

    /**
     * Usual installation for Linux.
     */
    private void onLinux() {
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
