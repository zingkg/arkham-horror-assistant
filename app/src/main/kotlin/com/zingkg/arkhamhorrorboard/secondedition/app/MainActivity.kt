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
import com.zingkg.arkhamhorrorboard.secondedition.card.Card
import com.zingkg.arkhamhorrorboard.secondedition.card.Cards
import com.zingkg.arkhamhorrorboard.secondedition.card.CultEncounter
import com.zingkg.arkhamhorrorboard.secondedition.card.ExhibitEncounter
import com.zingkg.arkhamhorrorboard.secondedition.card.InnsmouthLook
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

  private var arkhamNorthsideDeck: List<Card> = emptyList()
  private var arkhamDowntownDeck: List<Card> = emptyList()
  private var arkhamEasttownDeck: List<Card> = emptyList()
  private var arkhamMerchantDistrictDeck: List<Card> = emptyList()
  private var arkhamRivertownDeck: List<Card> = emptyList()
  private var arkhamMiskatonicUniversityDeck: List<Card> = emptyList()
  private var arkhamFrenchHillDeck: List<Card> = emptyList()
  private var arkhamUptownDeck: List<Card> = emptyList()
  private var arkhamSouthsideDeck: List<Card> = emptyList()

  private var previousDunwichHorrorExpSetting: Boolean = false
  private var dunwichHorrorExpSetting: Boolean = false
  private var dunwichBackwoodsCountryDeck: List<Card> = emptyList()
  private var dunwichBlastedHeathDeck: List<Card> = emptyList()
  private var dunwichVillageCommonsDeck: List<Card> = emptyList()

  private var previousTheKingInYellowExpSetting: Boolean = false
  private var theKingInYellowExpSetting: Boolean = false

  private var previousKingsportHorrorExpSetting: Boolean = false
  private var kingsportHorrorExpSetting: Boolean = false
  private var kingsportCentralHillDeck: List<Card> = emptyList()
  private var kingsportHarborsideDeck: List<Card> = emptyList()
  private var kingsportSouthshoreDeck: List<Card> = emptyList()
  private var kingsportKingsportHeadDeck: List<Card> = emptyList()

  private var previousTheBlackGoatOfTheWoodsExpSetting: Boolean = false
  private var theBlackGoatOfTheWoodsExpSetting: Boolean = false
  private var cultEncounterDeck: List<Card> = emptyList()

  private var previousInnsmouthHorrorExpSetting: Boolean = false
  private var innsmouthHorrorExpSetting: Boolean = false
  private var innsmouthFactoryDistrictDeck: List<Card> = emptyList()
  private var innsmouthChurchGreenDeck: List<Card> = emptyList()
  private var innsmouthInnsmouthShoreDeck: List<Card> = emptyList()
  private var innsmouthLookDeck: List<Card> = emptyList()

  private var previousTheLurkerAtTheThresholdExpSetting: Boolean = false
  private var theLurkerAtTheThresholdExpSetting: Boolean = false
  private var reckoningSeenIndex: Int = 0
  private var reckoningDeck: List<Reckoning> = emptyList()

  private var previousTheCurseOfTheDarkPharaohExpSetting: Boolean = false
  private var theCurseOfTheDarkPharaohExpSetting: Boolean = false
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

    // ActionBarDrawerToggle ties together the the proper interactions
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
    readAllCards(force = true)
    viewPager = findViewById(R.id.fragment_container)
    setPagerAdapter(shuffleCards(arkhamNorthsideDeck), CardType.ArkhamNorthside)
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
    // Settings likely changed. Read all files and regenerate all of the decks
    readAllCards(force = false)
    // Set the pagerAdapter to the assigned cards
    pagerAdapter?.let {
      when (it.cardType) {
        CardType.ArkhamNorthside -> setPagerAdapter(
          shuffleCards(arkhamNorthsideDeck),
          CardType.ArkhamNorthside)
        CardType.ArkhamDowntown -> setPagerAdapter(
          shuffleCards(arkhamDowntownDeck),
          CardType.ArkhamDowntown)
        CardType.ArkhamEasttown -> setPagerAdapter(
          shuffleCards(arkhamEasttownDeck),
          CardType.ArkhamEasttown)
        CardType.ArkhamMerchantDistrict -> setPagerAdapter(
          shuffleCards(arkhamMerchantDistrictDeck),
          CardType.ArkhamMerchantDistrict)
        CardType.ArkhamRivertown -> setPagerAdapter(
          shuffleCards(arkhamRivertownDeck),
          CardType.ArkhamRivertown)
        CardType.ArkhamMiskatonicUniversity -> setPagerAdapter(
          shuffleCards(arkhamMiskatonicUniversityDeck),
          CardType.ArkhamMiskatonicUniversity)
        CardType.ArkhamFrenchHill -> setPagerAdapter(
          shuffleCards(arkhamFrenchHillDeck),
          CardType.ArkhamFrenchHill)
        CardType.ArkhamUptown -> setPagerAdapter(
          shuffleCards(arkhamUptownDeck),
          CardType.ArkhamUptown)
        CardType.ArkhamSouthside -> setPagerAdapter(
          shuffleCards(arkhamSouthsideDeck),
          CardType.ArkhamSouthside)
        CardType.DunwichBackwoodsCountry -> setPagerAdapter(
          shuffleCards(dunwichBackwoodsCountryDeck),
          CardType.DunwichBackwoodsCountry)
        CardType.DunwichBlastedHeath -> setPagerAdapter(
          shuffleCards(dunwichBlastedHeathDeck),
          CardType.DunwichBlastedHeath)
        CardType.DunwichVillageCommons -> setPagerAdapter(
          shuffleCards(dunwichVillageCommonsDeck),
          CardType.DunwichVillageCommons)
        CardType.KingsportCentralHill -> setPagerAdapter(
          shuffleCards(kingsportCentralHillDeck),
          CardType.KingsportCentralHill)
        CardType.KingsportHarborside -> setPagerAdapter(
          shuffleCards(kingsportHarborsideDeck),
          CardType.KingsportHarborside)
        CardType.KingsportSouthshore -> setPagerAdapter(
          shuffleCards(kingsportSouthshoreDeck),
          CardType.KingsportSouthshore)
        CardType.KingsportKingsportHead -> setPagerAdapter(
          shuffleCards(kingsportKingsportHeadDeck),
          CardType.KingsportKingsportHead)
        CardType.InnsmouthFactoryDistrict -> setPagerAdapter(
          shuffleCards(innsmouthFactoryDistrictDeck),
          CardType.InnsmouthFactoryDistrict)
        CardType.InnsmouthChurchGreen -> setPagerAdapter(
          shuffleCards(innsmouthChurchGreenDeck),
          CardType.InnsmouthChurchGreen)
        CardType.InnsmouthInnsmouthShore -> setPagerAdapter(
          shuffleCards(innsmouthInnsmouthShoreDeck),
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

  private fun readAllCards(force: Boolean) {
    // Check Arkham Neighborhoods
    if (previousTheKingInYellowExpSetting != theKingInYellowExpSetting ||
      previousTheBlackGoatOfTheWoodsExpSetting != theBlackGoatOfTheWoodsExpSetting ||
      previousTheLurkerAtTheThresholdExpSetting != theLurkerAtTheThresholdExpSetting ||
      previousTheCurseOfTheDarkPharaohExpSetting != theCurseOfTheDarkPharaohExpSetting ||
      previousMiskatonicHorrorExpSetting != miskatonicHorrorExpSetting ||
      force) {
      arkhamNorthsideDeck = readArkhamNorthsideDeck()
      arkhamDowntownDeck = readArkhamDowntownDeck()
      arkhamEasttownDeck = emptyList()
      arkhamMerchantDistrictDeck = emptyList()
      arkhamRivertownDeck = emptyList()
      arkhamMiskatonicUniversityDeck = emptyList()
      arkhamFrenchHillDeck = emptyList()
      arkhamUptownDeck = emptyList()
      arkhamSouthsideDeck = emptyList()
    }

    if ((dunwichHorrorExpSetting && !previousDunwichHorrorExpSetting) ||
      (dunwichHorrorExpSetting && previousMiskatonicHorrorExpSetting != miskatonicHorrorExpSetting) ||
      force) {
      dunwichBackwoodsCountryDeck = readDunwichBackwoodsCountryDeck()
      dunwichBlastedHeathDeck = readDunwichBlastedHeathDeck()
      dunwichVillageCommonsDeck = readDunwichVillageCommonsDeck()
    }

    if ((kingsportHorrorExpSetting && !previousKingsportHorrorExpSetting) ||
      (kingsportHorrorExpSetting && previousMiskatonicHorrorExpSetting != miskatonicHorrorExpSetting) ||
      force) {
      kingsportCentralHillDeck = emptyList()
      kingsportHarborsideDeck = emptyList()
      kingsportSouthshoreDeck = emptyList()
      kingsportKingsportHeadDeck = emptyList()
    }

    if ((innsmouthHorrorExpSetting && !previousInnsmouthHorrorExpSetting) ||
      (innsmouthHorrorExpSetting && previousMiskatonicHorrorExpSetting != miskatonicHorrorExpSetting) ||
      force) {
      innsmouthFactoryDistrictDeck = readInnsmouthFactoryDistrictDeck()
      innsmouthChurchGreenDeck = readInnsmouthChurchGreenDeck()
      innsmouthInnsmouthShoreDeck = readInnsmouthInnsmouthShoreDeck()
    }

    if (previousMiskatonicHorrorExpSetting != miskatonicHorrorExpSetting || force) {
      cultEncounterDeck = readCultEncounterDeck()
      innsmouthLookDeck = readInnsmouthLookDeck()
      exhibitEncounterDeck = readExhibitEncounterDeck()
      reckoningSeenIndex = 0
      reckoningDeck = readReckoningDeck()
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

  private fun readArkhamNorthsideDeck(): List<Card> {
    // TODO
    return emptyList()
  }

  private fun readArkhamDowntownDeck(): List<Card> {
    val base = parseNeighborhoodResource(
        R.raw.neighborhood_arkham_downtown__base,
        Cards.ExpansionSet.BASE)
    val dunwich = if (dunwichHorrorExpSetting) {
      parseNeighborhoodResource(
          R.raw.neighborhood_arkham_downtown__dunwich_horror_exp,
          Cards.ExpansionSet.DUNWICH_HORROR)
    } else {
      emptyList()
    }
    val theKingInYellow = if (theKingInYellowExpSetting) {
      parseNeighborhoodResource(
          R.raw.neighborhood_arkham_downtown__the_king_in_yellow_exp,
          Cards.ExpansionSet.THE_KING_IN_YELLOW)
    } else {
      emptyList()
    }
    val kingsport = if (kingsportHorrorExpSetting) {
      parseNeighborhoodResource(
          R.raw.neighborhood_arkham_downtown__the_king_in_yellow_exp,
          Cards.ExpansionSet.KINGSPORT_HORROR)
    } else {
      emptyList()
    }
    val theBlackGoatOfTheWoods = if (theBlackGoatOfTheWoodsExpSetting) {
      parseNeighborhoodResource(
          R.raw.neighborhood_arkham_downtown__the_black_goat_of_the_woods_exp,
          Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    } else {
      emptyList()
    }
    val innsmouth = if (innsmouthHorrorExpSetting) {
      parseNeighborhoodResource(
          R.raw.neighborhood_arkham_downtown__innsmouth_horror_exp,
          Cards.ExpansionSet.INNSMOUTH_HORROR)
    } else {
      emptyList()
    }
    val theLurkerAtTheThreshold = if (theLurkerAtTheThresholdExpSetting) {
      parseNeighborhoodResource(
          R.raw.neighborhood_arkham_downtown__the_lurker_at_the_threshold_exp,
          Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    } else {
      emptyList()
    }
    val theCurseOfTheDarkPharaoh: List<NeighborhoodThreeLocations> = if (theCurseOfTheDarkPharaohExpSetting) {
      parseNeighborhoodResource(
          R.raw.neighborhood_arkham_downtown__the_curse_of_the_dark_pharaoh_exp,
          Cards.ExpansionSet.THE_CURSE_OF_THE_DARK_PHARAOH)
    } else {
      emptyList()
    }

    return base +
        dunwich +
        theKingInYellow +
        kingsport +
        theBlackGoatOfTheWoods +
        innsmouth +
        theLurkerAtTheThreshold +
        theCurseOfTheDarkPharaoh
  }

  private fun parseNeighborhoodResource(resource: Int, expansionSet: Cards.ExpansionSet): List<NeighborhoodThreeLocations> {
    val reader = readResource(resource)
    val lines = reader.readLines()
    reader.close()
    return lines.map { NeighborhoodThreeLocations.parseJson(JSONObject(it), expansionSet) }
  }

  private fun readDunwichBackwoodsCountryDeck(): List<Card> {
    val base = if (dunwichHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_backwoods_country__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    } else {
      emptyList()
    }

    val miskatonicBase = if (dunwichHorrorExpSetting && miskatonicHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH)
    } else {
      emptyList()
    }
    val miskatonicTkiy = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theKingInYellowExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_tkiy,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TKIY)
    } else {
      emptyList()
    }
    val miskatonicKh = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      kingsportHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_kh,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    } else {
      emptyList()
    }
    val miskatonicTbgotw = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theBlackGoatOfTheWoodsExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_tbgotw,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TBGOTW)
    } else {
      emptyList()
    }
    val miskatonicIh = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      innsmouthHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_ih,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_IH)
    } else {
      emptyList()
    }
    val miskatonicTlatt = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theLurkerAtTheThresholdExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_tlatt,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TLATT)
    } else {
      emptyList()
    }
    val miskatonicTcotdp = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theCurseOfTheDarkPharaohExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_backwoods_country__miskatonic_horror_exp_tcotdp,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TCOTDP)
    } else {
      emptyList()
    }
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
    val base = if (dunwichHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_blasted_heath__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    } else {
      emptyList()
    }

    val miskatonicBase = if (dunwichHorrorExpSetting && miskatonicHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH)
    } else {
      emptyList()
    }
    val miskatonicTkiy = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theKingInYellowExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_tkiy,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TKIY)
    } else {
      emptyList()
    }
    val miskatonicKh = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      kingsportHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_kh,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    } else {
      emptyList()
    }
    val miskatonicTbgotw = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theBlackGoatOfTheWoodsExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_tbgotw,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TBGOTW)
    } else {
      emptyList()
    }
    val miskatonicIh = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      innsmouthHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_ih,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_IH)
    } else {
      emptyList()
    }
    val miskatonicTlatt = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theLurkerAtTheThresholdExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_tlatt,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TLATT)
    } else {
      emptyList()
    }
    val miskatonicTcotdp = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theCurseOfTheDarkPharaohExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_blasted_heath__miskatonic_horror_exp_tcotdp,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TCOTDP)
    } else {
      emptyList()
    }
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
    val base = if (dunwichHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_village_commons__dunwich_horror_exp,
        Cards.ExpansionSet.DUNWICH_HORROR)
    } else {
      emptyList()
    }

    val miskatonicBase = if (dunwichHorrorExpSetting && miskatonicHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH)
    } else {
      emptyList()
    }
    val miskatonicTkiy = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theKingInYellowExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_tkiy,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TKIY)
    } else {
      emptyList()
    }
    val miskatonicKh = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      kingsportHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_kh,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    } else {
      emptyList()
    }
    val miskatonicTbgotw = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theBlackGoatOfTheWoodsExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_tbgotw,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TBGOTW)
    } else {
      emptyList()
    }
    val miskatonicIh = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      innsmouthHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_ih,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_IH)
    } else {
      emptyList()
    }
    val miskatonicTlatt = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theLurkerAtTheThresholdExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_tlatt,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TLATT)
    } else {
      emptyList()
    }
    val miskatonicTcotdp = if (dunwichHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theCurseOfTheDarkPharaohExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_dunwich_village_commons__miskatonic_horror_exp_tcotdp,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_TCOTDP)
    } else {
      emptyList()
    }
    return base +
      miskatonicBase +
      miskatonicTkiy +
      miskatonicKh +
      miskatonicTbgotw +
      miskatonicIh +
      miskatonicTlatt +
      miskatonicTcotdp
  }

  private fun readInnsmouthChurchGreenDeck(): List<Card> {
    val base = if (innsmouthHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_church_green__innsmouth_horror_exp,
        Cards.ExpansionSet.INNSMOUTH_HORROR)
    } else {
      emptyList()
    }

    val miskatonicBase = if (innsmouthHorrorExpSetting && miskatonicHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH)
    } else {
      emptyList()
    }
    val miskatonicDh = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      dunwichHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_dh,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_DH)
    } else {
      emptyList()
    }
    val miskatonicTkiy = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theKingInYellowExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_tkiy,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TKIY)
    } else {
      emptyList()
    }
    val miskatonicKh = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      kingsportHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_kh,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    } else {
      emptyList()
    }
    val miskatonicTbgotw = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theBlackGoatOfTheWoodsExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_tbgotw,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TBGOTW)
    } else {
      emptyList()
    }
    val miskatonicTlatt = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theLurkerAtTheThresholdExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_tlatt,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TLATT)
    } else {
      emptyList()
    }
    val miskatonicTcotdp = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theCurseOfTheDarkPharaohExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_church_green__miskatonic_horror_exp_tcotdp,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TCOTDP)
    } else {
      emptyList()
    }
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
    val base = if (innsmouthHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_factory_district__innsmouth_horror_exp,
        Cards.ExpansionSet.INNSMOUTH_HORROR)
    } else {
      emptyList()
    }

    val miskatonicBase = if (innsmouthHorrorExpSetting && miskatonicHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH)
    } else {
      emptyList()
    }
    val miskatonicDh = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      dunwichHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_dh,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_DH)
    } else {
      emptyList()
    }
    val miskatonicTkiy = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theKingInYellowExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_tkiy,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TKIY)
    } else {
      emptyList()
    }
    val miskatonicKh = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      kingsportHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_kh,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    } else {
      emptyList()
    }
    val miskatonicTbgotw = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theBlackGoatOfTheWoodsExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_tbgotw,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TBGOTW)
    } else {
      emptyList()
    }
    val miskatonicTlatt = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theLurkerAtTheThresholdExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_tlatt,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TLATT)
    } else {
      emptyList()
    }
    val miskatonicTcotdp = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theCurseOfTheDarkPharaohExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_factory_district__miskatonic_horror_exp_tcotdp,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TCOTDP)
    } else {
      emptyList()
    }
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
    val base = if (innsmouthHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_innsmouth_shore__innsmouth_horror_exp,
        Cards.ExpansionSet.INNSMOUTH_HORROR)
    } else {
      emptyList()
    }

    val miskatonicBase = if (innsmouthHorrorExpSetting && miskatonicHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH)
    } else {
      emptyList()
    }
    val miskatonicDh = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      dunwichHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_dh,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_DH)
    } else {
      emptyList()
    }
    val miskatonicTkiy = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theKingInYellowExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_tkiy,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TKIY)
    } else {
      emptyList()
    }
    val miskatonicKh = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      kingsportHorrorExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_kh,
        Cards.ExpansionSet.MISKATONIC_HORROR_DUNWICH_KH)
    } else {
      emptyList()
    }
    val miskatonicTbgotw = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theBlackGoatOfTheWoodsExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_tbgotw,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TBGOTW)
    } else {
      emptyList()
    }
    val miskatonicTlatt = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theLurkerAtTheThresholdExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_tlatt,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TLATT)
    } else {
      emptyList()
    }
    val miskatonicTcotdp = if (innsmouthHorrorExpSetting &&
      miskatonicHorrorExpSetting &&
      theCurseOfTheDarkPharaohExpSetting) {
      parseNeighborhoodResource(
        R.raw.neighborhood_innsmouth_innsmouth_shore__miskatonic_horror_exp_tcotdp,
        Cards.ExpansionSet.MISKATONIC_HORROR_INNSMOUTH_TCOTDP)
    } else {
      emptyList()
    }
    return base +
      miskatonicBase +
      miskatonicDh +
      miskatonicTkiy +
      miskatonicKh +
      miskatonicTbgotw +
      miskatonicTlatt +
      miskatonicTcotdp
  }

  private fun readCultEncounterDeck(): List<Card> {
    val base = parseCultEncounterResource(
        R.raw.cult_encounter__the_black_goat_of_the_woods_exp,
        Cards.ExpansionSet.THE_BLACK_GOAT_OF_THE_WOODS)
    val miskatonic = if (miskatonicHorrorExpSetting) {
      parseCultEncounterResource(
          R.raw.cult_encounter__miskatonic_horror_exp,
          Cards.ExpansionSet.MISKATONIC_HORROR)
    } else {
      emptyList()
    }

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
    val miskatonic = if (miskatonicHorrorExpSetting) {
      parseExhibitEncounterResource(
          R.raw.exhibit_encounter__miskatonic_horror_exp,
          Cards.ExpansionSet.MISKATONIC_HORROR)
    } else {
      emptyList()
    }

    return base + miskatonic
  }

  private fun parseExhibitEncounterResource(resource: Int, expansionSet: Cards.ExpansionSet): List<ExhibitEncounter> {
    val reader = readResource(resource)
    val lines = reader.readLines()
    reader.close()
    return lines.map { ExhibitEncounter.parseJson(JSONObject(it), expansionSet) }
  }

  private fun readInnsmouthLookDeck(): List<Card> {
    val base = parseInnsmouthLookResource(
        R.raw.innsmouth_look__innsmouth_horror_exp,
        Cards.ExpansionSet.INNSMOUTH_HORROR)
    val miskatonic = if (miskatonicHorrorExpSetting)
      parseInnsmouthLookResource(
          R.raw.innsmouth_look__miskatonic_horror_exp,
          Cards.ExpansionSet.MISKATONIC_HORROR)
    else
      emptyList()

    return base + miskatonic
  }

  private fun parseInnsmouthLookResource(resource: Int, expansionSet: Cards.ExpansionSet): List<InnsmouthLook> {
    val reader = readResource(resource)
    val lines = reader.readLines()
    reader.close()
    return lines.map { InnsmouthLook.parseJson(JSONObject(it), expansionSet) }
  }

  private fun readReckoningDeck(): List<Reckoning> {
    val base = parseReckoningResource(
        R.raw.reckoning__the_lurker_at_the_threshold_exp,
        Cards.ExpansionSet.THE_LURKER_AT_THE_THRESHOLD)
    val miskatonic = if (miskatonicHorrorExpSetting) {
      parseReckoningResource(
          R.raw.reckoning__miskatonic_horror_exp,
          Cards.ExpansionSet.MISKATONIC_HORROR)
    } else {
      emptyList()
    }

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
      pagerAdapter?.let { setPagerAdapter(shuffleCards(it.cards), it.cardType) }
    } else {
      when (position) {
        CardType.ArkhamNorthside.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(arkhamNorthsideDeck), CardType.ArkhamNorthside)
        }
        CardType.ArkhamDowntown.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(arkhamDowntownDeck), CardType.ArkhamDowntown)
        }
        CardType.ArkhamEasttown.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(arkhamEasttownDeck), CardType.ArkhamEasttown)
        }
        CardType.ArkhamMerchantDistrict.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(arkhamMerchantDistrictDeck), CardType.ArkhamMerchantDistrict)
        }
        CardType.ArkhamRivertown.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(arkhamRivertownDeck), CardType.ArkhamRivertown)
        }
        CardType.ArkhamMiskatonicUniversity.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(arkhamMiskatonicUniversityDeck), CardType.ArkhamMiskatonicUniversity)
        }
        CardType.ArkhamFrenchHill.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(arkhamFrenchHillDeck), CardType.ArkhamFrenchHill)
        }
        CardType.ArkhamUptown.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(arkhamUptownDeck), CardType.ArkhamUptown)
        }
        CardType.ArkhamSouthside.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(arkhamSouthsideDeck), CardType.ArkhamSouthside)
        }
        CardType.DunwichBackwoodsCountry.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(dunwichBackwoodsCountryDeck), CardType.DunwichBackwoodsCountry)
        }
        CardType.DunwichBlastedHeath.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(dunwichBlastedHeathDeck), CardType.DunwichBlastedHeath)
        }
        CardType.DunwichVillageCommons.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(dunwichVillageCommonsDeck), CardType.DunwichVillageCommons)
        }
        CardType.KingsportCentralHill.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(kingsportCentralHillDeck), CardType.KingsportCentralHill)
        }
        CardType.KingsportHarborside.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(kingsportHarborsideDeck), CardType.KingsportHarborside)
        }
        CardType.KingsportSouthshore.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(kingsportSouthshoreDeck), CardType.KingsportSouthshore)
        }
        CardType.KingsportKingsportHead.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(kingsportKingsportHeadDeck), CardType.KingsportKingsportHead)
        }
        CardType.InnsmouthFactoryDistrict.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(innsmouthFactoryDistrictDeck), CardType.InnsmouthFactoryDistrict)
        }
        CardType.InnsmouthChurchGreen.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(innsmouthChurchGreenDeck), CardType.InnsmouthChurchGreen)
        }
        CardType.InnsmouthInnsmouthShore.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(innsmouthInnsmouthShoreDeck), CardType.InnsmouthInnsmouthShore)
        }
        CardType.InnsmouthInnsmouthLook.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(innsmouthLookDeck), CardType.InnsmouthInnsmouthLook)
        }
        CardType.CultEncounter.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(cultEncounterDeck), CardType.CultEncounter)
        }
        CardType.ExhibitEncounter.position -> {
          setReckoningSeenIndex()
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(exhibitEncounterDeck), CardType.ExhibitEncounter)
        }
        CardType.Reckoning.position -> {
          supportActionBar?.title = titleStrings[position]
          setPagerAdapter(shuffleCards(reckoningDeck), CardType.Reckoning)
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
    ArkhamNorthside("Arkham - Northside", 0),
    ArkhamDowntown("Arkham - Downtown", 1),
    ArkhamEasttown("Arkham - Easttown", 2),
    ArkhamMerchantDistrict("Arkham - Merchant District", 3),
    ArkhamRivertown("Arkham - Rivertown", 4),
    ArkhamMiskatonicUniversity("Arkham - Miskatonic University", 5),
    ArkhamFrenchHill("Arkham - French Hill", 6),
    ArkhamUptown("Arkham - Uptown", 7),
    ArkhamSouthside("Arkham - Southside", 8),
    DunwichBackwoodsCountry("Dunwich - Backwoods Country", 9),
    DunwichBlastedHeath("Dunwich - Blasted Heath", 10),
    DunwichVillageCommons("Dunwich - Village Commons", 11),
    KingsportCentralHill("Kingsport - Central Hill", 12),
    KingsportHarborside("Kingsport - Harborside", 13),
    KingsportSouthshore("Kingsport - Southshore", 14),
    KingsportKingsportHead("Kingsport - Kingsport Head", 15),
    InnsmouthFactoryDistrict("Innsmouth - Factory District", 16),
    InnsmouthChurchGreen("Innsmouth - Church Green", 17),
    InnsmouthInnsmouthShore("Innsmouth - Innsmouth Shore", 18),
    InnsmouthInnsmouthLook("Innsmouth - Innsmouth Look", 19),
    CultEncounter("Cult Encounter", 20),
    ExhibitEncounter("Exhibit Encounter", 21),
    Reckoning("Reckoning", 22),
  }
}
