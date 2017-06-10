package com.zingkg.arkhamhorrorassistant.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An abstract class meant to be extended by every Card class.
 */
public abstract class CardXML {
    public final String expansionSet;

    CardXML(String expansionSet) {
        this.expansionSet = expansionSet;
    }

    public static String MISKATONIC = "miskatonic horror";

    /**
     * Retrieves the attribute with the given name. If it does not exist it will return an empty
     * string.
     *
     * @param xpp
     *     The XML pull parser instantiated with a XML document.
     * @param name
     *     The key name of the attribute.
     * @return The value of the attribute key.
     */
    public static String getAttribute(XmlPullParser xpp, String name) {
        final int attributeCount = xpp.getAttributeCount();
        String value = "";
        for (int i = 0; i < attributeCount; i++) {
            if (xpp.getAttributeName(i).equals(name))
                value = xpp.getAttributeValue(i);
        }
        return value;
    }

    /**
     * Checks if the parser has reached the end of the tags.
     *
     * @param eventType
     *     The XmlPullParser event type.
     * @param xppName
     *     The name of the current tag the XmlPullParser is on.
     * @param endingKey
     *     The key that the parser should end on.
     * @return True if the parser has reached the end. False if the parser has not.
     */
    public static boolean isEnd(int eventType, String xppName, String endingKey) {
        return eventType == XmlPullParser.END_TAG && (xppName == null || xppName.equals(endingKey));
    }

    /**
     * Checks if the XML contains text or other XML elements.
     *
     * @param eventType
     *     The XmlPullParser event type.
     * @param previousKey
     *     The previous key the XmlPullParser encountered.
     * @param key
     *     The currentKey the XmlPullParser is on.
     * @return True if the XML contains text. False otherwise.
     */
    public static boolean isXMLText(int eventType, String previousKey, String key) {
        return (
            eventType == XmlPullParser.TEXT || eventType == XmlPullParser.START_TAG
        ) && previousKey.equals(key);
    }

    /**
     * Gets all of the text associated in the XML.
     *
     * @param xpp
     *     The XmlPullParser instantiated with a XML document.
     * @param startTag
     *     The starting XML tag.
     * @return All of the text or elements inside of the XML tag.
     */
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

    /**
     * Digs into a XML element to retrieve all of the information inside.
     *
     * @param xpp
     *     The XmlPullParser instantiated with a XML document.
     * @param startTag
     *     The starting XML tag.
     * @return The information inside of the element.
     */
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

    /**
     * Retrieves a list of all the strings matching the tag.
     *
     * @param xpp
     *     The XmlPullParser instantiated with a XML document.
     * @param startTag
     *     The starting tag to retrieve strings.
     * @return A list of strings.
     */
    public static List<String> getStringList(
        XmlPullParser xpp,
        String startTag
    ) throws XmlPullParserException, IOException {
        List<String> strings = new ArrayList<>();
        int eventType = xpp.getEventType();
        boolean hitStartTag = false;
        String name = xpp.getName();
        while (eventType != XmlPullParser.END_TAG || (name != null && name.equals(startTag))) {
            if (eventType == XmlPullParser.START_TAG) {
                hitStartTag = true;
            } else if (eventType == XmlPullParser.TEXT && hitStartTag) {
                strings.add(xpp.getText());
                hitStartTag = false;
            }
            eventType = xpp.next();
            name = xpp.getName();
        }
        return Collections.unmodifiableList(strings);
    }
}
