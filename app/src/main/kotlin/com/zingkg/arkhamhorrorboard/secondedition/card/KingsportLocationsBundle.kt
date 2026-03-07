package com.zingkg.arkhamhorrorboard.secondedition.card

import kotlin.collections.List

data class KingsportLocationsBundle(
  val centralHillDeck: List<Card>,
  val harborsideDeck: List<Card>,
  val southshoreDeck: List<Card>,
  val kingsportHeadDeck: List<Card>) {

  fun filterCentralHillDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = centralHillDeck, expansionsEnabled = expansionSets)

  fun filterHarborsideDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = harborsideDeck, expansionsEnabled = expansionSets)

  fun filterSouthshoreDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = southshoreDeck, expansionsEnabled = expansionSets)

  fun filterKingsportHeadDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = kingsportHeadDeck, expansionsEnabled = expansionSets)

  companion object {
    fun empty(): KingsportLocationsBundle =
      KingsportLocationsBundle(
        centralHillDeck = emptyList(),
        harborsideDeck = emptyList(),
        southshoreDeck = emptyList(),
        kingsportHeadDeck = emptyList())
  }
}