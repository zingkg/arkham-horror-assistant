package com.zingkg.arkhamhorrorboard.secondedition.card

data class DunwichLocationsBundle(
  val backwoodsCountryDeck: List<Card>,
  val blastedHeathDeck: List<Card>,
  val villageCommonsDeck: List<Card>) {

  fun filterBackwoodsCountryDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = backwoodsCountryDeck, expansionsEnabled = expansionSets)

  fun filterBlastedHeathDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = blastedHeathDeck, expansionsEnabled = expansionSets)

  fun filterVillageCommonsDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = villageCommonsDeck, expansionsEnabled = expansionSets)

  companion object {
    fun empty(): DunwichLocationsBundle =
      DunwichLocationsBundle(
        backwoodsCountryDeck = emptyList(),
        blastedHeathDeck = emptyList(),
        villageCommonsDeck = emptyList())
  }
}
