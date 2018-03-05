package com.github.zhurlik.task

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * A task for installing Internet Explorer 11 browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallIE11OnWindows extends WindowsTask {
    @Input
    String browserVersion

    @Input
    String driverVersion

    @TaskAction
    void apply() {
        //choco install ie11 --version 0.2 -my
        choco('ie11', browserVersion)
        //choco install selenium-ie-driver --version 3.8.0 -my
        choco('selenium-ie-driver', driverVersion)
    }
}
