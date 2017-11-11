package com.zingkg.arkhamhorrorassistant.xml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

import java.io.Reader
import java.util.ArrayList

/**
 * A CultEncounter object that binds to the text of a Cult Encounter card.
 */
data class CultEncounter(
    val title: String,
    val lore: String,
    val entry: String,
    override val expansionSet: String
) : CardXML(expansionSet) {
    companion object {

        val BASE = "the black goat of the woods"
        private val cultEncounterCard = "cult-encounter"

        /**
         * Parses the stream to retrieve a list of Cult Encounters.
         *
         * @param reader
         * The reader on the file containing Cult Encounters.
         * @return A list of CultEncounter cards.
         */
        fun parseReader(reader: Reader): List<CultEncounter> {
            val cards = ArrayList<CultEncounter>()
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(reader)
            var eventType = xpp.eventType
            var expansionSet = ""
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG &&
                        xpp.name == "cult-encounter-cards" &&
                        expansionSet.isEmpty()) {
                    expansionSet = getAttribute(xpp, "expansion_set")
                }

                if (eventType == XmlPullParser.START_TAG && xpp.name == cultEncounterCard)
                    cards.add(parseXML(xpp, expansionSet))

                eventType = xpp.next()
            }
            return cards.toList()
        }

        /**
         * Parses the specific XML element that contains a Cult Encounter.
         *
         * @param xpp
         * The XmlPullParser instantiated with a Cult Encounter document as well as currently on a
         * Cult Encounter.
         * @param expansionSet
         * The expansion set of the Cult Encounter. Typically this is The Black Goat of the Woods or
         * Miskatonic.
         * @return A CultEncounter object.
         */
        private fun parseXML(
            xpp: XmlPullParser,
            expansionSet: String
        ): CultEncounter {
            var previousKey = ""
            var title = ""
            var lore = ""
            var entry = ""
            var eventType = xpp.next()
            while (!isEnd(eventType, xpp.name, cultEncounterCard)) {
                if (eventType == XmlPullParser.TEXT && previousKey == "title") {
                    title = xpp.text.trim { it <= ' ' }
                    previousKey = ""
                } else if (isXMLText(eventType, previousKey, "lore")) {
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
            return CultEncounter(title, lore, entry, expansionSet)
        }
    }
}
