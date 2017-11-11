package com.zingkg.arkhamhorrorassistant.xml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

import java.io.Reader
import java.util.ArrayList

/**
 * An ExhibitEncounter object that binds to the text of a Exhibit Encounter card.
 */
data class ExhibitEncounter(
    val title: String,
    val entry: String,
    val location: String,
    override val expansionSet: String
) : CardXML(expansionSet) {
    companion object {

        val BASE = "the curse of the dark pharoah"
        private val exhibitEncounterCard = "exhibit-encounter"

        /**
         * Parses the stream to retrieve a list of Exhibit Encounters.
         *
         * @param reader
         * The reader on the file containing Exhibit Encounters.
         * @return A list of ExhibitEncounter cards.
         */
        fun parseReader(reader: Reader): List<ExhibitEncounter> {
            val cards = ArrayList<ExhibitEncounter>()
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(reader)
            var eventType = xpp.eventType
            var expansionSet = ""
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG &&
                        xpp.name == "exhibit-encounter-cards" &&
                        expansionSet.isEmpty()) {
                    expansionSet = getAttribute(xpp, "expansion_set")
                }

                if (eventType == XmlPullParser.START_TAG && xpp.name == exhibitEncounterCard) {
                    cards.add(parseXML(xpp, expansionSet))
                }

                eventType = xpp.next()
            }
            return cards.toList()
        }

        /**
         * Parses the specific XML element that contains a Exhibit Encounter.
         *
         * @param xpp
         * The XmlPullParser instantiated with a Exhibit Encounter document as well as currently on
         * a Exhibit Encounter.
         * @param expansionSet
         * The expansion set of the Exhibit Encounter. Typically this is The Curse of the Dark
         * Pharoah or Miskatonic.
         * @return A ExhibitEncounter object.
         */
        private fun parseXML(
            xpp: XmlPullParser,
            expansionSet: String
        ): ExhibitEncounter {
            var previousKey = ""
            var title = ""
            var entry = ""
            var location = ""
            var eventType = xpp.next()
            while (!isEnd(eventType, xpp.name, exhibitEncounterCard)) {
                if (eventType == XmlPullParser.TEXT && previousKey == "title") {
                    title = xpp.text.trim { it <= ' ' }
                    previousKey = ""
                } else if (isXMLText(eventType, previousKey, "entry")) {
                    entry = getXMLText(xpp, previousKey).trim()
                    previousKey = ""
                } else if (isXMLText(eventType, previousKey, "location")) {
                    location = getXMLText(xpp, previousKey).trim()
                    previousKey = ""
                } else if (eventType == XmlPullParser.START_TAG) {
                    previousKey = xpp.name
                }
                eventType = xpp.next()
            }
            return ExhibitEncounter(title, entry, location, expansionSet)
        }
    }
}
