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
 * An ExhibitEncounter object that binds to the text of a Exhibit Encounter card.
 */
public class ExhibitEncounter extends CardXML {
    public final String title;
    public final String entry;
    public final String location;

    public ExhibitEncounter(String title, String entry, String location, String expansionSet) {
        super(expansionSet);
        this.title = title;
        this.entry = entry;
        this.location = location;
    }

    public static final String BASE = "the curse of the dark pharoah";
    private static final String exhibitEncounterCard = "exhibit-encounter";

    /**
     * Parses the stream to retrieve a list of Exhibit Encounters.
     *
     * @param reader
     *     The reader on the file containing Exhibit Encounters.
     * @return A list of ExhibitEncounter cards.
     */
    public static List<ExhibitEncounter> parseReader(Reader reader) {
        List<ExhibitEncounter> cards = new ArrayList<>();
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
                    xpp.getName().equals("exhibit-encounter-cards") &&
                    expansionSet.isEmpty()
                ) {
                    expansionSet = getAttribute(xpp, "expansion_set");
                }

                if (
                    eventType == XmlPullParser.START_TAG &&
                    xpp.getName().equals(exhibitEncounterCard)
                ) {
                    cards.add(parseXML(xpp, expansionSet));
                }

                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            System.err.println("Error parsing reader for exhibit encounter " + e);
        }
        return Collections.unmodifiableList(cards);
    }

    /**
     * Parses the specific XML element that contains a Exhibit Encounter.
     *
     * @param xpp
     *     The XmlPullParser instantiated with a Exhibit Encounter document as well as currently on
     *     a Exhibit Encounter.
     * @param expansionSet
     *     The expansion set of the Exhibit Encounter. Typically this is The Curse of the Dark
     *     Pharoah or Miskatonic.
     * @return A ExhibitEncounter object.
     */
    private static ExhibitEncounter parseXML(
        XmlPullParser xpp,
        String expansionSet
    ) throws XmlPullParserException, IOException {
        String previousKey = "", title = "", entry = "", location = "";
        int eventType = xpp.next();
        while (!isEnd(eventType, xpp.getName(), exhibitEncounterCard)) {
            if (eventType == XmlPullParser.TEXT && previousKey.equals("title")) {
                title = xpp.getText().trim();
                previousKey = "";
            } else if (isXMLText(eventType, previousKey, "entry")) {
                entry = getXMLText(xpp, previousKey).trim();
                previousKey = "";
            } else if (isXMLText(eventType, previousKey, "location")) {
                location = getXMLText(xpp, previousKey).trim();
                previousKey = "";
            } else if (eventType == XmlPullParser.START_TAG) {
                previousKey = xpp.getName();
            }
            eventType = xpp.next();
        }
        return new ExhibitEncounter(title, entry, location, expansionSet);
    }
}
