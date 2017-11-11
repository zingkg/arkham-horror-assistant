package com.zingkg.arkhamhorrorassistant.xml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.ArrayList

abstract class CardXML(open val expansionSet: String) {
    companion object {
        val MISKATONIC = "miskatonic horror"

        /**
         * Retrieves the attribute with the given name. If it does not exist it will return an empty
         * string.
         *
         * @param xpp
         * The XML pull parser instantiated with a XML document.
         * @param name
         * The key name of the attribute.
         * @return The value of the attribute key.
         */
        fun getAttribute(xpp: XmlPullParser, name: String): String {
            val attributeCount = xpp.attributeCount
            var value = ""
            (0 until attributeCount)
                .filter { xpp.getAttributeName(it) == name }
                .forEach { value = xpp.getAttributeValue(it) }
            return value
        }

        /**
         * Checks if the parser has reached the end of the tags.
         *
         * @param eventType
         * The XmlPullParser event type.
         * @param xppName
         * The name of the current tag the XmlPullParser is on.
         * @param endingKey
         * The key that the parser should end on.
         * @return True if the parser has reached the end. False if the parser has not.
         */
        fun isEnd(eventType: Int, xppName: String?, endingKey: String): Boolean {
            return eventType == XmlPullParser.END_TAG && (xppName == null || xppName == endingKey)
        }

        /**
         * Checks if the XML contains text or other XML elements.
         *
         * @param eventType
         * The XmlPullParser event type.
         * @param previousKey
         * The previous key the XmlPullParser encountered.
         * @param key
         * The currentKey the XmlPullParser is on.
         * @return True if the XML contains text. False otherwise.
         */
        fun isXMLText(eventType: Int, previousKey: String, key: String): Boolean {
            return (eventType == XmlPullParser.TEXT || eventType == XmlPullParser.START_TAG) && previousKey == key
        }

        /**
         * Gets all of the text associated in the XML.
         *
         * @param xpp
         * The XmlPullParser instantiated with a XML document.
         * @param startTag
         * The starting XML tag.
         * @return All of the text or elements inside of the XML tag.
         */
        @Throws(XmlPullParserException::class, IOException::class)
        fun getXMLText(
            xpp: XmlPullParser,
            startTag: String
        ): String {
            val sb = StringBuilder()
            var eventType = xpp.eventType
            while (eventType != XmlPullParser.END_TAG || xpp.name != null && xpp.name != startTag) {
                if (eventType == XmlPullParser.TEXT)
                    sb.append(xpp.text)
                else if (eventType == XmlPullParser.START_TAG && xpp.name != startTag)
                    sb.append(digXMLText(xpp, xpp.name))

                eventType = xpp.next()
            }
            return sb.toString()
        }

        /**
         * Digs into a XML element to retrieve all of the information inside.
         *
         * @param xpp
         * The XmlPullParser instantiated with a XML document.
         * @param startTag
         * The starting XML tag.
         * @return The information inside of the element.
         */
        private fun digXMLText(
            xpp: XmlPullParser,
            startTag: String
        ): String {
            val sb = StringBuilder()
            var eventType = xpp.eventType
            sb.append('<').append(startTag).append('>')
            while (eventType != XmlPullParser.END_TAG || xpp.name != null && xpp.name != startTag) {
                if (eventType == XmlPullParser.START_TAG && xpp.name != startTag) {
                    // Dig into this.
                    sb.append(digXMLText(xpp, xpp.name))
                } else if (eventType == XmlPullParser.TEXT) {
                    sb.append(xpp.text)
                }
                eventType = xpp.next()
            }
            sb.append("</").append(startTag).append('>')
            return sb.toString()
        }

        /**
         * Retrieves a list of all the strings matching the tag.
         *
         * @param xpp
         * The XmlPullParser instantuated with a XML document.
         * @param startTag
         * The starting tag to retrieve strings.
         * @return A list of strings.
         */
        fun getStringList(
            xpp: XmlPullParser,
            startTag: String
        ): List<String> {
            val strings = ArrayList<String>()
            var eventType = xpp.eventType
            var hitStartTag = false
            while (eventType != XmlPullParser.END_TAG || xpp.name != null && xpp.name != startTag) {
                if (eventType == XmlPullParser.START_TAG) {
                    hitStartTag = true
                } else if (eventType == XmlPullParser.TEXT && hitStartTag) {
                    strings.add(xpp.text)
                    hitStartTag = false
                }
                eventType = xpp.next()
            }
            return strings
        }
    }
}