package com.github.zhurlik.task

import com.github.zhurlik.domain.Browsers
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

/**
 * A task for installing PhantomJS browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallPhantomJsOnWindows extends WindowsTask {

    @Input
    String browserVersion

    private Browsers browser = Browsers.PHANTOMJS

    @TaskAction
    void apply() {

        //choco install phantomjs --version 2.1.1 -my
        choco('phantomjs', browserVersion)
        System.properties['phantomjs.binary.path'] = Paths.get(getToolsLocation(), 'PhantomJS',
                "phantomjs-$browserVersion-windows", 'phantomjs.exe').toString()
    }
}
