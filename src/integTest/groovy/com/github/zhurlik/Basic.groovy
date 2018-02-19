package com.github.zhurlik

import org.gradle.api.Task

abstract class Basic {
    /**
     * To be able to execute a gradle task.
     *
     * @param task
     */
    protected void executeTask(final Task task) {
        task.taskDependencies.getDependencies(task).each {
            subTask -> executeTask(subTask)
        }

        task.execute()
    }
}
