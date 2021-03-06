package org.jphototagger.program.misc;

import org.jphototagger.lib.runtime.External;
import org.jphototagger.program.app.AppLifeCycle.FinalTask;

/**
 * Executable command that can be called before JPhotoTaggers quits.
 *
 * <p>Usage: Create an instance and add it to
 * {@code AppLifeCycle#addFinalTask(org.jphototagger.program.app.AppLifeCycle.FinalTask)}
 *
 * @author Elmar Baumann
 */
public final class FinalExecutable extends FinalTask {

    private String executable;

    public FinalExecutable(String executable) {
        if (executable == null) {
            throw new NullPointerException("executable == null");
        }
        this.executable = executable;
    }

    @Override
    public void execute() {
        External.execute(executable);
        notifyFinished();
    }
}
