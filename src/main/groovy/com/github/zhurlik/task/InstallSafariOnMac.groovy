package com.github.zhurlik.task

import org.gradle.api.tasks.TaskAction

/**
 * A task for installing Safari browser.
 *
 * @author zhurlik@gmail.com
 */
class InstallSafariOnMac extends MacTask {
    @TaskAction
    void apply() {
        // doesn't require any actions
        // sudo /usr/bin/safaridriver --enable
    }
}
