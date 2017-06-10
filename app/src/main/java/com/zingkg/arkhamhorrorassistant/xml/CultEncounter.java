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
 * A CultEncounter object that binds to the text of a Cult Encounter card.
 */
public class CultEncounter extends CardXML {
    public final String title;
    public final String lore;
    public final String entry;

    public CultEncounter(String title, String lore, String entry, String expansionSet) {
        super(expansionSet);
        this.title = title;
        this.lore = lore;
        this.entry = entry;
    }

    public static final String BASE = "the black goat of the woods";
    private static final String cultEncounterCard = "cult-encounter";

    /**
     * Parses the stream to retrieve a list of Cult Encounters.
     *
     * @param reader
     *     The reader containing Cult Encounters.
     * @return A list of CultEncounter cards.
     */
    public static List<CultEncounter> parseReader(Reader reader) {
        List<CultEncounter> cards = new ArrayList<>();
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
                    xpp.getName().equals("cult-encounter-cards") &&
                    expansionSet.isEmpty()
                ) {
                    expansionSet = getAttribute(xpp, "expansion_set");
                }

                if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(cultEncounterCard))
                    cards.add(parseXML(xpp, expansionSet));

                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            System.err.println("Error parsing reader for cult encounters " + e);
        }
        return Collections.unmodifiableList(cards);
    }

    /**
     * Parses the specific XML element that contains a Cult Encounter.
     *
     * @param xpp
     *     The XmlPullParser instantiated with a Cult Encounter document as well as currently on a
     *     Cult Encounter.
     * @param expansionSet
     *     The expansion set of the Cult Encounter. Typically this is The Black Goat of the Woods or
     *     Miskatonic.
     * @return A CultEncounter object.
     */
    private static CultEncounter parseXML(
        XmlPullParser xpp,
        String expansionSet
    ) throws XmlPullParserException, IOException {
        String previousKey = "", title = "", lore = "", entry = "";
        int eventType = xpp.next();
        while (!isEnd(eventType, xpp.getName(), cultEncounterCard)) {
            if (eventType == XmlPullParser.TEXT && previousKey.equals("title")) {
                title = xpp.getText().trim();
                previousKey = "";
            } else if (isXMLText(eventType, previousKey, "lore")) {
                lore = getXMLText(xpp, previousKey).trim();
                previousKey = "";
            } else if (isXMLText(eventType, previousKey, "entry")) {
                entry = getXMLText(xpp, previousKey).trim();
                previousKey = "";
            } else if (eventType == XmlPullParser.START_TAG) {
                previousKey = xpp.getName();
            }
            eventType = xpp.next();
        }
        return new CultEncounter(title, lore, entry, expansionSet);
    }
}
