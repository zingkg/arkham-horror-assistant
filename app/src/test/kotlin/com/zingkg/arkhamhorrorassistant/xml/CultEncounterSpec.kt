package com.zingkg.arkhamhorrorassistant.xml

import com.zingkg.arkhamhorrorboard.secondedition.xml.CultEncounter
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec
import org.json.JSONObject

import java.io.StringReader

class CultEncounterSpec : WordSpec() {
    init {
        "CultEncounter.parseReader" should {
            "parse a valid cult encounter xml" {
                val cards = CultEncounter.parseReader(StringReader("<cult-encounter-cards expansion_set=\"the black goat of the woods\"><!-- Cult Encounter 1 --><cult-encounter><title>A Simple Choice</title><lore>&quot;These tools are for the collective good.&quot;</lore><entry>Draw 1 Common item, then either discard a different item or draw a Corruption card.</entry></cult-encounter></cult-encounter-cards>"))
                cards shouldBe listOf(
                    CultEncounter(
                        title = "A Simple Choice",
                        lore = "\"These tools are for the collective good.\"",
                        entry = "Draw 1 Common item, then either discard a different item or draw a Corruption card.",
                        expansionSet = "the black goat of the woods"
                    )
                )
            }

            "return an empty list if the xml is blank" {
                val cards = CultEncounter.parseReader(StringReader(""))
                cards shouldBe emptyList<CultEncounter>()
            }
        }
    }
}
