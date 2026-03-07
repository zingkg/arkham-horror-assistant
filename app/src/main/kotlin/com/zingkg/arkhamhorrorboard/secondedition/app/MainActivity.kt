package com.zingkg.arkhamhorrorboard.secondedition.app

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

import java.io.InputStreamReader
import java.io.Reader

import com.zingkg.arkhamhorrorboard.secondedition.app.fragments.CardFragment
import com.zingkg.arkhamhorrorboard.secondedition.app.fragments.CultEncounterFragment
import com.zingkg.arkhamhorrorboard.secondedition.app.fragments.ExhibitEncounterFragment
import com.zingkg.arkhamhorrorboard.secondedition.app.fragments.InnsmouthLookFragment
import com.zingkg.arkhamhorrorboard.secondedition.app.fragments.NeighborhoodThreeLocationsFragment
import com.zingkg.arkhamhorrorboard.secondedition.app.fragments.ReckoningFragment
import com.zingkg.arkhamhorrorboard.secondedition.card.ArkhamLocationsBundle
import com.zingkg.arkhamhorrorboard.secondedition.card.Card
import com.zingkg.arkhamhorrorboard.secondedition.card.Cards
import com.zingkg.arkhamhorrorboard.secondedition.card.CultEncounter
import com.zingkg.arkhamhorrorboard.secondedition.card.DunwichLocationsBundle
import com.zingkg.arkhamhorrorboard.secondedition.card.ExhibitEncounter
import com.zingkg.arkhamhorrorboard.secondedition.card.InnsmouthLocationsBundle
import com.zingkg.arkhamhorrorboard.secondedition.card.InnsmouthLook
import com.zingkg.arkhamhorrorboard.secondedition.card.KingsportLocationsBundle
import com.zingkg.arkhamhorrorboard.secondedition.card.NeighborhoodThreeLocations
import com.zingkg.arkhamhorrorboard.secondedition.card.Reckoning
import org.json.JSONObject
import kotlin.math.min

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener, CardFragment.CardCallbacks {
  private var drawerLayout: DrawerLayout? = null
  private var drawerList: ListView? = null
  private var drawerToggle: ActionBarDrawerToggle? = null
  private var viewPager: ViewPager? = null
  private var pagerAdapter: DeckPagerAdapter? = null
  private var lastDrawerPosition: Int = 0

  private var allArkhamLocationsBundle: ArkhamLocationsBundle = ArkhamLocationsBundle.empty()
  private var arkhamLocationsBundle: ArkhamLocationsBundle = ArkhamLocationsBundle.empty()

  private var previousDunwichHorrorExpSetting: Boolean = false
  private var dunwichHorrorExpSetting: Boolean = false
  private var allDunwichLocationsBundle: DunwichLocationsBundle = DunwichLocationsBundle.empty()
  private var dunwichLocationsBundle: DunwichLocationsBundle = DunwichLocationsBundle.empty()

  private var previousTheKingInYellowExpSetting: Boolean = false
  private var theKingInYellowExpSetting: Boolean = false

  private var previousKingsportHorrorExpSetting: Boolean = false
  private var kingsportHorrorExpSetting: Boolean = false
  private var allKingsportLocationsBundle: KingsportLocationsBundle = KingsportLocationsBundle.empty()
  private var kingsportLocationsBundle: KingsportLocationsBundle = KingsportLocationsBundle.empty()

  private var previousTheBlackGoatOfTheWoodsExpSetting: Boolean = false
  private var theBlackGoatOfTheWoodsExpSetting: Boolean = false
  private var allCultEncounterDeck: List<Card> = emptyList()
  private var cultEncounterDeck: List<Card> = emptyList()

  private var previousInnsmouthHorrorExpSetting: Boolean = false
  private var innsmouthHorrorExpSetting: Boolean = false
  private var allInnsmouthLocationsBundle = InnsmouthLocationsBundle.empty()
  private var innsmouthLocationsBundle = InnsmouthLocationsBundle.empty()
  private var allInnsmouthLookDeck: List<Card> = emptyList()
  private var innsmouthLookDeck: List<Card> = emptyList()

  private var previousTheLurkerAtTheThresholdExpSetting: Boolean = false
  private var theLurkerAtTheThresholdExpSetting: Boolean = false
  private var reckoningSeenIndex: Int = 0
  private var allReckoningDeck: List<Reckoning> = emptyList()
  private var reckoningDeck: List<Reckoning> = emptyList()

  private var previousTheCurseOfTheDarkPharaohExpSetting: Boolean = false
  private var theCurseOfTheDarkPharaohExpSetting: Boolean = false
  private var allExhibitEncounterDeck: List<Card> = emptyList()
  private var exhibitEncounterDeck: List<Card> = emptyList()

  private var previousMiskatonicHorrorExpSetting: Boolean = false
  private var miskatonicHorrorExpSetting: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    drawerLayout = findViewById(R.id.drawer_layout)
    drawerList = findViewById(R.id.nav_drawer)
    drawerLayout?.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START)
    drawerList?.adapter = ArrayAdapter(this, R.layout.drawer_list_item, titleStrings)
    drawerList?.onItemClickListener = this

    // enable ActionBar app icon to behave as action to toggle nav drawer
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setHomeButtonEnabled(true)

    // ActionBarDrawerToggle ties together the proper interactions
    // between the sliding drawer and the action bar app icon
    val currentDrawerToggle = object : ActionBarDrawerToggle(
      this, // host Activity
      drawerLayout, // DrawerLayout object
      toolbar, // nav drawer image to replace 'Up' caret
      R.string.drawer_open, // "open drawer" description for accessibility
      R.string.drawer_close   // "close drawer" description for accessibility
    ) {
      override fun onDrawerClosed(view: View?) {
        invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
      }

      override fun onDrawerOpened(drawerView: View?) {
        invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
      }
    }
    drawerLayout?.addDrawerListener(currentDrawerToggle)
    drawerToggle = currentDrawerToggle
    lastDrawerPosition = 0
    supportActionBar?.title = titleStrings[lastDrawerPosition]

    // Instantiate a view pager and a pager adapter.
    readAllSettings()
    readAllCards()
    assembleAllCards(force = true)
    viewPager = findViewById(R.id.fragment_container)
    setPagerAdapter(
      shuffleCards(arkhamLocationsBundle.northsideDeck),
      CardType.ArkhamNorthside)
  }

  private fun readAllSettings() {
    previousDunwichHorrorExpSetting = dunwichHorrorExpSetting
    dunwichHorrorExpSetting = dunwichHorrorExpSetting()

    previousTheKingInYellowExpSetting = theKingInYellowExpSetting
    theKingInYellowExpSetting = theKingInYellowExpSetting()

    previousKingsportHorrorExpSetting = kingsportHorrorExpSetting
    kingsportHorrorExpSetting = kingsportHorrorExpSetting()

    previousTheBlackGoatOfTheWoodsExpSetting = theBlackGoatOfTheWoodsExpSetting
    theBlackGoatOfTheWoodsExpSetting = theBlackGoatOfTheWoodsExpSetting()

    previousInnsmouthHorrorExpSetting = innsmouthHorrorExpSetting
    innsmouthHorrorExpSetting = innsmouthHorrorExpSetting()

    previousTheLurkerAtTheThresholdExpSetting = theLurkerAtTheThresholdExpSetting
    theLurkerAtTheThresholdExpSetting = theLurkerAtTheThresholdExpSetting()

    previousTheCurseOfTheDarkPharaohExpSetting = theCurseOfTheDarkPharaohExpSetting
    theCurseOfTheDarkPharaohExpSetting = theCurseOfTheDarkPharaohExpSetting()

    previousMiskatonicHorrorExpSetting = miskatonicHorrorExpSetting
    miskatonicHorrorExpSetting = miskatonicHorrorExpSetting()
  }

  override fun onResume() {
    super.onResume()
    readAllSettings()
    // Settings likely changed. Read all files and regenerate all the decks
    readAllCards()
    assembleAllCards(force = false)
    // Set the pagerAdapter to the assigned cards
    pagerAdapter?.let {
      when (it.cardType) {
        CardType.ArkhamNorthside -> setPagerAdapter(
          shuffleCards(arkhamLocationsBundle.northsideDeck),
          CardType.ArkhamNorthside)
        CardType.ArkhamDowntown -> setPagerAdapter(
          shuffleCards(arkhamLocationsBundle.downtownDeck),
          CardType.ArkhamDowntown)
        CardType.ArkhamEasttown -> setPagerAdapter(
          shuffleCards(arkhamLocationsBundle.easttownDeck),
          CardType.ArkhamEasttown)
        CardType.ArkhamMerchantDistrict -> setPagerAdapter(
          shuffleCards(arkhamLocationsBundle.merchantDistrictDeck),
          CardType.ArkhamMerchantDistrict)
        CardType.ArkhamRivertown -> setPagerAdapter(
          shuffleCards(arkhamLocationsBundle.rivertownDeck),
          CardType.ArkhamRivertown)
        CardType.ArkhamMiskatonicUniversity -> setPagerAdapter(
          shuffleCards(arkhamLocationsBundle.miskatonicUniversityDeck),
          CardType.ArkhamMiskatonicUniversity)
        CardType.ArkhamFrenchHill -> setPagerAdapter(
          shuffleCards(arkhamLocationsBundle.frenchHillDeck),
          CardType.ArkhamFrenchHill)
        CardType.ArkhamUptown -> setPagerAdapter(
          shuffleCards(arkhamLocationsBundle.uptownDeck),
          CardType.ArkhamUptown)
        CardType.ArkhamSouthside -> setPagerAdapter(
          shuffleCards(arkhamLocationsBundle.southsideDeck),
          CardType.ArkhamSouthside)
        CardType.DunwichBackwoodsCountry -> setPagerAdapter(
          shuffleCards(dunwichLocationsBundle.backwoodsCountryDeck),
          CardType.DunwichBackwoodsCountry)
        CardType.DunwichBlastedHeath -> setPagerAdapter(
          shuffleCards(dunwichLocationsBundle.blastedHeathDeck),
          CardType.DunwichBlastedHeath)
        CardType.DunwichVillageCommons -> setPagerAdapter(
          shuffleCards(dunwichLocationsBundle.villageCommonsDeck),
          CardType.DunwichVillageCommons)
        CardType.KingsportCentralHill -> setPagerAdapter(
          shuffleCards(kingsportLocationsBundle.centralHillDeck),
          CardType.KingsportCentralHill)
        CardType.KingsportHarborside -> setPagerAdapter(
          shuffleCards(kingsportLocationsBundle.harborsideDeck),
          CardType.KingsportHarborside)
        CardType.KingsportSouthshore -> setPagerAdapter(
          shuffleCards(kingsportLocationsBundle.southshoreDeck),
          CardType.KingsportSouthshore)
        CardType.KingsportKingsportHead -> setPagerAdapter(
          shuffleCards(kingsportLocationsBundle.kingsportHeadDeck),
          CardType.KingsportKingsportHead)
        CardType.InnsmouthFactoryDistrict -> setPagerAdapter(
          shuffleCards(innsmouthLocationsBundle.factoryDistrictDeck),
          CardType.InnsmouthFactoryDistrict)
        CardType.InnsmouthChurchGreen -> setPagerAdapter(
          shuffleCards(innsmouthLocationsBundle.churchGreenDeck),
          CardType.InnsmouthChurchGreen)
        CardType.InnsmouthInnsmouthShore -> setPagerAdapter(
          shuffleCards(innsmouthLocationsBundle.innsmouthShoreDeck),
          CardType.InnsmouthInnsmouthShore)
        CardType.InnsmouthInnsmouthLook -> setPagerAdapter(
          shuffleCards(innsmouthLookDeck),
          CardType.InnsmouthInnsmouthLook)
        CardType.CultEncounter -> setPagerAdapter(
          shuffleCards(cultEncounterDeck),
          CardType.CultEncounter)
        CardType.ExhibitEncounter -> setPagerAdapter(
          shuffleCards(exhibitEncounterDeck),
          CardType.ExhibitEncounter)
        CardType.Reckoning -> setPagerAdapter(
          shuffleCards(reckoningDeck),
          CardType.Reckoning)
      }
    }
  }

  private fun readAllCards() {
    // Check Arkham Neighborhoods
    if (allArkhamLocationsBundle == ArkhamLocationsBundle.empty()) {
      allArkhamLocationsBundle = ArkhamLocationsBundle(
        northsideDeck = readArkhamNorthsideDeck(),
        downtownDeck = readArkhamDowntownDeck(),
        easttownDeck = readArkhamEasttownDeck(),
        merchantDistrictDeck = readArkhamMerchantDistrictDeck(),
        rivertownDeck = readArkhamRivertownDeck(),
        miskatonicUniversityDeck = readArkhamMiskatonicUniversityDeck(),
        frenchHillDeck = readArkhamFrenchHillDeck(),
        uptownDeck = readArkhamUptownDeck(),
        southsideDeck = readArkhamSouthsideDeck())
    }

    if (allDunwichLocationsBundle == DunwichLocationsBundle.empty()) {
      allDunwichLocationsBundle = DunwichLocationsBundle(
        backwoodsCountryDeck = readDunwichBackwoodsCountryDeck(),
        blastedHeathDeck = readDunwichBlastedHeathDeck(),
        villageCommonsDeck = readDunwichVillageCommonsDeck())
    }

    if (allKingsportLocationsBundle == KingsportLocationsBundle.empty()) {
      allKingsportLocationsBundle = KingsportLocationsBundle(
        centralHillDeck = readKingsportCentralHillDeck(),
        harborsideDeck = readKingsportHarborsideDeck(),
        southshoreDeck = readKingsportSouthShoreDeck(),
        kingsportHeadDeck = readKingsportKingsportHeadDeck())
    }

    if (allInnsmouthLocationsBundle == InnsmouthLocationsBundle.empty()) {
      allInnsmouthLocationsBundle = InnsmouthLocationsBundle(
        factoryDistrictDeck = readInnsmouthFactoryDistrictDeck(),
        churchGreenDeck = readInnsmouthChurchGreenDeck(),
        innsmouthShoreDeck = readInnsmouthInnsmouthShoreDeck())
    }

    if (allCultEncounterDeck.isEmpty()) {
      allCultEncounterDeck = readCultEncounterDeck()
    }
    if (allInnsmouthLookDeck.isEmpty()) {
      allInnsmouthLookDeck = readInnsmouthLookDeck()
    }
    if (allExhibitEncounterDeck.isEmpty()) {
      allExhibitEncounterDeck = readExhibitEncounterDeck()
    }
    if (allReckoningDeck.isEmpty()) {
      reckoningSeenIndex = 0
      allReckoningDeck = readReckoningDeck()
    }
  }

  private fun assembleAllCards(force: Boolean) {
    val enabledExpansionSets = Cards.settingsToExpansionSets(
      dunwichHorrorExpSetting = dunwichHorrorExpSetting,
      theKingInYellowExpSetting = theKingInYellowExpSetting,
      kingsportHorrorExpSetting = kingsportHorrorExpSetting,
      theBlackGoatOfTheWoodsExpSetting = theBlackGoatOfTheWoodsExpSetting,
      innsmouthHorrorExpSetting = innsmouthHorrorExpSetting,
      theLurkerAtTheThresholdExpSetting = theLurkerAtTheThresholdExpSetting,
      theCurseOfTheDarkPharaohExpSetting = theCurseOfTheDarkPharaohExpSetting,
      miskatonicHorrorExpSetting = miskatonicHorrorExpSetting)
    // Check Arkham Neighborhoods
    if (previousTheKingInYellowExpSetting != theKingInYellowExpSetting ||
      previousTheBlackGoatOfTheWoodsExpSetting != theBlackGoatOfTheWoodsExpSetting ||
      previousTheLurkerAtTheThresholdExpSetting != theLurkerAtTheThresholdExpSetting ||
      previousTheCurseOfTheDarkPharaohExpSetting != theCurseOfTheDarkPharaohExpSetting ||
      previousMiskatonicHorrorExpSetting != miskatonicHorrorExpSetting ||
      force) {
      arkhamLocationsBundle = ArkhamLocationsBundle(
        northsideDeck = allArkhamLocationsBundle.filterNorthsideDeck(enabledExpansionSets),
        downtownDeck = allArkhamLocationsBundle.filterDowntownDeck(enabledExpansionSets),
        easttownDeck = allArkhamLocationsBundle.filterEasttownDeck(enabledExpansionSets),
        merchantDistrictDeck = allArkhamLocationsBundle.filterMerchantDistrictDeck(enabledExpansionSets),
        rivertownDeck = allArkhamLocationsBundle.filterRivertownDeck(enabledExpansionSets),
        miskatonicUniversityDeck = allArkhamLocationsBundle.filterMiskatonicUniversityDeck(enabledExpansionSets),
        frenchHillDeck = allArkhamLocationsBundle.filterFrenchHillDeck(enabledExpansionSets),
        uptownDeck = allArkhamLocationsBundle.filterUptownDeck(enabledExpansionSets),
        southsideDeck = allArkhamLocationsBundle.filterSouthsideDeck(enabledExpansionSets))
    }

    if ((dunwichHorrorExpSetting && !previousDunwichHorrorExpSetting) ||
      (dunwichHorrorExpSetting && previousMiskatonicHorrorExpSetting != miskatonicHorrorExpSetting) ||
      force) {
      dunwichLocationsBundle = DunwichLocationsBundle(
        backwoodsCountryDeck = allDunwichLocationsBundle.filterBackwoodsCountryDeck(enabledExpansionSets),
        blastedHeathDeck = allDunwichLocationsBundle.filterBlastedHeathDeck(enabledExpansionSets),
        villageCommonsDeck = allDunwichLocationsBundle.filterVillageCommonsDeck(enabledExpansionSets))
    }

    if ((kingsportHorrorExpSetting && !previousKingsportHorrorExpSetting) ||
      (kingsportHorrorExpSetting && previousMiskatonicHorrorExpSetting != miskatonicHorrorExpSetting) ||
      force) {
      kingsportLocationsBundle = KingsportLocationsBundle(
        centralHillDeck = allKingsportLocationsBundle.filterCentralHillDeck(enabledExpansionSets),
        harborsideDeck = allKingsportLocationsBundle.filterHarborsideDeck(enabledExpansionSets),
        southshoreDeck = allKingsportLocationsBundle.filterSouthshoreDeck(enabledExpansionSets),
        kingsportHeadDeck = allKingsportLocationsBundle.filterKingsportHeadDeck(enabledExpansionSets))
    }

    if ((innsmouthHorrorExpSetting && !previousInnsmouthHorrorExpSetting) ||
      (innsmouthHorrorExpSetting && previousMiskatonicHorrorExpSetting != miskatonicHorrorExpSetting) ||
      force) {
      innsmouthLocationsBundle = InnsmouthLocationsBundle(
        factoryDistrictDeck = allInnsmouthLocationsBundle.filterFactoryDistrictDeck(enabledExpansionSets),
        churchGreenDeck = allInnsmouthLocationsBundle.filterChurchGreenDeck(enabledExpansionSets),
        innsmouthShoreDeck = allInnsmouthLocationsBundle.filterInnsmouthShoreDeck(enabledExpansionSets))
    }

    if (previousMiskatonicHorrorExpSetting != miskatonicHorrorExpSetting || force) {
      cultEncounterDeck = Cards.filterDeck(allCultEncounterDeck, enabledExpansionSets)
      innsmouthLookDeck = Cards.filterDeck(allInnsmouthLookDeck, enabledExpansionSets)
      exhibitEncounterDeck = Cards.filterDeck(allExhibitEncounterDeck, enabledExpansionSets)
      reckoningSeenIndex = 0
      reckoningDeck = Cards.filterDeck(allReckoningDeck, enabledExpansionSets)
    }
  }

  private fun setPagerAdapter(cards: List<Card>, cardType: CardType) {
    pagerAdapter = DeckPagerAdapter(
      supportFragmentManager,
      supportActionBar?.title?.toString() ?: "",
      cards,
      cardType,
      miskatonicHorrorExpSetting
    )
    viewPager?.adapter = pagerAdapter

    if (cardType == CardType.Reckoning) {
      viewPager?.currentItem = reckoningSeenIndex
    }
  }

  private fun setReckoningSeenIndex() {
    if (pagerAdapter?.cardType == CardType.Reckoning) {
      reckoningSeenIndex = viewPager?.currentItem ?: 0
    }
  }

  private fun readResource(resource: Int): Reader {
    return InputStreamReader(resources.openRawResource(resource))
  }

  private fun parseNeighborhoodResource(resource: Int, expansionSet: Cards.ExpansionSet): List<NeighborhoodThreeLocations> {
    val reader = readResource(resource)
    val lines = reader.readLines()
    reader.close()
    return lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), expansionSet) }
  }

  /**
  * BEGIN Arkham locations utility
  */

  private fun readArkhamNorthsideDeck(): List<Card> {
    val base = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_northside__base,
        Cards.ExpansionSet.BASE)
    val dunwich = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_northside__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    val theKingInYellow = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_northside__the_king_in_yellow_exp,
        Cards.ExpansionSet.THE_KING_IN_YELLOW)
    val kingsport = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_northside__the_king_in_yellow_exp,
        Cards.ExpansionSet.KINGSPORT_HORROR)
    val theBlackGoatOfTheWoods = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_northside__the_black_goat_of_the_woods_exp,
        Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    val innsmouth = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_northside__innsmouth_horror_exp,
        Cards.ExpansionSet.INNSMOUTH_HORROR)
    val theLurkerAtTheThreshold = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_northside__the_lurker_at_the_threshold_exp,
        Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    val theCurseOfTheDarkPharaoh = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_northside__the_curse_of_the_dark_pharaoh_exp,
        Cards.ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)

    return base +
        dunwich +
        theKingInYellow +
        kingsport +
        theBlackGoatOfTheWoods +
        innsmouth +
        theLurkerAtTheThreshold +
        theCurseOfTheDarkPharaoh
  }

  private fun readArkhamDowntownDeck(): List<Card> {
    val base = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_downtown__base,
        Cards.ExpansionSet.BASE)
    val dunwich = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_downtown__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    val theKingInYellow = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_downtown__the_king_in_yellow_exp,
        Cards.ExpansionSet.THE_KING_IN_YELLOW)
    val kingsport = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_downtown__the_king_in_yellow_exp,
        Cards.ExpansionSet.KINGSPORT_HORROR)
    val theBlackGoatOfTheWoods = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_downtown__the_black_goat_of_the_woods_exp,
        Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    val innsmouth = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_downtown__innsmouth_horror_exp,
        Cards.ExpansionSet.INNSMOUTH_HORROR)
    val theLurkerAtTheThreshold = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_downtown__the_lurker_at_the_threshold_exp,
        Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    val theCurseOfTheDarkPharaoh = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_downtown__the_curse_of_the_dark_pharaoh_exp,
        Cards.ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)

    return base +
        dunwich +
        theKingInYellow +
        kingsport +
        theBlackGoatOfTheWoods +
        innsmouth +
        theLurkerAtTheThreshold +
        theCurseOfTheDarkPharaoh
  }

  private fun readArkhamEasttownDeck(): List<Card> {
    val base = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_easttown__base,
        Cards.ExpansionSet.BASE)
    val dunwich = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_easttown__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    val theKingInYellow = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_easttown__the_king_in_yellow_exp,
        Cards.ExpansionSet.THE_KING_IN_YELLOW)
    val kingsport = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_easttown__the_king_in_yellow_exp,
        Cards.ExpansionSet.KINGSPORT_HORROR)
    val theBlackGoatOfTheWoods = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_easttown__the_black_goat_of_the_woods_exp,
        Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    val innsmouth = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_easttown__innsmouth_horror_exp,
        Cards.ExpansionSet.INNSMOUTH_HORROR)
    val theLurkerAtTheThreshold = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_easttown__the_lurker_at_the_threshold_exp,
        Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    val theCurseOfTheDarkPharaoh = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_easttown__the_curse_of_the_dark_pharaoh_exp,
        Cards.ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)

    return base +
        dunwich +
        theKingInYellow +
        kingsport +
        theBlackGoatOfTheWoods +
        innsmouth +
        theLurkerAtTheThreshold +
        theCurseOfTheDarkPharaoh
  }

  private fun readArkhamMerchantDistrictDeck(): List<Card> {
    val base = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_merchant_district__base,
        Cards.ExpansionSet.BASE)
    val dunwich = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_merchant_district__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    val theKingInYellow = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_merchant_district__the_king_in_yellow_exp,
        Cards.ExpansionSet.THE_KING_IN_YELLOW)
    val kingsport = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_merchant_district__the_king_in_yellow_exp,
        Cards.ExpansionSet.KINGSPORT_HORROR)
    val theBlackGoatOfTheWoods = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_merchant_district__the_black_goat_of_the_woods_exp,
        Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    val innsmouth = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_merchant_district__innsmouth_horror_exp,
        Cards.ExpansionSet.INNSMOUTH_HORROR)
    val theLurkerAtTheThreshold = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_merchant_district__the_lurker_at_the_threshold_exp,
        Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    val theCurseOfTheDarkPharaoh = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_merchant_district__the_curse_of_the_dark_pharaoh_exp,
        Cards.ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)

    return base +
        dunwich +
        theKingInYellow +
        kingsport +
        theBlackGoatOfTheWoods +
        innsmouth +
        theLurkerAtTheThreshold +
        theCurseOfTheDarkPharaoh
  }

  private fun readArkhamRivertownDeck(): List<Card> {
    val base = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_rivertown__base,
        Cards.ExpansionSet.BASE)
    val dunwich = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_rivertown__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    val theKingInYellow = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_rivertown__the_king_in_yellow_exp,
        Cards.ExpansionSet.THE_KING_IN_YELLOW)
    val kingsport = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_rivertown__the_king_in_yellow_exp,
        Cards.ExpansionSet.KINGSPORT_HORROR)
    val theBlackGoatOfTheWoods = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_rivertown__the_black_goat_of_the_woods_exp,
        Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    val innsmouth = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_rivertown__innsmouth_horror_exp,
        Cards.ExpansionSet.INNSMOUTH_HORROR)
    val theLurkerAtTheThreshold = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_rivertown__the_lurker_at_the_threshold_exp,
        Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    val theCurseOfTheDarkPharaoh = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_rivertown__the_curse_of_the_dark_pharaoh_exp,
        Cards.ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)

    return base +
        dunwich +
        theKingInYellow +
        kingsport +
        theBlackGoatOfTheWoods +
        innsmouth +
        theLurkerAtTheThreshold +
        theCurseOfTheDarkPharaoh
  }

  private fun readArkhamMiskatonicUniversityDeck(): List<Card> {
    val base = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_miskatonic_university__base,
        Cards.ExpansionSet.BASE)
    val dunwich = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_miskatonic_university__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    val theKingInYellow = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_miskatonic_university__the_king_in_yellow_exp,
        Cards.ExpansionSet.THE_KING_IN_YELLOW)
    val kingsport = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_miskatonic_university__the_king_in_yellow_exp,
        Cards.ExpansionSet.KINGSPORT_HORROR)
    val theBlackGoatOfTheWoods = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_miskatonic_university__the_black_goat_of_the_woods_exp,
        Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    val innsmouth = parseNeighborhoodResource(
          R.raw.neighborhood_arkham_miskatonic_university__innsmouth_horror_exp,
          Cards.ExpansionSet.INNSMOUTH_HORROR)
    val theLurkerAtTheThreshold = parseNeighborhoodResource(
          R.raw.neighborhood_arkham_miskatonic_university__the_lurker_at_the_threshold_exp,
          Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    val theCurseOfTheDarkPharaoh = parseNeighborhoodResource(
          R.raw.neighborhood_arkham_miskatonic_university__the_curse_of_the_dark_pharaoh_exp,
          Cards.ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)

    return base +
        dunwich +
        theKingInYellow +
        kingsport +
        theBlackGoatOfTheWoods +
        innsmouth +
        theLurkerAtTheThreshold +
        theCurseOfTheDarkPharaoh
  }

  private fun readArkhamFrenchHillDeck(): List<Card> {
    val base = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_french_hill__base,
        Cards.ExpansionSet.BASE)
    val dunwich = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_french_hill__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    val theKingInYellow = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_french_hill__the_king_in_yellow_exp,
        Cards.ExpansionSet.THE_KING_IN_YELLOW)
    val kingsport = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_french_hill__the_king_in_yellow_exp,
        Cards.ExpansionSet.KINGSPORT_HORROR)
    val theBlackGoatOfTheWoods = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_french_hill__the_black_goat_of_the_woods_exp,
        Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    val innsmouth = parseNeighborhoodResource(
          R.raw.neighborhood_arkham_french_hill__innsmouth_horror_exp,
          Cards.ExpansionSet.INNSMOUTH_HORROR)
    val theLurkerAtTheThreshold = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_french_hill__the_lurker_at_the_threshold_exp,
        Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    val theCurseOfTheDarkPharaoh = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_french_hill__the_curse_of_the_dark_pharaoh_exp,
        Cards.ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)

    return base +
        dunwich +
        theKingInYellow +
        kingsport +
        theBlackGoatOfTheWoods +
        innsmouth +
        theLurkerAtTheThreshold +
        theCurseOfTheDarkPharaoh
  }

  private fun readArkhamUptownDeck(): List<Card> {
    val base = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_uptown__base,
        Cards.ExpansionSet.BASE)
    val dunwich = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_uptown__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    val theKingInYellow = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_uptown__the_king_in_yellow_exp,
        Cards.ExpansionSet.THE_KING_IN_YELLOW)
    val kingsport = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_uptown__the_king_in_yellow_exp,
        Cards.ExpansionSet.KINGSPORT_HORROR)
    val theBlackGoatOfTheWoods = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_uptown__the_black_goat_of_the_woods_exp,
        Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    val innsmouth = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_uptown__innsmouth_horror_exp,
        Cards.ExpansionSet.INNSMOUTH_HORROR)
    val theLurkerAtTheThreshold = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_uptown__the_lurker_at_the_threshold_exp,
        Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    val theCurseOfTheDarkPharaoh = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_uptown__the_curse_of_the_dark_pharaoh_exp,
        Cards.ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)

    return base +
        dunwich +
        theKingInYellow +
        kingsport +
        theBlackGoatOfTheWoods +
        innsmouth +
        theLurkerAtTheThreshold +
        theCurseOfTheDarkPharaoh
  }

  private fun readArkhamSouthsideDeck(): List<Card> {
    val base = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_southside__base,
        Cards.ExpansionSet.BASE)
    val dunwich = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_southside__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    val theKingInYellow = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_southside__the_king_in_yellow_exp,
        Cards.ExpansionSet.THE_KING_IN_YELLOW)
    val kingsport = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_southside__the_king_in_yellow_exp,
        Cards.ExpansionSet.KINGSPORT_HORROR)
    val theBlackGoatOfTheWoods = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_southside__the_black_goat_of_the_woods_exp,
        Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    val innsmouth = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_southside__innsmouth_horror_exp,
        Cards.ExpansionSet.INNSMOUTH_HORROR)
    val theLurkerAtTheThreshold = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_southside__the_lurker_at_the_threshold_exp,
        Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    val theCurseOfTheDarkPharaoh = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_southside__the_curse_of_the_dark_pharaoh_exp,
        Cards.ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)

    return base +
        dunwich +
        theKingInYellow +
        kingsport +
        theBlackGoatOfTheWoods +
        innsmouth +
        theLurkerAtTheThreshold +
        theCurseOfTheDarkPharaoh
  }
  /**
  * END Arkham locations utility
  */

  /**
  * BEGIN Dunwich location utility
  */

  private fun readDunwichBackwoodsCountryDeck(): List<Card> {
    val base = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_backwoods_country__dunwich_horror_exp,
      Cards.ExpansionSet.DUNWICH_HORROR)

    val miskatonicBase = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH)
    val miskatonicTkiy = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_tkiy,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TKIY)
    val miskatonicKh = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_kh,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    val miskatonicTbgotw = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_tbgotw,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TBGOTW)
    val miskatonicIh = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_ih,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_IH)
    val miskatonicTlatt = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_tlatt,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TLATT)
    val miskatonicTcotdp = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_tcotdp,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TCOTDP)

    return base +
      miskatonicBase +
      miskatonicTkiy +
      miskatonicKh +
      miskatonicTbgotw +
      miskatonicIh +
      miskatonicTlatt +
      miskatonicTcotdp
  }

  private fun readDunwichBlastedHeathDeck(): List<Card> {
    val base = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_blasted_heath__dunwich_horror_exp,
      Cards.ExpansionSet.DUNWICH_HORROR)

    val miskatonicBase = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH)
    val miskatonicTkiy = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_tkiy,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TKIY)
    val miskatonicKh = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_kh,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    val miskatonicTbgotw = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_tbgotw,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TBGOTW)
    val miskatonicIh = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_ih,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_IH)
    val miskatonicTlatt = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_tlatt,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TLATT)
    val miskatonicTcotdp = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_tcotdp,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TCOTDP)

    return base +
      miskatonicBase +
      miskatonicTkiy +
      miskatonicKh +
      miskatonicTbgotw +
      miskatonicIh +
      miskatonicTlatt +
      miskatonicTcotdp
  }

  private fun readDunwichVillageCommonsDeck(): List<Card> {
    val base = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_village_commons__dunwich_horror_exp,
      Cards.ExpansionSet.DUNWICH_HORROR)

    val miskatonicBase = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH)
    val miskatonicTkiy = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_tkiy,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TKIY)
    val miskatonicKh = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_kh,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    val miskatonicTbgotw = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_tbgotw,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TBGOTW)
    val miskatonicIh = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_ih,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_IH)
    val miskatonicTlatt = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_tlatt,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TLATT)
    val miskatonicTcotdp = parseNeighborhoodResource(
      R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_tcotdp,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TCOTDP)

    return base +
      miskatonicBase +
      miskatonicTkiy +
      miskatonicKh +
      miskatonicTbgotw +
      miskatonicIh +
      miskatonicTlatt +
      miskatonicTcotdp
  }
  /**
  * END Dunwich location utility
  */

  /**
  * BEGIN Kingsport location utility
  */

  private fun readKingsportCentralHillDeck(): List<Card> {
    val base = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_central_hill__kingsport_horror_exp,
      Cards.ExpansionSet.KINGSPORT_HORROR)

    val miskatonicBase = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_central_hill__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT)
    val miskatonicDh = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_central_hill__miskatonic_horror_exp_dh,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_DH)
    val miskatonicTkiy = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_central_hill__miskatonic_horror_exp_tkiy,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TKIY)
    val miskatonicTbgotw = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_central_hill__miskatonic_horror_exp_tbgotw,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TBGOTW)
    val miskatonicIh = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_central_hill__miskatonic_horror_exp_ih,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_IH)
    val miskatonicTlatt = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_central_hill__miskatonic_horror_exp_tlatt,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TLATT)
    val miskatonicTcotdp = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_central_hill__miskatonic_horror_exp_tcotdp,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TCOTDP)

    return base +
      miskatonicBase +
      miskatonicDh +
      miskatonicTkiy +
      miskatonicTbgotw +
      miskatonicIh +
      miskatonicTlatt +
      miskatonicTcotdp
  }

  private fun readKingsportHarborsideDeck(): List<Card> {
    val base = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_harborside__kingsport_horror_exp,
      Cards.ExpansionSet.KINGSPORT_HORROR)

    val miskatonicBase = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_harborside__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT)
    val miskatonicDh = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_harborside__miskatonic_horror_exp_dh,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_DH)
    val miskatonicTkiy = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_harborside__miskatonic_horror_exp_tkiy,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TKIY)
    val miskatonicTbgotw = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_harborside__miskatonic_horror_exp_tbgotw,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TBGOTW)
    val miskatonicIh = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_harborside__miskatonic_horror_exp_ih,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_IH)
    val miskatonicTlatt = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_harborside__miskatonic_horror_exp_tlatt,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TLATT)
    val miskatonicTcotdp = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_harborside__miskatonic_horror_exp_tcotdp,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TCOTDP)

    return base +
      miskatonicBase +
      miskatonicDh +
      miskatonicTkiy +
      miskatonicTbgotw +
      miskatonicIh +
      miskatonicTlatt +
      miskatonicTcotdp
  }

  private fun readKingsportSouthShoreDeck(): List<Card> {
    val base = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_south_shore__kingsport_horror_exp,
      Cards.ExpansionSet.KINGSPORT_HORROR)

    val miskatonicBase = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_south_shore__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT)
    val miskatonicDh = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_south_shore__miskatonic_horror_exp_dh,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_DH)
    val miskatonicTkiy = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_south_shore__miskatonic_horror_exp_tkiy,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TKIY)
    val miskatonicTbgotw = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_south_shore__miskatonic_horror_exp_tbgotw,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TBGOTW)
    val miskatonicIh = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_south_shore__miskatonic_horror_exp_ih,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_IH)
    val miskatonicTlatt = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_south_shore__miskatonic_horror_exp_tlatt,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TLATT)
    val miskatonicTcotdp = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_south_shore__miskatonic_horror_exp_tcotdp,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TCOTDP)

    return base +
      miskatonicBase +
      miskatonicDh +
      miskatonicTkiy +
      miskatonicTbgotw +
      miskatonicIh +
      miskatonicTlatt +
      miskatonicTcotdp
  }

  private fun readKingsportKingsportHeadDeck(): List<Card> {
    val base = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_kingsport_head__kingsport_horror_exp,
      Cards.ExpansionSet.KINGSPORT_HORROR)

    val miskatonicBase = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_kingsport_head__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT)
    val miskatonicDh = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_kingsport_head__miskatonic_horror_exp_dh,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_DH)
    val miskatonicTkiy = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_kingsport_head__miskatonic_horror_exp_tkiy,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TKIY)
    val miskatonicTbgotw = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_kingsport_head__miskatonic_horror_exp_tbgotw,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TBGOTW)
    val miskatonicIh = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_kingsport_head__miskatonic_horror_exp_ih,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_IH)
    val miskatonicTlatt = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_kingsport_head__miskatonic_horror_exp_tlatt,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TLATT)
    val miskatonicTcotdp = parseNeighborhoodResource(
      R.raw.neighborhood_kingsport_kingsport_head__miskatonic_horror_exp_tcotdp,
      Cards.ExpansionSet.MISKATONIC_HORROR_KINGSPORT_TCOTDP)

    return base +
      miskatonicBase +
      miskatonicDh +
      miskatonicTkiy +
      miskatonicTbgotw +
      miskatonicIh +
      miskatonicTlatt +
      miskatonicTcotdp
  }
  /**
  * END Kingsport location utility
  */

  /**
  * BEGIN Innsmouth location utility
  */

  private fun readInnsmouthChurchGreenDeck(): List<Card> {
    val base = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_church_green__innsmouth_horror_exp,
      Cards.ExpansionSet.INNSMOUTH_HORROR)

    val miskatonicBase = parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH)
    val miskatonicDh = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_dh,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_DH)
    val miskatonicTkiy = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_tkiy,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TKIY)
    val miskatonicKh = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_kh,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    val miskatonicTbgotw = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_tbgotw,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TBGOTW)
    val miskatonicTlatt = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_tlatt,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TLATT)
    val miskatonicTcotdp = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_tcotdp,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TCOTDP)

    return base +
      miskatonicBase +
      miskatonicDh +
      miskatonicTkiy +
      miskatonicKh +
      miskatonicTbgotw +
      miskatonicTlatt +
      miskatonicTcotdp
  }

  private fun readInnsmouthFactoryDistrictDeck(): List<Card> {
    val base = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_factory_district__innsmouth_horror_exp,
      Cards.ExpansionSet.INNSMOUTH_HORROR)

    val miskatonicBase = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH)
    val miskatonicDh = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_dh,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_DH)
    val miskatonicTkiy = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_tkiy,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TKIY)
    val miskatonicKh = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_kh,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    val miskatonicTbgotw = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_tbgotw,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TBGOTW)
    val miskatonicTlatt = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_tlatt,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TLATT)
    val miskatonicTcotdp = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_tcotdp,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TCOTDP)

    return base +
      miskatonicBase +
      miskatonicDh +
      miskatonicTkiy +
      miskatonicKh +
      miskatonicTbgotw +
      miskatonicTlatt +
      miskatonicTcotdp
  }

  private fun readInnsmouthInnsmouthShoreDeck(): List<Card> {
    val base = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_innsmouth_shore__innsmouth_horror_exp,
      Cards.ExpansionSet.INNSMOUTH_HORROR)

    val miskatonicBase = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH)
    val miskatonicDh = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_dh,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_DH)
    val miskatonicTkiy = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_tkiy,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TKIY)
    val miskatonicKh = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_kh,
      Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    val miskatonicTbgotw = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_tbgotw,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TBGOTW)
    val miskatonicTlatt = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_tlatt,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TLATT)
    val miskatonicTcotdp = parseNeighborhoodResource(
      R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_tcotdp,
      Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TCOTDP)

    return base +
      miskatonicBase +
      miskatonicDh +
      miskatonicTkiy +
      miskatonicKh +
      miskatonicTbgotw +
      miskatonicTlatt +
      miskatonicTcotdp
  }

  private fun readInnsmouthLookDeck(): List<Card> {
    val base = parseInnsmouthLookResource(
      R.raw.innsmouth_look__innsmouth_horror_exp,
      Cards.ExpansionSet.INNSMOUTH_HORROR)
    val miskatonic = parseInnsmouthLookResource(
      R.raw.innsmouth_look__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR)

    return base + miskatonic
  }

  private fun parseInnsmouthLookResource(resource: Int, expansionSet: Cards.ExpansionSet): List<InnsmouthLook> {
    val reader = readResource(resource)
    val lines = reader.readLines()
    reader.close()
    return lines.map { InnsmouthLook.parseJson(JSONObject(it), expansionSet) }
  }
  /**
  * END Innsmouth parsing utility
  */

  private fun readCultEncounterDeck(): List<Card> {
    val base = parseCultEncounterResource(
      R.raw.cult_encounter__the_black_goat_of_the_woods_exp,
      Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    val miskatonic = parseCultEncounterResource(
      R.raw.cult_encounter__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR)

    return base + miskatonic
  }

  private fun parseCultEncounterResource(resource: Int, expansionSet: Cards.ExpansionSet): List<CultEncounter> {
    val reader = readResource(resource)
    val lines = reader.readLines()
    reader.close()
    return lines.map { CultEncounter.parseJson(JSONObject(it), expansionSet) }
  }

  private fun readExhibitEncounterDeck(): List<ExhibitEncounter> {
    val base = parseExhibitEncounterResource(
      R.raw.exhibit_encounter__the_curse_of_the_dark_pharaoh_exp,
      Cards.ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)
    val miskatonic = parseExhibitEncounterResource(
      R.raw.exhibit_encounter__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR)

    return base + miskatonic
  }

  private fun parseExhibitEncounterResource(resource: Int, expansionSet: Cards.ExpansionSet): List<ExhibitEncounter> {
    val reader = readResource(resource)
    val lines = reader.readLines()
    reader.close()
    return lines.map { ExhibitEncounter.parseJson(JSONObject(it), expansionSet) }
  }

  private fun readReckoningDeck(): List<Reckoning> {
    val base = parseReckoningResource(
      R.raw.reckoning__the_lurker_at_the_threshold_exp,
      Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    val miskatonic = parseReckoningResource(
      R.raw.reckoning__miskatonic_horror_exp,
      Cards.ExpansionSet.MISKATONIC_HORROR)

    return base + miskatonic
  }

  private fun parseReckoningResource(resource: Int, expansionSet: Cards.ExpansionSet): List<Reckoning> {
    val reader = readResource(resource)
    val lines = reader.readLines()
    reader.close()
    return lines.map { Reckoning.parseJson(JSONObject(it), expansionSet) }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will automatically handle clicks on
    // the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.

    return when (item.itemId) {
      R.id.action_settings -> {
        // Start the settings activity.
        startActivity(Intent(this, SettingsActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
    if (lastDrawerPosition == position) {
      pagerAdapter?.let { setPagerAdapter(cards = shuffleCards(it.cards), it.cardType) }
    } else {
      when (position) {
        CardType.ArkhamNorthside.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(arkhamLocationsBundle.northsideDeck), CardType.ArkhamNorthside)
        }
        CardType.ArkhamDowntown.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(arkhamLocationsBundle.downtownDeck), CardType.ArkhamDowntown)
        }
        CardType.ArkhamEasttown.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(arkhamLocationsBundle.easttownDeck), CardType.ArkhamEasttown)
        }
        CardType.ArkhamMerchantDistrict.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(arkhamLocationsBundle.merchantDistrictDeck), CardType.ArkhamMerchantDistrict)
        }
        CardType.ArkhamRivertown.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(arkhamLocationsBundle.rivertownDeck), CardType.ArkhamRivertown)
        }
        CardType.ArkhamMiskatonicUniversity.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(arkhamLocationsBundle.miskatonicUniversityDeck), CardType.ArkhamMiskatonicUniversity)
        }
        CardType.ArkhamFrenchHill.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(arkhamLocationsBundle.frenchHillDeck), CardType.ArkhamFrenchHill)
        }
        CardType.ArkhamUptown.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(arkhamLocationsBundle.uptownDeck), CardType.ArkhamUptown)
        }
        CardType.ArkhamSouthside.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(arkhamLocationsBundle.southsideDeck), CardType.ArkhamSouthside)
        }
        CardType.DunwichBackwoodsCountry.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(dunwichLocationsBundle.backwoodsCountryDeck), CardType.DunwichBackwoodsCountry)
        }
        CardType.DunwichBlastedHeath.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(dunwichLocationsBundle.blastedHeathDeck), CardType.DunwichBlastedHeath)
        }
        CardType.DunwichVillageCommons.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(dunwichLocationsBundle.villageCommonsDeck), CardType.DunwichVillageCommons)
        }
        CardType.KingsportCentralHill.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(kingsportLocationsBundle.centralHillDeck), CardType.KingsportCentralHill)
        }
        CardType.KingsportHarborside.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(kingsportLocationsBundle.harborsideDeck), CardType.KingsportHarborside)
        }
        CardType.KingsportSouthshore.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(kingsportLocationsBundle.southshoreDeck), CardType.KingsportSouthshore)
        }
        CardType.KingsportKingsportHead.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(kingsportLocationsBundle.kingsportHeadDeck), CardType.KingsportKingsportHead)
        }
        CardType.InnsmouthFactoryDistrict.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(innsmouthLocationsBundle.factoryDistrictDeck), CardType.InnsmouthFactoryDistrict)
        }
        CardType.InnsmouthChurchGreen.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(innsmouthLocationsBundle.churchGreenDeck), CardType.InnsmouthChurchGreen)
        }
        CardType.InnsmouthInnsmouthShore.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(innsmouthLocationsBundle.innsmouthShoreDeck), CardType.InnsmouthInnsmouthShore)
        }
        CardType.InnsmouthInnsmouthLook.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(innsmouthLookDeck), CardType.InnsmouthInnsmouthLook)
        }
        CardType.CultEncounter.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(cultEncounterDeck), CardType.CultEncounter)
        }
        CardType.ExhibitEncounter.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(exhibitEncounterDeck), CardType.ExhibitEncounter)
        }
        CardType.Reckoning.position -> {
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(cards = shuffleCards(reckoningDeck), CardType.Reckoning)
        }
      }
      lastDrawerPosition = position
    }
    drawerLayout?.closeDrawer(drawerList)
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    // Sync the toggle state after onRestoreInstanceState has occurred.
    drawerToggle?.syncState()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    // Pass any configuration change to the drawer toggles.
    drawerToggle?.onConfigurationChanged(newConfig)
  }

  override fun onDoneItemClick() {
    pagerAdapter?.let {
      if (it.cardType == CardType.Reckoning) {
        reckoningDeck = shuffleCards(reckoningDeck)
        reckoningSeenIndex = 0
        setPagerAdapter(shuffleCards(reckoningDeck), it.cardType)
      } else {
        setPagerAdapter(shuffleCards(it.cards), it.cardType)
      }
    }
  }

  private class DeckPagerAdapter(
    fm: FragmentManager,
    val title: String,
    val cards: List<Card>,
    val cardType: CardType,
    private val mMiskatonicSetting: Boolean,
  ) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
      if (position == 0) {
        when (cardType) {
          CardType.ArkhamNorthside -> return neighborhoodTitleFragment()
          CardType.ArkhamDowntown -> return neighborhoodTitleFragment()
          CardType.ArkhamEasttown -> return neighborhoodTitleFragment()
          CardType.ArkhamMerchantDistrict -> return neighborhoodTitleFragment()
          CardType.ArkhamRivertown -> return neighborhoodTitleFragment()
          CardType.ArkhamMiskatonicUniversity -> return neighborhoodTitleFragment()
          CardType.ArkhamFrenchHill -> return neighborhoodTitleFragment()
          CardType.ArkhamUptown -> return neighborhoodTitleFragment()
          CardType.ArkhamSouthside -> return neighborhoodTitleFragment()
          CardType.DunwichBackwoodsCountry -> return neighborhoodTitleFragment()
          CardType.DunwichBlastedHeath -> return neighborhoodTitleFragment()
          CardType.DunwichVillageCommons -> return neighborhoodTitleFragment()
          CardType.KingsportCentralHill -> return neighborhoodTitleFragment()
          CardType.KingsportHarborside -> return neighborhoodTitleFragment()
          CardType.KingsportSouthshore -> return neighborhoodTitleFragment()
          CardType.KingsportKingsportHead -> return neighborhoodTitleFragment()
          CardType.InnsmouthFactoryDistrict -> return neighborhoodTitleFragment()
          CardType.InnsmouthChurchGreen -> return neighborhoodTitleFragment()
          CardType.InnsmouthInnsmouthShore -> return neighborhoodTitleFragment()
          CardType.CultEncounter -> {
            val arguments = Bundle()
            arguments.putString("title", "<b>Cult Encounter Deck</b><br/><br/>Swipe left to draw")
            val fragment = CultEncounterFragment()
            fragment.arguments = arguments
            return fragment
          }
          CardType.ExhibitEncounter -> {
            val arguments = Bundle()
            arguments.putString("title", "<b>Exhibit Encounter Deck</b><br/><br/>Swipe left to draw")
            val fragment = ExhibitEncounterFragment()
            fragment.arguments = arguments
            return fragment
          }
          CardType.InnsmouthInnsmouthLook -> {
            val arguments = Bundle()
            val fragment = InnsmouthLookFragment()
            arguments.putString("lore", "<b>Innsmouth Look Deck</b><br/><br/>Swipe left to draw")
            fragment.arguments = arguments
            return fragment
          }
          CardType.Reckoning -> {
            val arguments = Bundle()
            arguments.putString("title", "<b>Reckoning Deck</b><br/><br/>Swipe left to draw")
            val fragment = ReckoningFragment()
            fragment.arguments = arguments
            return fragment
          }
        }
      } else {
        val cardPosition = position - 1
        when (cardType) {
          CardType.ArkhamNorthside -> return neighborhoodCardFragment(cardPosition)
          CardType.ArkhamDowntown -> return neighborhoodCardFragment(cardPosition)
          CardType.ArkhamEasttown -> return neighborhoodCardFragment(cardPosition)
          CardType.ArkhamMerchantDistrict -> return neighborhoodCardFragment(cardPosition)
          CardType.ArkhamRivertown -> return neighborhoodCardFragment(cardPosition)
          CardType.ArkhamMiskatonicUniversity -> return neighborhoodCardFragment(cardPosition)
          CardType.ArkhamFrenchHill -> return neighborhoodCardFragment(cardPosition)
          CardType.ArkhamUptown -> return neighborhoodCardFragment(cardPosition)
          CardType.ArkhamSouthside -> return neighborhoodCardFragment(cardPosition)
          CardType.DunwichBackwoodsCountry -> return neighborhoodCardFragment(cardPosition)
          CardType.DunwichBlastedHeath -> return neighborhoodCardFragment(cardPosition)
          CardType.DunwichVillageCommons -> return neighborhoodCardFragment(cardPosition)
          CardType.KingsportCentralHill -> return neighborhoodCardFragment(cardPosition)
          CardType.KingsportHarborside -> return neighborhoodCardFragment(cardPosition)
          CardType.KingsportSouthshore -> return neighborhoodCardFragment(cardPosition)
          CardType.KingsportKingsportHead -> return neighborhoodCardFragment(cardPosition)
          CardType.InnsmouthFactoryDistrict -> return neighborhoodCardFragment(cardPosition)
          CardType.InnsmouthChurchGreen -> return neighborhoodCardFragment(cardPosition)
          CardType.InnsmouthInnsmouthShore -> return neighborhoodCardFragment(cardPosition)
          CardType.CultEncounter -> {
            val card = cards[cardPosition] as CultEncounter
            val arguments = CultEncounterFragment.exportCultEncounter(card)
            val fragment = CultEncounterFragment()
            fragment.arguments = arguments
            return fragment
          }
          CardType.ExhibitEncounter -> {
            val card = cards[cardPosition] as ExhibitEncounter
            val arguments = ExhibitEncounterFragment.exportExhibitEncounter(card)
            val fragment = ExhibitEncounterFragment()
            fragment.arguments = arguments
            return fragment
          }
          CardType.InnsmouthInnsmouthLook -> {
            val card = cards[cardPosition] as InnsmouthLook
            val arguments = InnsmouthLookFragment.exportInnsmouthLook(card)
            val fragment = InnsmouthLookFragment()
            fragment.arguments = arguments
            return fragment
          }
          CardType.Reckoning -> {
            val card = cards[cardPosition] as Reckoning
            val arguments = ReckoningFragment.exportReckoning(card)
            val fragment = ReckoningFragment()
            fragment.arguments = arguments
            return fragment
          }
        }
      }
    }

    private fun neighborhoodTitleFragment(): NeighborhoodThreeLocationsFragment {
      val arguments = Bundle()
      arguments.putString("title1", String.format("<b>%s</b>", title))
      arguments.putString("entry1", "Swipe left to draw")
      val fragment = NeighborhoodThreeLocationsFragment()
      fragment.arguments = arguments
      return fragment
    }

    private fun neighborhoodCardFragment(cardPosition: Int): NeighborhoodThreeLocationsFragment {
      val card = cards[cardPosition] as NeighborhoodThreeLocations
      val arguments = NeighborhoodThreeLocationsFragment.exportNeighborhood(card)
      val fragment = NeighborhoodThreeLocationsFragment()
      fragment.arguments = arguments
      return fragment
    }

    override fun getCount(): Int {
      return if (cardType == CardType.InnsmouthInnsmouthLook && mMiskatonicSetting)
        min(11, cards.size)
      else if (cardType == CardType.Reckoning && !mMiskatonicSetting)
        min(28, cards.size)
      else if (cardType == CardType.Reckoning)
        min(42, cards.size)
      else
        min(6, cards.size)
    }
  }

  private fun dunwichHorrorExpSetting(): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
      getString(R.string.dunwich_horror_expansion_title),
      false
    )
  }

  private fun theKingInYellowExpSetting(): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
      getString(R.string.the_king_in_yellow_expansion_title),
      false
    )
  }

  private fun kingsportHorrorExpSetting(): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
      getString(R.string.kingsport_horror_expansion_title),
      false
    )
  }

  private fun theBlackGoatOfTheWoodsExpSetting(): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
      getString(R.string.the_black_goat_of_the_woods_expansion_title),
      false
    )
  }

  private fun innsmouthHorrorExpSetting(): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
      getString(R.string.innsmouth_horror_expansion_title),
      false
    )
  }

  private fun theLurkerAtTheThresholdExpSetting(): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
      getString(R.string.the_lurker_at_the_threshold_expansion_title),
      false
    )
  }

  private fun theCurseOfTheDarkPharaohExpSetting(): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
      getString(R.string.the_curse_of_the_dark_pharaoh_expansion_title),
      false
    )
  }

  private fun miskatonicHorrorExpSetting(): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
      getString(R.string.miskatonic_horror_expansion_title),
      false
    )
  }

  companion object {
    private val titleStrings = listOf(
      "Arkham - Northside",
      "Arkham - Downtown",
      "Arkham - Easttown",
      "Arkham - Merchant District",
      "Arkham - Rivertown",
      "Arkham - Miskatonic University",
      "Arkham - French Hill",
      "Arkham - Uptown",
      "Arkham - Southside",
      "Dunwich - Backwoods Country",
      "Dunwich - Blasted Heath",
      "Dunwich - Village Commons",
      "Kingsport - Central Hill",
      "Kingsport - Harborside",
      "Kingsport - Southshore",
      "Kingsport - Kingsport Head",
      "Innsmouth - Factory District",
      "Innsmouth - Church Green",
      "Innsmouth - Innsmouth Shore",
      "Innsmouth - Innsmouth Look",
      "Cult Encounter",
      "Exhibit Encounter",
      "Reckoning",
    )

    private fun <T : Card> shuffleCards(cards: List<T>): List<T> {
      return cards.shuffled()
    }
  }

  enum class CardType(val title: String, val position: Int) {
    ArkhamNorthside(title = "Arkham - Northside", position = 0),
    ArkhamDowntown(title = "Arkham - Downtown", position = 1),
    ArkhamEasttown(title = "Arkham - Easttown", position = 2),
    ArkhamMerchantDistrict(title = "Arkham - Merchant District", position = 3),
    ArkhamRivertown(title = "Arkham - Rivertown", position = 4),
    ArkhamMiskatonicUniversity(title = "Arkham - Miskatonic University", position = 5),
    ArkhamFrenchHill(title = "Arkham - French Hill", position = 6),
    ArkhamUptown(title = "Arkham - Uptown", position = 7),
    ArkhamSouthside(title = "Arkham - Southside", position = 8),
    DunwichBackwoodsCountry(title = "Dunwich - Backwoods Country", position = 9),
    DunwichBlastedHeath(title = "Dunwich - Blasted Heath", position = 10),
    DunwichVillageCommons(title = "Dunwich - Village Commons", position = 11),
    KingsportCentralHill(title = "Kingsport - Central Hill", position = 12),
    KingsportHarborside(title = "Kingsport - Harborside", position = 13),
    KingsportSouthshore(title = "Kingsport - Southshore", position = 14),
    KingsportKingsportHead(title = "Kingsport - Kingsport Head", position = 15),
    InnsmouthFactoryDistrict(title = "Innsmouth - Factory District", position = 16),
    InnsmouthChurchGreen(title = "Innsmouth - Church Green", position = 17),
    InnsmouthInnsmouthShore(title = "Innsmouth - Innsmouth Shore", position = 18),
    InnsmouthInnsmouthLook(title = "Innsmouth - Innsmouth Look", position = 19),
    CultEncounter(title = "Cult Encounter", position = 20),
    ExhibitEncounter(title = "Exhibit Encounter", position = 21),
    Reckoning(title = "Reckoning", position = 22),
  }
}
