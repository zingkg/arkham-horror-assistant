package com.zingkg.arkhamhorrorassistant.xml

import com.zingkg.secondedition.xml.InnsmouthLook
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec

import java.io.StringReader

class InnsmouthLookSpec : WordSpec() {
    init {
        "InnsmouthLook.parseReader" should {
            "generate an innsmouth look with valid xml" {
                val cards = InnsmouthLook.parseReader(StringReader("<innsmouth-look-cards expansion_set=\"innsmouth horror\"><!-- Innsmouth 1 --><innsmouth-look><lore>Ia-R'lyehl Cihuiha flgagnl id Ia!</lore><entry>Search the monster cup for a Deep One and place it in your location, then add an uprising token to the Deep Ones Rising track. You are devoured.</entry></innsmouth-look></innsmouth-look-cards>"))
                cards shouldBe listOf(
                    InnsmouthLook(
                        lore = "Ia-R'lyehl Cihuiha flgagnl id Ia!",
                        entry = "Search the monster cup for a Deep One and place it in your location, then add an uprising token to the Deep Ones Rising track. You are devoured.",
                        expansionSet = "innsmouth horror"
                    )
                )
            }

            "generate an empty list with empty xml" {
                val cards = InnsmouthLook.parseReader(StringReader(""))
                cards shouldBe emptyList<InnsmouthLook>()
            }
        }
    }
}
