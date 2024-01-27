package com.zingkg.arkhamhorrorassistant.xml

import com.zingkg.secondedition.xml.ExhibitEncounter
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec

import java.io.StringReader

class ExhibitEncounterSpec : WordSpec() {
    init {
        "ExhibitEncounter.parseReader" should {
            "generate a exhibit encounter from valid xml" {
                val cards = ExhibitEncounter.parseReader(StringReader("<exhibit-encounter-cards expansion_set=\"miskatonic horror\"><!-- Exhibit Encounter 1 --><exhibit-encounter><title>A Curse on Thieves</title><entry>Someone has written hieroglyphics on the wall, warning of a curse upon those who disturb the Pharoah&apos;s tomb. If you are currently Cursed, make a Luck (+0) check. If you fail, you are devoured.</entry><location>Move the Ancient Whispers marker to the Uptown Street Location.</location></exhibit-encounter></exhibit-encounter-cards>"))
                cards shouldBe listOf(
                    ExhibitEncounter(
                        title = "A Curse on Thieves",
                        entry = "Someone has written hieroglyphics on the wall, warning of a curse upon those who disturb the Pharoah's tomb. If you are currently Cursed, make a Luck (+0) check. If you fail, you are devoured.",
                        location = "Move the Ancient Whispers marker to the Uptown Street Location.",
                        expansionSet = "miskatonic horror"
                    )
                )
            }

            "create an empty list if the xml is empty" {
                val cards = ExhibitEncounter.parseReader(StringReader(""))
                cards shouldBe emptyList<ExhibitEncounter>()
            }
        }
    }
}
