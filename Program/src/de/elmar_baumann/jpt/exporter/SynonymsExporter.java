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
package de.elmar_baumann.jpt.exporter;

import de.elmar_baumann.jpt.UserSettings;
import de.elmar_baumann.jpt.app.AppLogger;
import de.elmar_baumann.jpt.app.MessageDisplayer;
import de.elmar_baumann.jpt.database.DatabaseSynonyms;
import de.elmar_baumann.jpt.io.CharEncoding;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2010-02-07
 */
public final class SynonymsExporter extends AbstractAction {

    private static final long             serialVersionUID = 1L;
    public static final  SynonymsExporter INSTANCE         = new SynonymsExporter();
    public static final  String           DTD              = "synonyms.dtd";
    public static final  String           TAGNAME_ROOT     = "synonyms";
    public static final  String           TAGNAME_ENTRY    = "entry";
    public static final  String           TAGNAME_WORD     = "word";
    public static final  String           TAGNAME_SYNONYM  = "synonym";
    static final         String           FILENAME         = "synonyms.xml";

    @Override
    public void actionPerformed(ActionEvent e) {
        export();
    }

    public void export() {
        try {
            Document           doc   = getDoc();
            DOMSource          ds    = new DOMSource(doc);
            File               file  = getFile();
            StreamResult       sr    = new StreamResult(file);
            TransformerFactory tf    = TransformerFactory.newInstance();
            Transformer        trans = tf.newTransformer();

            insertSynonyms(doc);
            initTransformer(trans);
            trans.transform(ds, sr);
            MessageDisplayer.information(null, "SynonymsExporter.Info.Success", file);
        } catch (Exception ex) {
            AppLogger.logSevere(SynonymsExporter.class, ex);
        }
    }

    public File getFile() {
        return new File(
                UserSettings.INSTANCE.getSettingsDirectoryName() +
                File.separator +
                FILENAME);
    }

    private void insertSynonyms(Document doc) {
        Element rootElement = doc.createElement(TAGNAME_ROOT);

        doc.appendChild(rootElement);

        for (String word : DatabaseSynonyms.INSTANCE.getAllWords()) {
            Element entryElement = doc.createElement(TAGNAME_ENTRY);
            Element wordElement = doc.createElement(TAGNAME_WORD);
            wordElement.setTextContent(word);
            entryElement.appendChild(wordElement);
            for (String synonym : DatabaseSynonyms.INSTANCE.getSynonymsOf(word)) {
                Element synonymElement = doc.createElement(TAGNAME_SYNONYM);
                synonymElement.setTextContent(synonym);
                entryElement.appendChild(synonymElement);
            }
            rootElement.appendChild(entryElement);
        }

    }

    private Document getDoc() throws ParserConfigurationException {
        DocumentBuilderFactory factory     = DocumentBuilderFactory.newInstance();
        DocumentBuilder        builder     = factory.newDocumentBuilder();
        DOMImplementation      impl        = builder.getDOMImplementation();
        Document               doc         = impl.createDocument(null, null, null);

        return doc;
    }

    private void initTransformer(Transformer trans) throws IllegalArgumentException {
        trans.setOutputProperty(OutputKeys.METHOD, "xml");
        trans.setOutputProperty(OutputKeys.ENCODING, CharEncoding.JPT_KEYWORDS);
        trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, DTD);
        trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.setOutputProperty(OutputKeys.STANDALONE, "no");
    }

    private SynonymsExporter() {
    }
}