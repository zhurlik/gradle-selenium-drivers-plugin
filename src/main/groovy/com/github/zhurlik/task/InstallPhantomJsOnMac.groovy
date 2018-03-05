package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.apache.tools.ant.BuildException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

/**
 * A task for installing PhantomJS browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallPhantomJsOnMac extends MacTask {

    @Input
    String browserVersion

    private Browsers browser = Browsers.PHANTOMJS

    @TaskAction
    void apply() {
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
    }

    /**
     * Downloads and stores an installer in the temoprary folder.
     *
     * @return file name
     */
    private String downloadInstaller() {
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
     *      https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-macosx.zip
     *
     * @return url
     */
    private String getUrlOnBitbucket() {
        return "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-$browserVersion-macosx.zip"
    }

    /**
     * Returns url for downloading the corresponded version.
     * For example:
     *      https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-1.9.2-macosx.zip
     *
     * @return url
     */
    private String getUrlOnGoogleCode() {
        return "https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/phantomjs/phantomjs-" +
                "$browserVersion-macosx.zip"
    }
}
