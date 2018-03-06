package com.github.zhurlik

import org.gradle.api.Task

import java.nio.file.Path
import java.nio.file.Paths

/**
 * For extracting a common methods.
 *
 * @author zhurlik@gmail.com
 */
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

    /**
     * Makes screenshots.
     *
     * @param task Gradle task to get temporary folder
     * @param screenshot bytes[]
     * @param pageName a file will be as: temporary folder/{name}.png
     */
    protected void screenshot(final Task task, final byte[] screenshot, final String pageName ) {

        final Path path = Paths.get("${task.temporaryDir}", "${pageName}.png")

        new BufferedOutputStream(new FileOutputStream(path.toFile())).withCloseable {out->
            out.write(screenshot)
        }
    }
}
