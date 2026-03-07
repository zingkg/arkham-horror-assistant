package com.zingkg.arkhamhorrorboard.secondedition.card

import kotlin.collections.List

data class ArkhamLocationsBundle(
  val northsideDeck: List<Card>,
  val downtownDeck: List<Card>,
  val easttownDeck: List<Card>,
  val merchantDistrictDeck: List<Card>,
  val rivertownDeck: List<Card>,
  val miskatonicUniversityDeck: List<Card>,
  val frenchHillDeck: List<Card>,
  val uptownDeck: List<Card>,
  val southsideDeck: List<Card>) {

  fun filterNorthsideDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = northsideDeck, expansionsEnabled = expansionSets)

  fun filterDowntownDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = downtownDeck, expansionsEnabled = expansionSets)

  fun filterEasttownDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = easttownDeck, expansionsEnabled = expansionSets)

  fun filterMerchantDistrictDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = merchantDistrictDeck, expansionsEnabled = expansionSets)

  fun filterRivertownDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = rivertownDeck, expansionsEnabled = expansionSets)

  fun filterMiskatonicUniversityDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = miskatonicUniversityDeck, expansionsEnabled = expansionSets)

  fun filterFrenchHillDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = frenchHillDeck, expansionsEnabled = expansionSets)

  fun filterUptownDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = uptownDeck, expansionsEnabled = expansionSets)

  fun filterSouthsideDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = southsideDeck, expansionsEnabled = expansionSets)

  companion object {
    fun empty(): ArkhamLocationsBundle =
      ArkhamLocationsBundle(
        northsideDeck = emptyList(),
        downtownDeck = emptyList(),
        easttownDeck = emptyList(),
        merchantDistrictDeck = emptyList(),
        rivertownDeck = emptyList(),
        miskatonicUniversityDeck = emptyList(),
        frenchHillDeck = emptyList(),
        uptownDeck = emptyList(),
        southsideDeck = emptyList())
  }
}
