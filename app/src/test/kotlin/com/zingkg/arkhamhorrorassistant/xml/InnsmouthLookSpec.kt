package com.zingkg.arkhamhorrorassistant.xml

import com.zingkg.arkhamhorrorboard.secondedition.xml.InnsmouthLook
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec
import org.json.JSONObject

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

            "expansion" {
                val cards = InnsmouthLook.parseReader(StringReader("""<innsmouth-look-cards expansionSet="innsmouth horror">
    <!-- Innsmouth 1 -->
    <innsmouth-look>
        <lore>
            <i>Ia-R'lyehl Cihuiha flgagnl id Ia!</i>
        </lore>
        <entry>
            Search the monster cup for a Deep One and place it in your location, then add an uprising token to the Deep Ones Rising track. You are devoured.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 2 -->
    <innsmouth-look>
        <lore>
            <i>That night, I dreamed. Great watery spaces opened out before me, and I seemed to wander through titanic sunken porticos and labyrinths of weedy cyclopean walls with grotesque fishes as my companions.</i>
        </lore>
        <entry>
            You do not have the Innsmouth Look. Nothing happens.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 3 -->
    <innsmouth-look>
        <lore>
            <i>That night, I dreamed. Great watery spaces opened out before me, and I seemed to wander through titanic sunken porticos and labyrinths of weedy cyclopean walls with grotesque fishes as my companions.</i>
        </lore>
        <entry>
            You do not have the Innsmouth Look. Nothing happens.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 4 -->
    <innsmouth-look>
        <lore>
            <i>That night, I dreamed. Great watery spaces opened out before me, and I seemed to wander through titanic sunken porticos and labyrinths of weedy cyclopean walls with grotesque fishes as my companions.</i>
        </lore>
        <entry>
            You do not have the Innsmouth Look. Nothing happens.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 5 -->
    <innsmouth-look>
        <lore>
            <i>That night, I dreamed. Great watery spaces opened out before me, and I seemed to wander through titanic sunken porticos and labyrinths of weedy cyclopean walls with grotesque fishes as my companions.</i>
        </lore>
        <entry>
            You do not have the Innsmouth Look. Nothing happens.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 6 -->
    <innsmouth-look>
        <lore>
            <i>That night, I dreamed. Great watery spaces opened out before me, and I seemed to wander through titanic sunken porticos and labyrinths of weedy cyclopean walls with grotesque fishes as my companions.</i>
        </lore>
        <entry>
            You do not have the Innsmouth Look. Nothing happens.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 7 -->
    <innsmouth-look>
        <lore>
            <i>That night, I dreamed. Great watery spaces opened out before me, and I seemed to wander through titanic sunken porticos and labyrinths of weedy cyclopean walls with grotesque fishes as my companions.</i>
        </lore>
        <entry>
            You do not have the Innsmouth Look. Nothing happens.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 8 -->
    <innsmouth-look>
        <lore>
            <i>That night, I dreamed. Great watery spaces opened out before me, and I seemed to wander through titanic sunken porticos and labyrinths of weedy cyclopean walls with grotesque fishes as my companions.</i>
        </lore>
        <entry>
            You do not have the Innsmouth Look. Nothing happens.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 9 -->
    <innsmouth-look>
        <lore>
            <i>That night, I dreamed. Great watery spaces opened out before me, and I seemed to wander through titanic sunken porticos and labyrinths of weedy cyclopean walls with grotesque fishes as my companions.</i>
        </lore>
        <entry>
            You do not have the Innsmouth Look. Nothing happens.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 10 -->
    <innsmouth-look>
        <lore>
            <i>That night, I dreamed. Great watery spaces opened out before me, and I seemed to wander through titanic sunken porticos and labyrinths of weedy cyclopean walls with grotesque fishes as my companions.</i>
        </lore>
        <entry>
            You do not have the Innsmouth Look. Nothing happens.
        </entry>
    </innsmouth-look>
</innsmouth-look-cards>
"""))
                val jsons = cards.map {
                    JSONObject(mapOf(
                        "lore" to it.lore,
                        "entry" to it.entry))
                }
                println("innsmouth jsons: $jsons")
            }

            "miskatonic" {
                val cards = InnsmouthLook.parseReader(StringReader("""<innsmouth-look-cards expansionSet="miskatonic">
    <!-- Innsmouth 1 -->
    <innsmouth-look>
        <lore>
            <i>Your blood chills when you see your friend. You&apos;ve seen his face so many times, but now it seems different. Your friend&apos;s large, watery eyes no longer appear human.</i>
        </lore>
        <entry>
            Draw one Ally card and return it to the box. Then draw another Innsmouth Look card.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 2 -->
    <innsmouth-look>
        <lore>
            <i>Looking into your own family&apos;s history, you discover a line of your ancestry that descended from the small fishing village of Innsmouth.</i>
        </lore>
        <entry>
            You may gain up to three Clue tokens. For each Clue token you choose to gain, draw another Innsmouth Look card.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 3 -->
    <innsmouth-look>
        <lore>
            <i>You wake up after dreaming of a dark, submerged city. Shadowy figures floated everywhere, and the entire ocean was teeming with activity.</i>
        </lore>
        <entry>
            All monsters with the plus sign dimensional symbol, regardless of special movement abilities, move to an adjacent street area, location, or vortex, following the path with a black movement arrow. Then draw another Innsmouth Look card.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 4 -->
    <innsmouth-look>
        <lore>
            <i>In the dimmest corner of your mind, you hear a faintly echoing call. Something is moving across the water.</i>
        </lore>
        <entry>
            All monsters with the plus sign dimensional symbol, regardless of special movement abilities, move to an adjacent street area, location, or vortex, following the path with a white movement arrow. Then draw another Innsmouth Look card.
        </entry>
    </innsmouth-look>
    <!-- Innsmouth 5 -->
    <innsmouth-look>
        <lore>
            <i>Each time you pass the mirror, the thought occurs to you, &quot;Do I dare look? How deeply do I dare gaze into my own eyes?&quot;</i>
        </lore>
        <entry>
            Choose 1 of the following:
            <p>1. Draw 1 additional Innsmouth Look card and you are Cursed.</p>
            <p>2. Draw 2 additional Innsmouth Look cards.</p>
            <p>3. Draw 3 additional Innsmouth Look cards and you are Blessed</p>
        </entry>
    </innsmouth-look>
</innsmouth-look-cards>
"""))
                val jsons = cards.map {
                    JSONObject(mapOf(
                        "lore" to it.lore,
                        "entry" to it.entry))
                }
                println("innsmouth miskatonic jsons: $jsons")
            }
        }
    }
}
