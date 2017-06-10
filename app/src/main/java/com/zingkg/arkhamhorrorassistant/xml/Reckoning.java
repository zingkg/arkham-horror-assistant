package com.zingkg.arkhamhorrorassistant.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Reckoning object that binds to the text of a Reckoning card.
 */
public class Reckoning extends CardXML {
    public final String title;
    public final String entry;

    public Reckoning(String title, String entry, String expansionSet) {
        super(expansionSet);
        this.title = title;
        this.entry = entry;
    }

    public static final String BASE = "the lurker at the threshold";
    private static final String reckoningCard = "reckoning";

    /**
     * Parses the stream to retrieve a list of Reckoning cards.
     *
     * @param reader
     *     The reader on the file containing Reckoning Encounters.
     * @return A list of Reckoning cards.
     */
    public static List<Reckoning> parseReader(Reader reader) {
        List<Reckoning> reckoningCards = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(reader);
            int eventType = xpp.getEventType();
            String expansionSet = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (
                    eventType == XmlPullParser.START_TAG &&
                    xpp.getName().equals("reckoning-cards") &&
                    expansionSet.isEmpty()
                ) {
                    expansionSet = getAttribute(xpp, "expansion_set");
                }

                if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(reckoningCard))
                    reckoningCards.add(parseXML(xpp, expansionSet));

                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            System.err.println("Error parsing reader for reckoning " + e);
        }
        return Collections.unmodifiableList(reckoningCards);
    }

    /**
     * Parses the specific XML element that contains a Reckoning card.
     *
     * @param xpp
     *     The XmlPullParser instantiated with a Reckoning document as well as currently on a
     *     Reckoning card.
     * @param expansionSet
     *     The expansion set of the Reckoning card. Typically this is the Lurker at the Threshold or
     *     Miskatonic.
     * @return A Reckoning object.
     */
    private static Reckoning parseXML(
        XmlPullParser xpp,
        String expansionSet
    ) throws XmlPullParserException, IOException {
        String previousKey = "", title = "", entry = "";
        int eventType = xpp.next();
        while (!isEnd(eventType, xpp.getName(), reckoningCard)) {
            if (isXMLText(eventType, previousKey, "title")) {
                title = getXMLText(xpp, previousKey).trim();
                previousKey = "";
            } else if (isXMLText(eventType, previousKey, "entry")) {
                entry = getXMLText(xpp, previousKey).trim();
                previousKey = "";
            } else if (eventType == XmlPullParser.START_TAG) {
                previousKey = xpp.getName();
            }
            eventType = xpp.next();
        }
        return new Reckoning(title, entry, expansionSet);
    }
}
