package com.zingkg.arkhamhorrorboard.secondedition.card

data class InnsmouthLocationsBundle(
  val factoryDistrictDeck: List<Card>,
  val churchGreenDeck: List<Card>,
  val innsmouthShoreDeck: List<Card>) {

  fun filterFactoryDistrictDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = factoryDistrictDeck, expansionsEnabled = expansionSets)

  fun filterChurchGreenDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = churchGreenDeck, expansionsEnabled = expansionSets)

  fun filterInnsmouthShoreDeck(expansionSets: Set<Cards.ExpansionSet>): List<Card> =
    Cards.filterDeck(deck = innsmouthShoreDeck, expansionsEnabled = expansionSets)

  companion object {
    fun empty(): InnsmouthLocationsBundle =
      InnsmouthLocationsBundle(
        factoryDistrictDeck = emptyList(),
        churchGreenDeck = emptyList(),
        innsmouthShoreDeck = emptyList())
  }
}
