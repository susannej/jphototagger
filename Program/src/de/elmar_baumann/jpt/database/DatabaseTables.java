/*
 * JPhotoTagger tags and finds images fast
 * Copyright (C) 2009 by the developer team, resp. Elmar Baumann<eb@elmar-baumann.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package de.elmar_baumann.jpt.database;

import de.elmar_baumann.jpt.app.update.tables.UpdateTables;
import de.elmar_baumann.jpt.app.AppLock;
import de.elmar_baumann.jpt.app.AppLogger;
import de.elmar_baumann.jpt.resource.Bundle;
import de.elmar_baumann.lib.dialog.LongMessageDialog;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-10-21
 */
public final class DatabaseTables extends Database {

    private static final List<String>   TABLE_NAMES = new ArrayList<String>();
    public static final  DatabaseTables INSTANCE    = new DatabaseTables();

    static {
        // TODO PERMANENT: Update after adding a table
        TABLE_NAMES.add("actions_after_db_insertion");
        TABLE_NAMES.add("application");
        TABLE_NAMES.add("autoscan_directories");
        TABLE_NAMES.add("collection_names");
        TABLE_NAMES.add("collections");
        TABLE_NAMES.add("exif");
        TABLE_NAMES.add("favorite_directories");
        TABLE_NAMES.add("file_exclude_pattern");
        TABLE_NAMES.add("files");
        TABLE_NAMES.add("hierarchical_subjects");
        TABLE_NAMES.add("metadata_edit_templates");
        TABLE_NAMES.add("programs");
        TABLE_NAMES.add("saved_searches");
        TABLE_NAMES.add("saved_searches_panels");
        TABLE_NAMES.add("saved_searches_values");
        TABLE_NAMES.add("xmp");
        TABLE_NAMES.add("xmp_dc_subjects");
    }

    public static List<String> getTableNames() {
        return TABLE_NAMES;
    }

    private DatabaseTables() {
    }

    /**
     * Creates the necessary tables if not exists. Exits the VM if not successfully.
     */
    public void createTables() {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(true);
            Statement stmt = connection.createStatement();
            createAppTable(connection, stmt); // prior to all other tables!
            createFilesTable(connection, stmt);
            createXmpTables(connection, stmt);
            createExifTables(connection, stmt);
            createCollectionsTables(connection, stmt);
            createSavedSearchesTables(connection, stmt);
            createAutoScanDirectoriesTable(connection, stmt);
            createMetadataTemplateTable(connection, stmt);
            createFavoriteDirectoriesTable(connection, stmt);
            createFileExcludePatternTable(connection, stmt);
            createProgramsTable(connection, stmt);
            createActionsAfterDbInsertionTable(connection, stmt);
            createHierarchicalSubjectsTable(connection, stmt);
            createSynonymsTable(connection, stmt);
            UpdateTables.INSTANCE.update(connection);
            stmt.close();
        } catch (Exception ex) {
            AppLogger.logSevere(DatabaseTables.class, ex);
            if (ex instanceof SQLException) {
                errorMessageSqlException((SQLException) ex);
            }
            AppLock.unlock();
            System.exit(0);
        } finally {
            free(connection);
        }
    }

    private void createFilesTable(Connection connection, Statement stmt) throws
            SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "files")) {
            stmt.execute("CREATE CACHED TABLE files " +
                    " (" +
                    "  id                BIGINT                  GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY" +
                    ", filename          VARCHAR_IGNORECASE(512) NOT NULL" +
                    ", lastmodified      BIGINT" +
                    ", thumbnail         BINARY" +
                    ", xmp_lastmodified  BIGINT" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_files ON files (filename)");
        }
    }

    private void createXmpTables(Connection connection, Statement stmt) throws
            SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "xmp")) {
            stmt.execute("CREATE CACHED TABLE xmp" +
                    " (" +
                    "  id                              BIGINT                  GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY" +
                    ", id_files                        BIGINT                  NOT NULL" +
                    ", dc_creator                      VARCHAR(128)" +
                    ", dc_description                  VARCHAR_IGNORECASE(2000)" +
                    ", dc_rights                       VARCHAR_IGNORECASE(128)" +
                    ", dc_title                        VARCHAR_IGNORECASE(64)" +
                    ", iptc4xmpcore_countrycode        VARCHAR_IGNORECASE(3)" +
                    ", iptc4xmpcore_location           VARCHAR_IGNORECASE(64)" +
                    ", photoshop_authorsposition       VARCHAR_IGNORECASE(32)" +
                    ", photoshop_captionwriter         VARCHAR_IGNORECASE(32)" +
                    ", photoshop_city                  VARCHAR_IGNORECASE(32)" +
                    ", photoshop_country               VARCHAR_IGNORECASE(64)" +
                    ", photoshop_credit                VARCHAR_IGNORECASE(32)" +
                    ", photoshop_headline              VARCHAR_IGNORECASE(256)" +
                    ", photoshop_instructions          VARCHAR_IGNORECASE(256)" +
                    ", photoshop_source                VARCHAR_IGNORECASE(32)" +
                    ", photoshop_state                 VARCHAR_IGNORECASE(32)" +
                    ", photoshop_transmissionReference VARCHAR_IGNORECASE(32)" +
                    ", rating                          BIGINT" +
                    ", iptc4xmpcore_datecreated        VARCHAR_IGNORECASE(10)" +

                    ", FOREIGN KEY (id_files) REFERENCES files (id) ON DELETE CASCADE" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_xmp_id_files                        ON xmp (id_files)");
            stmt.execute("CREATE INDEX        idx_xmp_dc_description                  ON xmp (dc_description)");
            stmt.execute("CREATE INDEX        idx_xmp_dc_rights                       ON xmp (dc_rights)");
            stmt.execute("CREATE INDEX        idx_xmp_dc_title                        ON xmp (dc_title)");
            stmt.execute("CREATE INDEX        idx_xmp_iptc4xmpcore_countrycode        ON xmp (iptc4xmpcore_countrycode)");
            stmt.execute("CREATE INDEX        idx_xmp_iptc4xmpcore_location           ON xmp (iptc4xmpcore_location)");
            stmt.execute("CREATE INDEX        idx_xmp_photoshop_authorsposition       ON xmp (photoshop_authorsposition)");
            stmt.execute("CREATE INDEX        idx_xmp_photoshop_captionwriter         ON xmp (photoshop_captionwriter)");
            stmt.execute("CREATE INDEX        idx_xmp_photoshop_city                  ON xmp (photoshop_city)");
            stmt.execute("CREATE INDEX        idx_xmp_photoshop_country               ON xmp (photoshop_country)");
            stmt.execute("CREATE INDEX        idx_xmp_photoshop_credit                ON xmp (photoshop_credit)");
            stmt.execute("CREATE INDEX        idx_xmp_photoshop_headline              ON xmp (photoshop_headline)");
            stmt.execute("CREATE INDEX        idx_xmp_photoshop_instructions          ON xmp (photoshop_instructions)");
            stmt.execute("CREATE INDEX        idx_xmp_photoshop_source                ON xmp (photoshop_source)");
            stmt.execute("CREATE INDEX        idx_xmp_photoshop_state                 ON xmp (photoshop_state)");
            stmt.execute("CREATE INDEX        idx_xmp_photoshop_transmissionReference ON xmp (photoshop_transmissionReference)");
            stmt.execute("CREATE INDEX        idx_iptc4xmpcore_datecreated            ON xmp (iptc4xmpcore_datecreated)");
        }
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "xmp_dc_subjects")) {
            stmt.execute("CREATE CACHED TABLE xmp_dc_subjects" +
                    " (" +
                    "  id_xmp  BIGINT                NOT NULL" +
                    ", subject VARCHAR_IGNORECASE(64)" +
                    ", FOREIGN KEY (id_xmp) REFERENCES xmp (id) ON DELETE CASCADE" +
                    ");");
            stmt.execute("CREATE INDEX idx_xmp_dc_subjects_id_xmp  ON xmp_dc_subjects (id_xmp)");
            stmt.execute("CREATE INDEX idx_xmp_dc_subjects_subject ON xmp_dc_subjects (subject)");
        }
    }

    private void createExifTables(Connection connection, Statement stmt) throws
            SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "exif")) {
            stmt.execute("CREATE CACHED TABLE exif" +
                    " (" +
                    "  id                       BIGINT                 GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY" +
                    ", id_files                 BIGINT                 NOT NULL" +
                    ", exif_recording_equipment VARCHAR_IGNORECASE(125)" +
                    ", exif_date_time_original  DATE" +
                    ", exif_focal_length        REAL" +
                    ", exif_iso_speed_ratings   SMALLINT" +
                    ", exif_lens                VARCHAR_IGNORECASE(256)" +

                    ", FOREIGN KEY (id_files) REFERENCES files (id) ON DELETE CASCADE" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_exif_id_files            ON exif (id_files)");
            stmt.execute("CREATE INDEX        idx_exif_recording_equipment ON exif (exif_recording_equipment)");
            stmt.execute("CREATE INDEX        idx_exif_date_time_original  ON exif (exif_date_time_original)");
            stmt.execute("CREATE INDEX        idx_exif_focal_length        ON exif (exif_focal_length)");
            stmt.execute("CREATE INDEX        idx_exif_iso_speed_ratings   ON exif (exif_iso_speed_ratings)");
            stmt.execute("CREATE INDEX        idx_exif_lens                ON exif (exif_lens)");
        }
    }

    private void createCollectionsTables(Connection connection, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "collection_names")) {
            stmt.execute("CREATE CACHED TABLE collection_names" +
                    " (" +
                    "  id   BIGINT                 GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY" +
                    ", name VARCHAR_IGNORECASE(256)" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_collection_names_id   ON collection_names (id)");
            stmt.execute("CREATE INDEX        idx_collection_names_name ON collection_names (name)");
        }
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "collections")) {
            stmt.execute("CREATE CACHED TABLE collections" +
                    " (" +
                    "  id_collectionnnames BIGINT" +
                    ", id_files            BIGINT" +
                    ", sequence_number     INTEGER" +

                    ", FOREIGN KEY (id_collectionnnames) REFERENCES collection_names (id) ON DELETE CASCADE" +
                    ", FOREIGN KEY (id_files)            REFERENCES files (id)            ON DELETE CASCADE" +
                    ");");
            stmt.execute("CREATE INDEX idx_collections_id_collectionnnames ON collections (id_collectionnnames)");
            stmt.execute("CREATE INDEX idx_collections_id_files            ON collections (id_files)");
            stmt.execute("CREATE INDEX idx_collections_sequence_number     ON collections (sequence_number)");
        }
    }

    private void createSavedSearchesTables(Connection connection, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "saved_searches")) {
            stmt.execute("CREATE CACHED TABLE saved_searches" +
                    " (" +
                    "  id          BIGINT                 GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY" +
                    ", name        VARCHAR_IGNORECASE(125)" +
                    ", sql_string  BINARY" +
                    ", is_query    BOOLEAN" +
                    ", search_type SMALLINT" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_saved_searches_id   ON saved_searches (id)");
            stmt.execute("CREATE UNIQUE INDEX idx_saved_searches_name ON saved_searches (name)");
        }
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "saved_searches_values")) {
            stmt.execute("CREATE CACHED TABLE saved_searches_values" +
                    " (" +
                    "  id_saved_searches BIGINT" +
                    ", value             VARCHAR(256)" +
                    ", value_index       INTEGER" +

                    ", FOREIGN KEY (id_saved_searches) REFERENCES saved_searches (id) ON DELETE CASCADE" +
                    ");");
            stmt.execute("CREATE INDEX idx_saved_searches_id_saved_searches ON saved_searches_values (id_saved_searches)");
            stmt.execute("CREATE INDEX idx_saved_searches_value_index       ON saved_searches_values (value_index)");
        }
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "saved_searches_panels")) {
            stmt.execute("CREATE CACHED TABLE saved_searches_panels" +
                    " (" +
                    "  id_saved_searches BIGINT" +
                    ", panel_index       INTEGER" +
                    ", bracket_left_1    BOOLEAN" +
                    ", operator_id       INTEGER" +
                    ", bracket_left_2    BOOLEAN" +
                    ", column_id         INTEGER" +
                    ", comparator_id     INTEGER" +
                    ", value             VARCHAR(256)" +
                    ", bracket_right     BOOLEAN" +

                    ", FOREIGN KEY (id_saved_searches) REFERENCES saved_searches (id) ON DELETE CASCADE" +
                    ");");
            stmt.execute("CREATE INDEX idx_saved_searches_panels_id_saved_searches ON saved_searches_panels (id_saved_searches)");
            stmt.execute("CREATE INDEX idx_saved_searches_panels_panel_index       ON saved_searches_panels (panel_index)");
        }
    }

    private void createAutoScanDirectoriesTable(Connection connection, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "autoscan_directories")) {
            stmt.execute("CREATE CACHED TABLE autoscan_directories" +
                    " (" +
                    "directory VARCHAR_IGNORECASE(1024)" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_autoscan_directories_directory ON autoscan_directories (directory)");
        }
    }

    private void createMetadataTemplateTable(Connection connection, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "metadata_edit_templates")) {
            stmt.execute("CREATE CACHED TABLE metadata_edit_templates" +
                    " (" +
                    "  name                           VARCHAR_IGNORECASE(256)" +
                    ", dcSubjects                     BINARY" +
                    ", dcTitle                        BINARY" +
                    ", photoshopHeadline              BINARY" +
                    ", dcDescription                  BINARY" +
                    ", photoshopCaptionwriter         BINARY" +
                    ", iptc4xmpcoreLocation           BINARY" +
                    ", iptc4xmpcoreCountrycode        BINARY" +
                    ", dcRights                       BINARY" +
                    ", dcCreator                      BINARY" +
                    ", photoshopAuthorsposition       BINARY" +
                    ", photoshopCity                  BINARY" +
                    ", photoshopState                 BINARY" +
                    ", photoshopCountry               BINARY" +
                    ", photoshopTransmissionReference BINARY" +
                    ", photoshopInstructions          BINARY" +
                    ", photoshopCredit                BINARY" +
                    ", photoshopSource                BINARY" +
                    ", rating                         BINARY" +
                    ", iptc4xmpcore_datecreated       BINARY" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_metadata_edit_templates_name ON metadata_edit_templates (name)");
        }
    }

    private void createFavoriteDirectoriesTable(Connection connection, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "favorite_directories")) {
            stmt.execute("CREATE CACHED TABLE favorite_directories" +
                    " (" +
                    "  favorite_name  VARCHAR_IGNORECASE(256)" +
                    ", directory_name VARCHAR(512)" +
                    ", favorite_index INTEGER" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_favorite_directories_favorite_name ON favorite_directories (favorite_name)");
        }
    }

    private void createFileExcludePatternTable(Connection connection, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "file_exclude_pattern")) {
            stmt.execute("CREATE CACHED TABLE file_exclude_pattern" +
                    " (" +
                    "pattern VARCHAR_IGNORECASE(256)" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_file_exclude_pattern_pattern ON file_exclude_pattern (pattern)");
        }
    }

    private void createProgramsTable(Connection connection, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "programs")) {
            stmt.execute("CREATE CACHED TABLE programs " +
                    " (" +
                    "  id                            BIGINT                  NOT NULL" +
                    ", action                        BOOLEAN" +
                    ", filename                      VARCHAR(512)            NOT NULL" +
                    ", alias                         VARCHAR_IGNORECASE(250) NOT NULL" +
                    ", parameters_before_filename    BINARY" +
                    ", parameters_after_filename     BINARY" +
                    ", input_before_execute          BOOLEAN" +
                    ", input_before_execute_per_file BOOLEAN" +
                    ", single_file_processing        BOOLEAN" +
                    ", change_file                   BOOLEAN" +
                    ", sequence_number               INTEGER" +
                    ", use_pattern                   BOOLEAN" +
                    ", pattern                       BINARY" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_programs_id              ON programs (id)");
            stmt.execute("CREATE INDEX        idx_programs_filename        ON programs (filename)");
            stmt.execute("CREATE INDEX        idx_programs_alias           ON programs (alias)");
            stmt.execute("CREATE INDEX        idx_programs_sequence_number ON programs (sequence_number)");
            stmt.execute("CREATE INDEX        idx_programs_action          ON programs (action)");
        }
    }

    private void createActionsAfterDbInsertionTable(Connection connection, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "actions_after_db_insertion")) {
            stmt.execute("CREATE CACHED TABLE actions_after_db_insertion " +
                    " (" +
                    "  id_programs  BIGINT  NOT NULL" +
                    ", action_order INTEGER" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_actions_after_db_insertion_id_programs  ON actions_after_db_insertion (id_programs)");
            stmt.execute("CREATE INDEX        idx_actions_after_db_insertion_action_order ON actions_after_db_insertion (action_order)");
        }
    }

    private void createHierarchicalSubjectsTable(Connection connection, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "hierarchical_subjects")) {
            stmt.execute("CREATE CACHED TABLE hierarchical_subjects " +
                    " (" +
                    "  id        BIGINT                 NOT NULL" +
                    ", id_parent BIGINT" +
                    ", subject   VARCHAR_IGNORECASE(64) NOT NULL" +
                    ", real      BOOLEAN" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_hierarchical_subjects_id        ON hierarchical_subjects (id)");
            stmt.execute("CREATE INDEX        idx_hierarchical_subjects_id_parent ON hierarchical_subjects (id_parent)");
            stmt.execute("CREATE INDEX        idx_hierarchical_subjects_subject   ON hierarchical_subjects (subject)");
            stmt.execute("CREATE INDEX        idx_hierarchical_subjects_real      ON hierarchical_subjects (real)");
        }
    }

    /**
     * Creates the table for internal application usage such as update
     * information etc.
     *
     * @param connection connection
     * @param stmt       sql statement
     * @throws           SQLException on sql errors
     */
    private void createAppTable(Connection connection, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "application")) {
            stmt.execute("CREATE CACHED TABLE application " +
                    " (" +
                    "  key   VARCHAR(128) PRIMARY KEY" +
                    ", value BINARY" +
                    ");");
        }
    }

    private void createSynonymsTable(Connection connection, Statement stmt) throws SQLException {
        if (!DatabaseMetadata.INSTANCE.existsTable(connection, "synonyms")) {
            stmt.execute("CREATE CACHED TABLE synonyms " +
                    " (" +
                    "  word VARCHAR(128)" +
                    ", synonym VARCHAR(128)" +
                    ", PRIMARY KEY (word, synonym)" +
                    ");");
            stmt.execute("CREATE UNIQUE INDEX idx_synonyms         ON synonyms (word, synonym)");
            stmt.execute("CREATE        INDEX idx_synonyms_word    ON synonyms (word)");
            stmt.execute("CREATE        INDEX idx_synonyms_synonym ON synonyms (synonym)");
        }
    }

    private void errorMessageSqlException(SQLException ex) {
        LongMessageDialog dlg = new LongMessageDialog(null, true);
        dlg.setTitle(Bundle.getString("DatabaseTables.Error.Title"));
        dlg.setMessage(getExceptionMessage(ex));
        dlg.setVisible(true);
    }

    private String getExceptionMessage(SQLException ex) {
        return Bundle.getString("DatabaseTables.Error", ex.getLocalizedMessage());
    }
}
