package com.github.zhurlik.task

import org.gradle.api.DefaultTask
import org.gradle.process.ExecResult

abstract class MacTask extends DefaultTask {

    /**
     * Extracts a dmg file using mounting/unmounting into a special folder.
     *
     * @param archive a dmg file
     * @param innerPath what should be copied
     * @param target where will be extracted
     */
    protected void extractDmg(final String archive, final String innerPath, final String target){
        // extracting dmg file
        new ByteArrayOutputStream().withCloseable { out ->
            // mount
            ExecResult res = project.exec {
                commandLine '/bin/bash', '-c', "yes qy | hdiutil attach $archive"
                standardOutput = out
                ignoreExitValue = true
            }

            String log = out.toString()
            if (res.exitValue == 0) { // success
                logger.quiet("DMG file has been mounted")
                logger.debug("Log: $log")
            } else {
                logger.error("A problem with mounting dmg file: $log")
                res.rethrowFailure()
            }

            // copy files
            project.copy {
                from innerPath
                into target
            }
            logger.quiet("DMG file has been extracted")

            // unmount
            res = project.exec {
                commandLine 'hdiutil', 'detach', log.readLines().last().find('/dev/disk1s[0-9]')
                standardOutput = out
                ignoreExitValue = true
            }

            log = out.toString()
            if (res.exitValue == 0) { // success
                logger.quiet("DMG file has been unmounted")
                logger.debug("Log: $log")
            } else {
                logger.error("A problem with unmounting dmg file: $log")
                res.rethrowFailure()
            }
        }
    }
}
