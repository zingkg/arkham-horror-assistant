package com.zingkg.arkhamhorrorassistant.xml

import com.zingkg.secondedition.xml.Reckoning
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec

import java.io.StringReader

class ReckoningSpec : WordSpec() {
    init {
        "Reckoning.parseReader" should {
            "generate a reckoning with valid xml" {
                val cards = Reckoning.parseReader(StringReader("<reckoning-cards expansion_set=\"the lurker at the threshold\"><!-- Reckoning 1 --><reckoning><title>A Humble Servant</title><entry>Add 1 doom token to the doom track for each investigator with 3 Dark Pacts.</entry></reckoning></reckoning-cards>"))
                cards shouldBe listOf(
                    Reckoning(
                        title = "A Humble Servant",
                        entry = "Add 1 doom token to the doom track for each investigator with 3 Dark Pacts.",
                        expansionSet = "the lurker at the threshold"
                    )
                )
            }

            "generate an empty list if the xml is empty" {
                val cards = Reckoning.parseReader(StringReader(""))
                cards shouldBe emptyList<Reckoning>()
            }
        }
    }
}
