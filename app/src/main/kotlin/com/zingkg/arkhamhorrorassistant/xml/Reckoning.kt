package com.zingkg.arkhamhorrorassistant.xml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

import java.io.Reader
import java.util.ArrayList

/**
 * A Reckoning object that binds to the text of a Reckoning card.
 */
data class Reckoning(
    val title: String,
    val entry: String,
    override val expansionSet: String
) : CardXML(expansionSet) {
    companion object {
        val BASE = "the lurker at the threshold"
        private val reckoningCard = "reckoning"

        /**
         * Parses the stream to retrieve a list of Reckoning cards.
         *
         * @param reader
         * The reader on the file containing Reckoning Encounters.
         * @return A list of Reckoning cards.
         */
        fun parseReader(reader: Reader): List<Reckoning> {
            val reckoningCards = ArrayList<Reckoning>()
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(reader)
            var eventType = xpp.eventType
            var expansionSet = ""
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG &&
                        xpp.name == "reckoning-cards" &&
                        expansionSet.isEmpty()) {
                    expansionSet = getAttribute(xpp, "expansion_set")
                }

                if (eventType == XmlPullParser.START_TAG && xpp.name == reckoningCard)
                    reckoningCards.add(parseXML(xpp, expansionSet))

                eventType = xpp.next()
            }
            return reckoningCards.toList()
        }

        /**
         * Parses the specific XML element that contains a Reckoning card.
         *
         * @param xpp
         * The XmlPullParser instantiated with a Reckoning document as well as currently on a
         * Reckoning card.
         * @param expansionSet
         * The expansion set of the Reckoning card. Typically this is the Lurker at the Threshold or
         * Miskatonic.
         * @return A Reckoning object.
         */
        private fun parseXML(
            xpp: XmlPullParser,
            expansionSet: String
        ): Reckoning {
            var previousKey = ""
            var title = ""
            var entry = ""
            var eventType = xpp.next()
            while (!isEnd(eventType, xpp.name, reckoningCard)) {
                if (isXMLText(eventType, previousKey, "title")) {
                    title = getXMLText(xpp, previousKey).trim()
                    previousKey = ""
                } else if (isXMLText(eventType, previousKey, "entry")) {
                    entry = getXMLText(xpp, previousKey).trim()
                    previousKey = ""
                } else if (eventType == XmlPullParser.START_TAG) {
                    previousKey = xpp.name
                }
                eventType = xpp.next()
            }
            return Reckoning(title, entry, expansionSet)
        }
    }
}
