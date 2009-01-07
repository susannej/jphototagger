package de.elmar_baumann.imv.database;

import de.elmar_baumann.imv.Log;
import de.elmar_baumann.imv.event.DatabaseAction;
import de.elmar_baumann.imv.event.ProgressEvent;
import de.elmar_baumann.imv.event.ProgressListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/10/21
 */
public class DatabaseFileExcludePattern extends Database {
    
    private static DatabaseFileExcludePattern instance = new DatabaseFileExcludePattern();
    
    public static DatabaseFileExcludePattern getInstance() {
        return instance;
    }
    
    private DatabaseFileExcludePattern() {
    }

    /**
     * Inserts a file exclude pattern.
     *
     * @param  pattern  pattern
     * @return true if inserted
     * @see    #existsFileExcludePattern(java.lang.String)
     */
    public synchronized boolean insertFileExcludePattern(String pattern) {
        boolean inserted = false;
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO file_exclude_pattern (pattern) VALUES (?)"); // NOI18N
            stmt.setString(1, pattern);
            Log.logFiner(DatabaseFileExcludePattern.class, stmt.toString());
            int count = stmt.executeUpdate();
            connection.commit();
            stmt.close();
            inserted = count > 0;
        } catch (SQLException ex) {
            de.elmar_baumann.imv.Log.logWarning(getClass(), ex);
        } finally {
            free(connection);
        }
        return inserted;
    }

    /**
     * Deletes a file exclude pattern from the database.
     *
     * @param  pattern  pattern
     * @return true if deleted
     */
    public synchronized boolean deleteFileExcludePattern(String pattern) {
        boolean deleted = false;
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM file_exclude_pattern WHERE pattern = ?"); // NOI18N
            stmt.setString(1, pattern);
            Log.logFiner(DatabaseFileExcludePattern.class, stmt.toString());
            int count = stmt.executeUpdate();
            connection.commit();
            stmt.close();
            deleted = count > 0;
        } catch (SQLException ex) {
            de.elmar_baumann.imv.Log.logWarning(getClass(), ex);
        } finally {
            free(connection);
        }
        return deleted;
    }

    /**
     * Returns wheter a file exclude pattern exists.
     *
     * @param  pattern pattern
     * @return true if exists
     */
    public boolean existsFileExcludePattern(String pattern) {
        boolean exists = false;
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT COUNT(*) FROM file_exclude_pattern WHERE pattern = ?"); // NOI18N
            stmt.setString(1, pattern);
            Log.logFinest(DatabaseFileExcludePattern.class, stmt.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
            stmt.close();
        } catch (SQLException ex) {
            de.elmar_baumann.imv.Log.logWarning(getClass(), ex);
        } finally {
            free(connection);
        }
        return exists;
    }

    /**
     * Returns all file exclude patterns.
     *
     * @return patterns
     */
    public List<String> getFileExcludePatterns() {
        List<String> patterns = new LinkedList<String>();
        Connection connection = null;
        try {
            connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT pattern FROM file_exclude_pattern ORDER BY pattern ASC"); // NOI18N
            while (rs.next()) {
                patterns.add(rs.getString(1));
            }
            stmt.close();
        } catch (SQLException ex) {
            de.elmar_baumann.imv.Log.logWarning(getClass(), ex);
        } finally {
            free(connection);
        }
        return patterns;
    }

    /**
     * Deletes files from the database which matches against some patterns.
     *
     * @param   patterns  patterns
     * @param   listener  progress listener, can cancel the action
     * @return  count of deleted files
     */
    public synchronized int deleteFilesWithPattern(
        List<String> patterns, ProgressListener listener) {
        
        Connection connection = null;
        int count = 0;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            List<String> deletedFiles = new LinkedList<String>();
            Statement queryStmt = connection.createStatement();
            PreparedStatement updateStmt = connection.prepareStatement(
                "DELETE FROM files WHERE filename = ?"); // NOI18N
            ResultSet rs = queryStmt.executeQuery("SELECT filename FROM files"); // NOI18N
            int patternCount = patterns.size();
            int progress = 0;
            ProgressEvent event = new ProgressEvent(this, 0, DatabaseStatistics.getInstance().getFileCount() * patternCount, 0, null);
            notifyProgressListenerStart(listener, event);
            boolean stop = event.isStop();
            while (!stop && rs.next()) {
                String filename = rs.getString(1);
                for (int i = 0; !stop && i < patternCount; i++) {
                    progress++;
                    String pattern = patterns.get(i);
                    if (filename.matches(pattern)) {
                        updateStmt.setString(1, filename);
                        deletedFiles.add(filename);
                        Log.logFiner(DatabaseFileExcludePattern.class, updateStmt.toString());
                        updateStmt.executeUpdate();
                        stop = event.isStop();
                    }
                    event.setInfo(filename);
                    event.setValue(progress);
                    notifyProgressListenerPerformed(listener, event);
                }
            }
            connection.commit();
            queryStmt.close();
            updateStmt.close();
            notifyProgressListenerEnd(listener, event);
            notifyDatabaseListener(DatabaseAction.Type.ImageFilesDeleted, deletedFiles);
        } catch (SQLException ex) {
            de.elmar_baumann.imv.Log.logWarning(getClass(), ex);
        } finally {
            free(connection);
        }
        return count;
    }

}
