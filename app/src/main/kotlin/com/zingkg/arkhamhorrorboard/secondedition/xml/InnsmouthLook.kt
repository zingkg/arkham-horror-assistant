package com.zingkg.arkhamhorrorboard.secondedition.xml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

import java.io.Reader
import java.util.ArrayList

/**
 * A InnsmouthLook object that binds to the text of a Innsmouth Look card.
 */
data class InnsmouthLook(
    val lore: String,
    val entry: String,
    override val expansionSet: String
) : CardXML(expansionSet) {
    companion object {
        val BASE = "innsmouth horror"
        private val innsmouthLookCard = "innsmouth-look"

        /**
         * Parses the stream to retrieve a list of Innsmouth Look cards.
         *
         * @param reader
         * The reader on the file containing Innsmouth Look cards.
         * @return A list of InnsmouthLook cards.
         */
        fun parseReader(reader: Reader): List<InnsmouthLook> {
            val innsmouthLookCards = ArrayList<InnsmouthLook>()
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(reader)
            var eventType = xpp.eventType
            var expansionSet = ""
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG &&
                        xpp.name == "innsmouth-look-cards" &&
                        expansionSet.isEmpty()) {
                    expansionSet = getAttribute(xpp, "expansion_set")
                }

                if (eventType == XmlPullParser.START_TAG && xpp.name == innsmouthLookCard)
                    innsmouthLookCards.add(parseXML(xpp, expansionSet))

                eventType = xpp.next()
            }
            return innsmouthLookCards.toList()
        }

        /**
         * Parses the specific XML element that contains a Innsmouth Look card.
         *
         * @param xpp
         * The XmlPullParser instantiated with a Innsmouth Look document as well as currently on a
         * Innsmouth Look card.
         * @param expansionSet
         * The expansion set of the Innsmouth Look card. Typically this is the Innsmouth Horror or
         * Miskatonic.
         * @return A InnsmouthLook object.
         */
        private fun parseXML(
            xpp: XmlPullParser,
            type: String
        ): InnsmouthLook {
            var previousKey = ""
            var lore = ""
            var entry = ""
            var eventType = xpp.next()
            while (!isEnd(eventType, xpp.name, innsmouthLookCard)) {
                if (isXMLText(eventType, previousKey, "lore")) {
                    lore = getXMLText(xpp, previousKey).trim()
                    previousKey = ""
                } else if (isXMLText(eventType, previousKey, "entry")) {
                    entry = getXMLText(xpp, previousKey).trim()
                    previousKey = ""
                } else if (eventType == XmlPullParser.START_TAG) {
                    previousKey = xpp.name
                }
                eventType = xpp.next()
            }
            return InnsmouthLook(lore, entry, type)
        }
    }
}
