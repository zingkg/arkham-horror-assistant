package com.zingkg.arkhamhorrorassistant.xml

import com.zingkg.arkhamhorrorboard.secondedition.xml.Reckoning
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec
import org.json.JSONObject

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

            "expansion" {
                val cards = Reckoning.parseReader(StringReader("""<reckoning-cards expansionSet="the lurker at the threshold">
    <!-- Reckoning 1 -->
    <reckoning>
        <title>A Humble Servant</title>
        <entry>Add 1 doom token to the doom track for each investigator with 3 Dark Pacts.</entry>
    </reckoning>
    <!-- Reckoning 2 -->
    <reckoning>
        <title>A Little Taste</title>
        <entry>The investigator(s) with the least Power gain 1 Power, even if he does not have a Dark Pact. Note, however, that an investigator needs a Dark Pact to spend Power.</entry>
    </reckoning>
    <!-- Reckoning 3 -->
    <reckoning>
        <title>An Offer You Can&apos;t Refuse</title>
        <entry>The first player chooses an investigator. That investigator must take a Dark Pact if able. If not able, that investigator is devoured.</entry>
    </reckoning>
    <!-- Reckoning 4 -->
    <reckoning>
        <title>Bargain of the Gate</title>
        <entry>The investigator with the most Power moves to the Other World of his choice (ties broken by the first player).</entry>
    </reckoning>
    <!-- Reckoning 5 -->
    <reckoning>
        <title>Dark Reward</title>
        <entry>Each investigator with a Dark Pact gains 1 Power per Dark Pact he has.</entry>
    </reckoning>
    <!-- Reckoning 6 -->
    <reckoning>
        <title>Debt Comes Due</title>
        <entry>For each 3 Power possessed by the investigators as a group, add 1 doom token to the doom track (round down).</entry>
    </reckoning>
    <!-- Reckoning 7 -->
    <reckoning>
        <title>Devil&apos; Bargain</title>
        <entry>The investigators must choose: either each investigator with Power must discard a Common or Unique Item, or add 1 doom token to the doom track.</entry>
    </reckoning>
    <!-- Reckoning 8 -->
    <reckoning>
        <title>Further Temptation</title>
        <entry>Each investigator with a Dark Pact draws 1 Spell.</entry>
    </reckoning>
    <!-- Reckoning 9 -->
    <reckoning>
        <title>Give the Devil his Due</title>
        <entry>Each investigator with a Dark Pact must discard 1 Gate trophy or 5 toughness worth of monster trophies or 5 Clue tokens, if able. If he cannot, he must discard all his monster trophies and Clues.</entry>
    </reckoning>
    <!-- Reckoning 10 -->
    <reckoning>
        <title>He Is Everywhere</title>
        <entry>If every investigator has 1 or more Dark Pacts and/or 1 or more Power, add a doom token to the doom track.</entry>
    </reckoning>
    <!-- Reckoning 11 -->
    <reckoning>
        <title>Humanity Lost</title>
        <entry>The investigator with the most Power is devoured (ties broken by the first player). If no investigators have Power, there is no effect.</entry>
    </reckoning>
    <!-- Reckoning 12 -->
    <reckoning>
        <title>In Too Deep</title>
        <entry>Each investigator with only 1 Dark Pact must immediately choose and take a second Dark Pact.</entry>
    </reckoning>
    <!-- Reckoning 13 -->
    <reckoning>
        <title>Power Begets Power</title>
        <entry>Each investigator with at least 1 Power gains 1 Power.</entry>
    </reckoning>
    <!-- Reckoning 14 -->
    <reckoning>
        <title>Power Corrodes</title>
        <entry>Each investigator loses 1 Sanity for each Power he has.</entry>
    </reckoning>
    <!-- Reckoning 15 -->
    <reckoning>
        <title>Power Corrupts</title>
        <entry>Each investigator loses 1 Sanity or 1 Stamina for each Power he has.</entry>
    </reckoning>
    <!-- Reckoning 16 -->
    <reckoning>
        <title>Power Decays</title>
        <entry>Each investigator loses 1 Stamina for each Power he has.</entry>
    </reckoning>
    <!-- Reckoning 17 -->
    <reckoning>
        <title>Prisoner&apos;s Dilemma</title>
        <entry>The investigators must choose: either the investigator with the most Power is devoured, or the investigator with the least Power is driven insane.</entry>
    </reckoning>
    <!-- Reckoning 18 -->
    <reckoning>
        <title>Spread the Word</title>
        <entry>Each investigator may choose to gain one or more Dark Pacts. Then, if there is at least one investigator who does not have a Dark Pact, each investigator who does have a Dark Pact is Cursed.</entry>
    </reckoning>
    <!-- Reckoning 19 -->
    <reckoning>
        <title>Strange Behavior</title>
        <entry>Each investigator with a Bound Ally must discard 3 Clue tokens. For each investigator that fails to do this, increase the terror level by 1.</entry>
    </reckoning>
    <!-- Reckoning 20 -->
    <reckoning>
        <title>Terrible Realization</title>
        <entry>Each investigator with a Blood Pact loses 2 Sanity.</entry>
    </reckoning>
    <!-- Reckoning 21 -->
    <reckoning>
        <title>The Body a Temple</title>
        <entry>Each investigator with a Soul Pact loses 2 Stamina.</entry>
    </reckoning>
    <!-- Reckoning 22 -->
    <reckoning>
        <title>The Body Decays</title>
        <entry>Each investigator with a Dark Pact loses 1 Stamina per Dark Pact he has.</entry>
    </reckoning>
    <!-- Reckoning 23 -->
    <reckoning>
        <title>The Master of Magic</title>
        <entry>Each investigator with at least 1 Spell gains 1 Power.</entry>
    </reckoning>
    <!-- Reckoning 24 -->
    <reckoning>
        <title>The Mind Unravels</title>
        <entry>Each investigator with a Dark Pact loses 1 Sanity per Dark Pact he has.</entry>
    </reckoning>
    <!-- Reckoning 25 -->
    <reckoning>
        <title>Through the Threshold</title>
        <entry>Every investigator in Arkham with 1 or more Power is drawn through the closest open Gate (his choice if there is a tie).</entry>
    </reckoning>
    <!-- Reckoning 26 -->
    <reckoning>
        <title>Turning On One Another</title>
        <entry>Discard 1 Ally from the Ally deck for each Bound Ally card in play. For each Ally that you cannot discard (because the deck is empty), increase the terror level by 1.</entry>
    </reckoning>
    <!-- Reckoning 27 -->
    <reckoning>
        <title>Uncertain Future</title>
        <entry>Shuffle the Reckoning deck and its discard pile together (including this card), then draw a new Reckoning card.</entry>
    </reckoning>
    <!-- Reckoning 28 -->
    <reckoning>
        <title>Unsettling Aura</title>
        <entry>If the total Power possessed by the investigators as a group is greater than the total number of investigators in the game, increase the terror level by 1.</entry>
    </reckoning>
</reckoning-cards>
"""))
                val jsons = cards.map {
                    JSONObject(mapOf(
                        "title" to it.title,
                        "entry" to it.entry))
                }
                println("reckoning jsons: $jsons")
            }

            "miskatonic" {
                val cards = Reckoning.parseReader(StringReader("""<reckoning-cards expansionSet="miskatonic">
    <!-- Reckoning 1 -->
    <reckoning>
        <title>An Unsettling Trend</title>
        <entry>For each investigator with a Bound Ally, raise the terror level by 1.</entry>
    </reckoning>
    <!-- Reckoning 2 -->
    <reckoning>
        <title>Beyond Control</title>
        <entry>Each investigator with a Soul Pact gains 1 Power for each point of Sanity he currently has. He then reduces his maximum Sanity by 2.</entry>
    </reckoning>
    <!-- Reckoning 3 -->
    <reckoning>
        <title>Blood Boils</title>
        <entry>Each investigator with a Blood Pact gains 1 Power for each point of Stamina he currently has. He then reduces his maximum Stamina by 2.</entry>
    </reckoning>
    <!-- Reckoning 4 -->
    <reckoning>
        <title>Devotion Beyond Reason</title>
        <entry>Each investigator with a Bound Ally must discard all of his money and items.</entry>
    </reckoning>
    <!-- Reckoning 5 -->
    <reckoning>
        <title>His Undivided Attention</title>
        <entry>Draw the next 2 Reckoning cards from the deck and resolve them both, one at a time in the order drawn.</entry>
    </reckoning>
    <!-- Reckoning 6 -->
    <reckoning>
        <title>Lest You Wither Away</title>
        <entry>Each investigator may choose to immediately gain 1 or more Dark Pacts. Then, if there is at least 1 investigator who does not have a Dark Pact, each investigator who does have a Dark Pact reduces his maximum Stamina by 1.</entry>
    </reckoning>
    <!-- Reckoning 7 -->
    <reckoning>
        <title>Merely a Vessel</title>
        <entry>Each investigator with 1 or more Power tokens in Arkham is delayed. Each investigator with 1 or more Power tokens in an Other World is lost in time and space.</entry>
    </reckoning>
    <!-- Reckoning 8 -->
    <reckoning>
        <title>So Easy to Break</title>
        <entry>Each investigator that has a Dark Pact but does not currently have a Blood Pact loses 2 Stamina.</entry>
    </reckoning>
    <!-- Reckoning 9 -->
    <reckoning>
        <title>Surrender Your Soul</title>
        <entry>Each investigator that has a Dark Pact but does not currently have a Soul Pact loses 2 Sanity.</entry>
    </reckoning>
    <!-- Reckoning 10 -->
    <reckoning>
        <title>The Barriers Crumble</title>
        <entry>A gate opens and a monster appears in each unstable location containing an investigator with 1 or more Dark Pacts (if there is not already a gate in that location).</entry>
    </reckoning>
    <!-- Reckoning 11 -->
    <reckoning>
        <title>The Legacy of Blood</title>
        <entry>Each investigator with a Blood Pact is devoured. The new investigator chosen to replace him starts with a Blood Pact and 3 Power tokens in addition to his other fixed possessions.</entry>
    </reckoning>
    <!-- Reckoning 12 -->
    <reckoning>
        <title>The Legacy of Madness</title>
        <entry>Each investigator with a Soul Pact is devoured. The new investigator chosen to replace him starts with a Soul Pact and 3 Power tokens in addition to his other fixed possessions.</entry>
    </reckoning>
    <!-- Reckoning 13 -->
    <reckoning>
        <title>The Voices Beckon</title>
        <entry>Each investigator may choose to immediately gain 1 or more Dark Pacts. Then, if there is at least 1 investigator who does not have a Dark Pact, each investigator who does have a Dark Pact reduces his maximum Sanity by 1.</entry>
    </reckoning>
    <!-- Reckoning 14 -->
    <reckoning>
        <title>Traveler of the Paths</title>
        <entry>Each investigator with 2 or more Dark Pacts must immediately return all of his gate trophies face down to the bottom of the pile of gate markers.</entry>
    </reckoning>
</reckoning-cards>
"""))
                val jsons = cards.map {
                    JSONObject(mapOf(
                        "title" to it.title,
                        "entry" to it.entry))
                }
                println("reckoning miskatonic jsons: $jsons")
            }
        }
    }
}
