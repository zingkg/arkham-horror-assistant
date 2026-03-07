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
    MISKATONIC_HORROR("Miskatonic Horror", "mh"),
    MISKATONIC_HORROR_DUNWICH("Miskatonic Horror - Dunwich", "mh-dh"),
    MISKATONIC_HORROR_DUNWICH_TKIY("Miskatonic Horror - Dunwich (TKIY)", "mh-dh,tkiy"),
    MISKATONIC_HORROR_DUNWICH_KH("Miskatonic Horror - Dunwich (KH)", "mh-dh,kh"),
    MISKATONIC_HORROR_DUNWICH_TBGOTW("Miskatonic Horror - Dunwich (KH)", "mh-dh,tbgotw"),
    MISKATONIC_HORROR_DUNWICH_IH("Miskatonic Horror - Dunwich (IH)", "mh-dh,ih"),
    MISKATONIC_HORROR_DUNWICH_TLATT("Miskatonic Horror - Dunwich (TLATT)", "mh-dh,tlatt"),
    MISKATONIC_HORROR_DUNWICH_TCOTDP("Miskatonic Horror - Dunwich (TCOTDP)", "mh-dh,tcotdp"),
    MISKATONIC_HORROR_KINGSPORT("Miskatonic Horror - Kingsport", "mh-kh"),
    MISKATONIC_HORROR_KINGSPORT_DH("Miskatonic Horror - Kingsport (DH)", "mh-kh,dh"),
    MISKATONIC_HORROR_KINGSPORT_TKIY("Miskatonic Horror - Kingsport (TKIY)", "mh-kh,tkiy"),
    MISKATONIC_HORROR_KINGSPORT_TBGOTW("Miskatonic Horror - Kingsport (TBGOTW)", "mh-kh,tbgotw"),
    MISKATONIC_HORROR_KINGSPORT_IH("Miskatonic Horror - Kingsport (IH)", "mh-kh,ih"),
    MISKATONIC_HORROR_KINGSPORT_TLATT("Miskatonic Horror - Kingsport (TLATT)", "mh-kh,tlatt"),
    MISKATONIC_HORROR_KINGSPORT_TCOTDP("Miskatonic Horror - Kingsport (TCOTDP)", "mh-kh,tcotdp"),
    MISKATONIC_HORROR_INNSMOUTH("Miskatonic Horror - Innsmouth", "mh-ih"),
    MISKATONIC_HORROR_INNSMOUTH_DH("Miskatonic Horror - Innsmouth (DH)", "mh-ih,dh"),
    MISKATONIC_HORROR_INNSMOUTH_TKIY("Miskatonic Horror - Innsmouth (TKIY)", "mh-ih,tkiy"),
    MISKATONIC_HORROR_INNSMOUTH_KH("Miskatonic Horror - Innsmouth (KH)", "mh-ih,kh"),
    MISKATONIC_HORROR_INNSMOUTH_TBGOTW("Miskatonic Horror - Innsmouth (TBGOTW)", "mh-ih,tbgotw"),
    MISKATONIC_HORROR_INNSMOUTH_TLATT("Miskatonic Horror - Innsmouth (TLATT)", "mh-ih,tlatt"),
    MISKATONIC_HORROR_INNSMOUTH_TCOTDP("Miskatonic Horror - Innsmouth (TCOTDP)", "mh-ih,tcotdp"),
  }

  companion object {
    fun <T : Card> filterDeck(
    deck: List<T>,
    expansionsEnabled: Set<ExpansionSet>): List<T> =
    deck.filter { expansionsEnabled.contains(it.expansionSet()) }

    fun settingsToExpansionSets(
      dunwichHorrorExpSetting: Boolean,
      theKingInYellowExpSetting: Boolean,
      kingsportHorrorExpSetting: Boolean,
      theBlackGoatOfTheWoodsExpSetting: Boolean,
      innsmouthHorrorExpSetting: Boolean,
      theLurkerAtTheThresholdExpSetting: Boolean,
      theCurseOfTheDarkPharaohExpSetting: Boolean,
      miskatonicHorrorExpSetting: Boolean,): Set<ExpansionSet> {
      val dunwich = if (dunwichHorrorExpSetting && miskatonicHorrorExpSetting) {
        val base = setOf(ExpansionSet.DUNWICH_HORROR, ExpansionSet.MISKATONIC_HORROR_DUNWICH)
        val tkiy = if (theKingInYellowExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_DUNWICH_TKIY)
        } else {
          emptySet()
        }
        val kh = if (kingsportHorrorExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
        } else {
          emptySet()
        }
        val tbgotw = if (theBlackGoatOfTheWoodsExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_DUNWICH_TBGOTW)
        } else {
          emptySet()
        }
        val ih = if (innsmouthHorrorExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_DUNWICH_IH)
        } else {
          emptySet()
        }
        val tlatt = if (theLurkerAtTheThresholdExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_DUNWICH_TLATT)
        } else {
          emptySet()
        }
        val tcotdp = if (theCurseOfTheDarkPharaohExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_DUNWICH_TCOTDP)
        } else {
          emptySet()
        }
        base + tkiy + kh + tbgotw + ih + tlatt + tcotdp
      } else if (dunwichHorrorExpSetting) {
        setOf(ExpansionSet.DUNWICH_HORROR)
      } else {
        emptySet()
      }

      val theKingInYellow = if (theKingInYellowExpSetting) {
        setOf(ExpansionSet.THE_KING_IN_YELLOW)
      } else {
        emptySet()
      }

      val kingsport = if (kingsportHorrorExpSetting && miskatonicHorrorExpSetting) {
        val base = setOf(ExpansionSet.KINGSPORT_HORROR, ExpansionSet.MISKATONIC_HORROR_KINGSPORT)
        val dh = if (dunwichHorrorExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_KINGSPORT_DH)
        } else {
          emptySet()
        }
        val tkiy = if (theKingInYellowExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TKIY)
        } else {
          emptySet()
        }
        val tbgotw = if (theBlackGoatOfTheWoodsExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TBGOTW)
        } else {
          emptySet()
        }
        val ih = if (innsmouthHorrorExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_KINGSPORT_IH)
        } else {
          emptySet()
        }
        val tlatt = if (theLurkerAtTheThresholdExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TLATT)
        } else {
          emptySet()
        }
        val tcotdp = if (theCurseOfTheDarkPharaohExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TCOTDP)
        } else {
          emptySet()
        }
        base + dh + tkiy + tbgotw + ih + tlatt + tcotdp
      } else if (kingsportHorrorExpSetting) {
        setOf(ExpansionSet.KINGSPORT_HORROR)
      } else {
        emptySet()
      }

      val theBlackGoatOfTheWoods = if (theBlackGoatOfTheWoodsExpSetting) {
        setOf(ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
      } else {
        emptySet()
      }

      val innsmouth = if (innsmouthHorrorExpSetting && miskatonicHorrorExpSetting) {
        val base = setOf(ExpansionSet.INNSMOUTH_HORROR, ExpansionSet.MISKATONIC_HORROR_INNSMOUTH)
        val dh = if (dunwichHorrorExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_DH)
        } else {
          emptySet()
        }
        val tkiy = if (theKingInYellowExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TKIY)
        } else {
          emptySet()
        }
        val kh = if (kingsportHorrorExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_KH)
        } else {
          emptySet()
        }
        val tbgotw = if (theBlackGoatOfTheWoodsExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TBGOTW)
        } else {
          emptySet()
        }
        val tlatt = if (theLurkerAtTheThresholdExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TLATT)
        } else {
          emptySet()
        }
        val tcotdp = if (theCurseOfTheDarkPharaohExpSetting) {
          setOf(ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TCOTDP)
        } else {
          emptySet()
        }
        base + dh + tkiy + kh + tbgotw + tlatt + tcotdp
      } else if (innsmouthHorrorExpSetting) {
        setOf(ExpansionSet.INNSMOUTH_HORROR)
      } else {
        emptySet()
      }

      val theLurkerAtTheThreshold = if (theLurkerAtTheThresholdExpSetting) {
        setOf(ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
      } else {
        emptySet()
      }

      val theCurseOfTheDarkPharaoh = if (theCurseOfTheDarkPharaohExpSetting) {
        setOf(ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)
      } else {
        emptySet()
      }

      val miskatonic = if (miskatonicHorrorExpSetting) {
        setOf(ExpansionSet.MISKATONIC_HORROR)
      } else {
        emptySet()
      }

      return setOf(ExpansionSet.BASE) +
        dunwich +
        theKingInYellow +
        kingsport +
        theBlackGoatOfTheWoods +
        innsmouth +
        theLurkerAtTheThreshold +
        theCurseOfTheDarkPharaoh +
        miskatonic
    }
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
