package com.github.zhurlik.task

import org.gradle.api.DefaultTask
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.ExecResult

abstract class LinuxTask extends DefaultTask {

    /**
     * Extracts a deb files using 'ar, tar' commands and copies to target.
     *
     * @param archive a deb file
     * @param innerPath what should be copied
     * @param target where will be extracted
     */
    protected void extractDeb(final String archive, final String innerPath, final String target) {
        new ByteArrayOutputStream().withCloseable { out ->
            // extract deb
            ExecResult res = project.exec {
                workingDir temporaryDir.path
                commandLine 'ar', '-x', archive
                standardOutput = out
                ignoreExitValue = true
            }

            String log = out.toString()
            if (res.exitValue == 0) { // success
                logger.quiet("DEB file has been extracted")
                logger.debug("Log: $log")
            } else {
                logger.error("A problem with extarcting deb file: $log")
                res.rethrowFailure()
            }

            project.exec {
                workingDir temporaryDir.path
                commandLine 'tar', '-xf', 'data.tar.xz'
                standardOutput = out
                ignoreExitValue = true
            }

            log = out.toString()
            if (res.exitValue == 0) { // success
                logger.quiet("data.tar.xz file has been extracted")
                logger.debug("Log: $log")
            } else {
                logger.error("A problem with extarcting data.tar.xz file: $log")
                res.rethrowFailure()
            }

            // copy to target
            project.copy {
                from "${temporaryDir.path}/$innerPath"
                into target
            }
        }
    }

    /**
     *  Is 64 or 32 bit system.
     *
     * @return true when 64 bit system
     */
    protected boolean is64() {
        return OperatingSystem.current().getNativePrefix().contains('64')
    }

}
