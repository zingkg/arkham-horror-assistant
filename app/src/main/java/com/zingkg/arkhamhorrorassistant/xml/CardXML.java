package com.zingkg.arkhamhorrorassistant.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CardXML {
    public final String expansionSet;

    CardXML(String expansionSet) {
        this.expansionSet = expansionSet;
    }

    public static String MISKATONIC = "miskatonic horror";

    public static String getAttribute(XmlPullParser xpp, String name) {
        final int attributeCount = xpp.getAttributeCount();
        String value = "";
        for (int i = 0; i < attributeCount; i++) {
            if (xpp.getAttributeName(i).equals(name))
                value = xpp.getAttributeValue(i);
        }
        return value;
    }

    public static boolean isEnd(int eventType, String xppName, String endingKey) {
        return eventType == XmlPullParser.END_TAG && (xppName == null || xppName.equals(endingKey));
    }

    public static boolean isXMLText(int eventType, String previousKey, String key) {
        return (
            eventType == XmlPullParser.TEXT || eventType == XmlPullParser.START_TAG
        ) && previousKey.equals(key);
    }

    public static String getXMLText(
        XmlPullParser xpp,
        String startTag
    ) throws XmlPullParserException, IOException {
        StringBuilder sb = new StringBuilder();
        int eventType = xpp.getEventType();
        while (
            eventType != XmlPullParser.END_TAG || (xpp.getName() != null &&
            !xpp.getName().equals(startTag))
        ) {
            if (eventType == XmlPullParser.TEXT)
                sb.append(xpp.getText());
            else if (eventType == XmlPullParser.START_TAG && !xpp.getName().equals(startTag))
                sb.append(digXMLText(xpp, xpp.getName()));

            eventType = xpp.next();
        }
        return sb.toString();
    }

    private static String digXMLText(
        XmlPullParser xpp,
        String startTag
    ) throws XmlPullParserException, IOException {
        StringBuilder sb = new StringBuilder();
        int eventType = xpp.getEventType();
        sb.append('<').append(startTag).append('>');
        while (
            eventType != XmlPullParser.END_TAG || (
                xpp.getName() != null && !xpp.getName().equals(startTag)
            )
        ) {
            if (eventType == XmlPullParser.START_TAG && !xpp.getName().equals(startTag)) {
                // Dig into this.
                sb.append(digXMLText(xpp, xpp.getName()));
            } else if (eventType == XmlPullParser.TEXT) {
                sb.append(xpp.getText());
            }
            eventType = xpp.next();
        }
        sb.append("</").append(startTag).append('>');
        return sb.toString();
    }

    public static List<String> getStringList(
        XmlPullParser xpp,
        String startTag
    ) throws XmlPullParserException, IOException {
        List<String> strings = new ArrayList<>();
        int eventType = xpp.getEventType();
        boolean hitStartTag = false;
        while (
            eventType != XmlPullParser.END_TAG || (
                xpp.getName() != null && !xpp.getName().equals(startTag)
            )
        ) {
            if (eventType == XmlPullParser.START_TAG) {
                hitStartTag = true;
            } else if (eventType == XmlPullParser.TEXT && hitStartTag) {
                strings.add(xpp.getText());
                hitStartTag = false;
            }
            eventType = xpp.next();
        }
        return strings;
    }
}
