package com.zingkg.arkhamhorrorassistant.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * A InnsmouthLook object that binds to the text of a Innsmouth Look card.
 */
public class InnsmouthLook extends CardXML {
    public final String lore;
    public final String entry;

    public InnsmouthLook(String lore, String entry, String expansionSet) {
        super(expansionSet);
        this.lore = lore;
        this.entry = entry;
    }

    public static final String BASE = "innsmouth horror";
    private static final String innsmouthLookCard = "innsmouth-look";

    /**
     * Parses the stream to retrieve a list of Innsmouth Look cards.
     *
     * @param reader
     *     The reader on the file containing Innsmouth Look cards.
     * @return A list of InnsmouthLook cards.
     */
    public static List<InnsmouthLook> parseFile(Reader reader) {
        List<InnsmouthLook> innsmouthLookCards = new ArrayList<>();
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
                    xpp.getName().equals("innsmouth-look-cards") &&
                    expansionSet.isEmpty()
                ) {
                    expansionSet = getAttribute(xpp, "expansionSet");
                }

                if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(innsmouthLookCard))
                    innsmouthLookCards.add(parseXML(xpp, expansionSet));

                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return innsmouthLookCards;
    }

    /**
     * Parses the specific XML element that contains a Innsmouth Look card.
     *
     * @param xpp
     *     The XmlPullParser instantiated with a Innsmouth Look document as well as currently on a
     *     Innsmouth Look card.
     * @param expansionSet
     *     The expansion set of the Innsmouth Look card. Typically this is the Innsmouth Horror or
     *     Miskatonic.
     * @return A InnsmouthLook object.
     */
    private static InnsmouthLook parseXML(
        XmlPullParser xpp,
        String type
    ) throws XmlPullParserException, IOException {
        String previousKey = "", lore = "", entry = "";
        int eventType = xpp.next();
        while (!isEnd(eventType, xpp.getName(), innsmouthLookCard)) {
            if (isXMLText(eventType, previousKey, "lore")) {
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
        return new InnsmouthLook(lore, entry, type);
    }
}
