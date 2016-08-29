package com.zingkg.arkhamhorrorassistant.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Reckoning> parseFile(Reader reader) {
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
                    expansionSet = getAttribute(xpp, "expansionSet");
                }

                if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(reckoningCard))
                    reckoningCards.add(parseXML(xpp, expansionSet));

                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return reckoningCards;
    }

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
