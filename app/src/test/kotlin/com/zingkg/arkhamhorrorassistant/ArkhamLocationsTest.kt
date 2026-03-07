package com.zingkg.arkhamhorrorassistant

import com.zingkg.arkhamhorrorboard.secondedition.card.Cards
import com.zingkg.arkhamhorrorboard.secondedition.card.NeighborhoodThreeLocations
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.WordSpec
import org.json.JSONObject

class ArkhamLocationsTest : WordSpec() {
  init {
    "Downtown locations" should {
      "parse correctly for Arkham base" {
        val lines = Common.readResourceFile("neighborhood_arkham_downtown__base.jsonl")
        val downtown = lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), Cards.ExpansionSet.BASE) }
        downtown.all { it.expansionSet == Cards.ExpansionSet.BASE } shouldBe true
        downtown.size shouldBe 7
      }

      "parse correctly for Dunwich" {
        val lines = Common.readResourceFile("neighborhood_arkham_downtown__dunwich_horror_exp.jsonl")
        val downtown = lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), Cards.ExpansionSet.DUNWICH_HORROR) }
        downtown.all { it.expansionSet == Cards.ExpansionSet.DUNWICH_HORROR } shouldBe true
        downtown.size shouldBe 7
      }
    }

    "Easttown locations" should {
      "parse correctly for Arkham base" {
        val lines = Common.readResourceFile("neighborhood_arkham_easttown__base.jsonl")
        val easttown = lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), Cards.ExpansionSet.BASE) }
        easttown.all { it.expansionSet == Cards.ExpansionSet.BASE } shouldBe true
        easttown.size shouldBe 7
      }
    }

    "French Hill locations" should {
      "parse correctly for Arkham base" {
        val lines = Common.readResourceFile("neighborhood_arkham_french_hill__base.jsonl")
        val frenchHill = lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), Cards.ExpansionSet.BASE) }
        frenchHill.all { it.expansionSet == Cards.ExpansionSet.BASE } shouldBe true
        frenchHill.size shouldBe 7
      }
    }

    "Merchant District locations" should {
      "parse correctly for Arkham base" {
        val lines = Common.readResourceFile("neighborhood_arkham_merchant_district__base.jsonl")
        val merchantDistrict = lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), Cards.ExpansionSet.BASE) }
        merchantDistrict.all { it.expansionSet == Cards.ExpansionSet.BASE } shouldBe true
        merchantDistrict.size shouldBe 7
      }
    }

    "Miskatonic University locations" should {
      "parse correctly for Arkham base" {
        val lines = Common.readResourceFile("neighborhood_arkham_miskatonic_university__base.jsonl")
        val miskatonicUniversity = lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), Cards.ExpansionSet.BASE) }
        miskatonicUniversity.all { it.expansionSet == Cards.ExpansionSet.BASE } shouldBe true
        miskatonicUniversity.size shouldBe 7
      }
    }

    "Northside locations" should {
      "parse correctly for Arkham base" {
        val lines = Common.readResourceFile("neighborhood_arkham_northside__base.jsonl")
        val northside = lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), Cards.ExpansionSet.BASE) }
        northside.all { it.expansionSet == Cards.ExpansionSet.BASE } shouldBe true
        northside.size shouldBe 7
      }
    }

    "Rivertown locations" should {
      "parse correctly for Arkham base" {
        val lines = Common.readResourceFile("neighborhood_arkham_rivertown__base.jsonl")
        val rivertown = lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), Cards.ExpansionSet.BASE) }
        rivertown.all { it.expansionSet == Cards.ExpansionSet.BASE } shouldBe true
        rivertown.size shouldBe 7
      }
    }

    "Southside locations" should {
      "parse correctly for Arkham base" {
        val lines = Common.readResourceFile("neighborhood_arkham_southside__base.jsonl")
        val southside = lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), Cards.ExpansionSet.BASE) }
        southside.all { it.expansionSet == Cards.ExpansionSet.BASE } shouldBe true
        southside.size shouldBe 7
      }
    }

    "Uptown locations" should {
      "parse correctly for Arkham base" {
        val lines = Common.readResourceFile("neighborhood_arkham_uptown__base.jsonl")
        val uptown = lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), Cards.ExpansionSet.BASE) }
        uptown.all { it.expansionSet == Cards.ExpansionSet.BASE } shouldBe true
        uptown.size shouldBe 7
      }
    }
  }
}
