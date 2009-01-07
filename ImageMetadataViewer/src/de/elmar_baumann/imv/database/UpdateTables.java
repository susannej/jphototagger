package de.elmar_baumann.imv.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Updates tables from previous application versions
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/10/23
 */
final class UpdateTables extends Database {

    private static final UpdateTables instance = new UpdateTables();

    static UpdateTables getInstance() {
        return instance;
    }

    private UpdateTables() {}

    synchronized void update(Connection connection) throws SQLException {
        new UpdateTablesDropColumns().update(connection);
        new UpdateTableRenameColumns().update(connection);
        new UpdateTablesAddColumns().update(connection);
        new UpdateTablesXmpLastModified().update(connection);
        new UpdateTablesPrograms().update(connection);
        UpdateTablesMessages.getInstance().getProgressDialog().setVisible(false);
    }
}
