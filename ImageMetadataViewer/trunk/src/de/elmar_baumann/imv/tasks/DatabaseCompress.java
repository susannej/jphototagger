package de.elmar_baumann.imv.tasks;

import de.elmar_baumann.imv.app.AppLog;
import de.elmar_baumann.imv.database.DatabaseMaintainance;
import de.elmar_baumann.imv.event.ProgressEvent;
import de.elmar_baumann.imv.event.ProgressListener;
import de.elmar_baumann.imv.resource.Bundle;
import java.util.ArrayList;
import java.util.List;

/**
 * Compresses the database.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/10/30
 */
public final class DatabaseCompress implements Runnable {

    private final List<ProgressListener> listeners = new ArrayList<ProgressListener>();
    private boolean success = false;

    public synchronized void addProgressListener(ProgressListener l) {
        listeners.add(l);
    }

    /**
     * Returns the success of the database operation.
     * 
     * @return true if successfully compressed.
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * Compresses the database. The process can't be stopped and the progress
     * values can't submitted to them. The listeners are only notified about
     * the start and end of compressing.
     */
    @Override
    public void run() {
        logCompressDatabase();
        notifyStarted();
        success = DatabaseMaintainance.getInstacne().compressDatabase();
        notifyEnded();
    }

    private synchronized void notifyStarted() {
        ProgressEvent evt = new ProgressEvent(this, Bundle.getString("DatabaseCompress.StartMessage"));
        for (ProgressListener listener : listeners) {
            listener.progressStarted(evt);
        }
    }

    private void logCompressDatabase() {
        AppLog.logInfo(DatabaseCompress.class, Bundle.getString("DatabaseCompress.InformationMessage.StartCompress"));
    }

    private synchronized void notifyEnded() {
        ProgressEvent evt = new ProgressEvent(this, 0, 1, 1, getEndMessage());
        for (ProgressListener listener : listeners) {
            listener.progressEnded(evt);
        }
    }

    private Object getEndMessage() {
        Object[] params = {success
            ? Bundle.getString("DatabaseCompress.EndMessage.Success.True")
            : Bundle.getString("DatabaseCompress.EndMessage.Success.False")
        };
        return Bundle.getString("DatabaseCompress.EndMessage", params);
    }
}
