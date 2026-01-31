package com.zingkg.arkhamhorrorboard.secondedition.card

import org.json.JSONObject

class Cards {
  enum class ExpansionSet(val longName: String, val shortName: String) {
    BASE("Arkham Horror", "ah"),
    DUNWICH_HORROR("Dunwich Horror", "dh"),
    THE_KING_IN_YELLOW("The King in Yellow", "tkiy"),
    KINGSPORT_HORROR("Kingsport Horror", "kh"),
    THE_BLACK_GOAT_OF_THE_WOODS("The Black Goat of the Woods", "tbgotw"),
    INNSMOUTH_HORROR("Innsmouth Horror", "ih"),
    THE_LURKER_AT_THE_THRESHOLD("The Lurker at the Threshold", "tlatt"),
    THE_CURSE_OF_THE_DARK_PHARAOH("The Curse of the Dark Pharaoh", "tcotdp"),
    MISKATONIC_HORROR("Miskatonic Horror", "mh")
  }
}

sealed interface Card {
  fun expansionSet(): Cards.ExpansionSet
}

data class CultEncounter(
  val title: String,
  val lore: String,
  val entry: String,
  val expansionSet: Cards.ExpansionSet
) : Card {
  override fun expansionSet(): Cards.ExpansionSet {
    return expansionSet
  }

  companion object {
    fun parseJson(
      json: JSONObject,
      expansionSet: Cards.ExpansionSet
    ): CultEncounter {
      val title = json.getString("title")
      val lore = json.getString("lore")
      val entry = json.getString("entry")
      return CultEncounter(title, lore, entry, expansionSet)
    }
  }
}

data class ExhibitEncounter(
  val title: String,
  val entry: String,
  val location: String,
  val expansionSet: Cards.ExpansionSet
) : Card {
  override fun expansionSet(): Cards.ExpansionSet {
    return expansionSet
  }

  companion object {
    fun parseJson(
      json: JSONObject,
      expansionSet: Cards.ExpansionSet
    ): ExhibitEncounter {
      val title = json.getString("title")
      val entry = json.getString("entry")
      val location = json.getString("location")
      return ExhibitEncounter(title, entry, location, expansionSet)
    }
  }
}

data class InnsmouthLook(
    val lore: String,
    val entry: String,
    val expansionSet: Cards.ExpansionSet
) : Card {
  override fun expansionSet(): Cards.ExpansionSet {
    return expansionSet
  }

  companion object {
    fun parseJson(
      json: JSONObject,
      expansionSet: Cards.ExpansionSet
    ): InnsmouthLook {
      val lore = json.getString("lore")
      val entry = json.getString("entry")
      return InnsmouthLook(lore, entry, expansionSet)
    }
  }
}

data class NeighborhoodThreeLocations(
  val location1Title: String,
  val location1Entry: String,
  val location2Title: String,
  val location2Entry: String,
  val location3Title: String,
  val location3Entry: String,
  val expansionSet: Cards.ExpansionSet): Card {
  override fun expansionSet(): Cards.ExpansionSet {
    return expansionSet
  }

  companion object {
    fun parseJson(
      json: JSONObject,
      expansionSet: Cards.ExpansionSet
    ): NeighborhoodThreeLocations {
      val locations = json.getJSONArray("locations")
      val (location1Title, location1Entry) = parseLocationJson(locations.getJSONObject(0))
      val (location2Title, location2Entry) = parseLocationJson(locations.getJSONObject(1))
      val (location3Title, location3Entry) = parseLocationJson(locations.getJSONObject(2))
      return NeighborhoodThreeLocations(
          location1Title,
          location1Entry,
          location2Title,
          location2Entry,
          location3Title,
          location3Entry,
          expansionSet)
    }

    private fun parseLocationJson(locationJson: JSONObject): Pair<String, String> {
      return Pair(locationJson.getString("title"), locationJson.getString("entry"))
    }
  }
}

data class Reckoning(
  val title: String,
  val entry: String,
  val expansionSet: Cards.ExpansionSet
) : Card {
  override fun expansionSet(): Cards.ExpansionSet {
    return expansionSet
  }

  companion object {
    fun parseJson(
      json: JSONObject,
      expansionSet: Cards.ExpansionSet
    ): Reckoning {
      val title = json.getString("title")
      val entry = json.getString("entry")
      return Reckoning(title, entry, expansionSet)
    }
  }
}
