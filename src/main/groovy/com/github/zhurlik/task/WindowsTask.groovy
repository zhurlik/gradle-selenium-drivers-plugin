package com.github.zhurlik.task

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.process.ExecResult

abstract class WindowsTask extends DefaultTask {
    /**
     * Common method for installing a package on Windows.
     * See https://chocolatey.org/docs/commandsinstall.
     *
     * @param packageName name
     * @param packageVersion version
     */
    protected void choco(final String packageName, final String packageVersion) {
        try {
            new ByteArrayOutputStream().withCloseable { out ->
                ExecResult res = project.exec {
                    commandLine 'cmd', '/c', "choco install $packageName --version $packageVersion -my"
                    standardOutput = out
                    ignoreExitValue = true
                }

                final String log = out.toString()
                if (res.exitValue == 0) { // success
                    logger.quiet("$packageName has been installed")
                    logger.debug("Intsallation log: $log")
                } else { // failure
                    logger.error("A problem during installation: $log")
                    res.rethrowFailure()
                }
            }
        } catch (Exception ex) {
            throw new GradleException("$packageName is not installed:", ex)
        }
    }

    /**
     *  A wrapper to invoke Get-ToolsLocation function via powershell.
     *  See https://chocolatey.org/docs/helpers-get-tools-location
     *
     * @return result of Get-ToolsLoacation, default is c:\tools
     */
    protected String getToolsLocation() {
        new ByteArrayOutputStream().withCloseable { out ->
            try {
                ExecResult res = project.exec {
                    commandLine 'powershell',
                            'Set-ExecutionPolicy -ExecutionPolicy Unrestricted -Confirm:$False;',
                            "Import-Module ${System.env['ChocolateyInstall']}\\helpers\\chocolateyInstaller.psm1 -Force;",
                            'Get-ToolsLocation'
                    standardOutput = out
                    ignoreExitValue = true
                }

                if (res.exitValue == 0) { // success
                    final String value = out.toString().trim()
                    logger.debug("Get-ToolsLocation: ${value}")
                    return value
                } else { // failure
                    logger.error("There is a problem with Get-ToolsLocation: ${out.toString()}")
                }
            } catch (Exception ex) {
                logger.error('There is a problem with Get-ToolsLocation:', ex)
            }

            // default
            return 'c:\\tools'
        }
    }
}
