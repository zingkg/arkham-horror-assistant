package com.zingkg.arkhamhorrorassistant.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class InnsmouthLook extends CardXML {
    public final String mLore;
    public final String mEntry;

    public InnsmouthLook(String lore, String entry, String type) {
        super(type);
        mLore = lore;
        mEntry = entry;
    }

    public static final String BASE = "innsmouth horror";
    private static final String innsmouthLookCard = "innsmouth-look";

    public static List<InnsmouthLook> parseFile(Reader reader) {
        List<InnsmouthLook> innsmouthLookCards = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(reader);
            int eventType = xpp.getEventType();
            String type = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (
                    eventType == XmlPullParser.START_TAG &&
                    xpp.getName().equals("innsmouth-look-cards") &&
                    type.isEmpty()
                ) {
                    type = getAttribute(xpp, "type");
                }

                if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(innsmouthLookCard))
                    innsmouthLookCards.add(parseXML(xpp, type));

                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return innsmouthLookCards;
    }

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
