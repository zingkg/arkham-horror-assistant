package com.zingkg.arkhamhorrorboard.secondedition.app

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.zingkg.arkhamhorrorboard.secondedition.app.R

import java.io.InputStreamReader
import java.io.Reader
import java.util.Arrays
import java.util.Collections

import com.zingkg.arkhamhorrorboard.secondedition.app.fragments.CardFragment
import com.zingkg.arkhamhorrorboard.secondedition.app.fragments.CultEncounterFragment
import com.zingkg.arkhamhorrorboard.secondedition.app.fragments.ExhibitEncounterFragment
import com.zingkg.arkhamhorrorboard.secondedition.app.fragments.InnsmouthLookFragment
import com.zingkg.arkhamhorrorboard.secondedition.app.fragments.ReckoningFragment
import com.zingkg.arkhamhorrorboard.secondedition.xml.CardXML
import com.zingkg.arkhamhorrorboard.secondedition.xml.CultEncounter
import com.zingkg.arkhamhorrorboard.secondedition.xml.ExhibitEncounter
import com.zingkg.arkhamhorrorboard.secondedition.xml.InnsmouthLook
import com.zingkg.arkhamhorrorboard.secondedition.xml.Reckoning

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener, CardFragment.CardCallbacks {
    private var drawerLayout: DrawerLayout? = null
    private var drawerList: ListView? = null
    private var drawerToggle: ActionBarDrawerToggle? = null
    private var viewPager: ViewPager? = null
    private var pagerAdapter: DeckPagerAdapter? = null
    private var lastDrawerPosition: Int = 0
    private var miskatonicSetting: Boolean = false
    private var reckoningSeenIndex: Int = 0
    private var reckoningDeck: List<Reckoning> = emptyList()

    private fun miskatonicSetting(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
            getString(R.string.miskatonic_horror_expansion_title),
            false
        )
    }

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
        miskatonicSetting = miskatonicSetting()
        viewPager = findViewById(R.id.fragment_container)
        setPagerAdapter(generateCultEncounterDeck(), CultEncounter::class.java)
    }

    override fun onResume() {
        super.onResume()
        val previousMiskatonicSetting = miskatonicSetting
        miskatonicSetting = miskatonicSetting()
        // If the miskatonic setting is enabled, the miskatonic cards should be read and added. If
        // the miskatonic setting is disabled, the miskatonic cards should be removed from the set.
        if (previousMiskatonicSetting != miskatonicSetting && miskatonicSetting) {
            // Miskatonic setting is enabled.
            val miskatonic = when {
                pagerAdapter?.cardClass == CultEncounter::class.java ->
                    parseCultEncounterResource(R.raw.cult_encounter_miskatonic)
                pagerAdapter?.cardClass == ExhibitEncounter::class.java ->
                    parseExhibitEncounterResource(R.raw.exhibit_encounter_miskatonic)
                pagerAdapter?.cardClass == InnsmouthLook::class.java ->
                    parseInnsmouthLookResource(R.raw.innsmouth_look_miskatonic)
                pagerAdapter?.cardClass == Reckoning::class.java ->
                    parseReckoningResource(R.raw.reckoning_miskatonic)
                else ->
                    emptyList()
            }
            pagerAdapter?.let {
                setPagerAdapter(
                    shuffleCards(cards = it.cards + miskatonic),
                    it.cardClass
                )
            }
        } else if (previousMiskatonicSetting != miskatonicSetting) {
            // Miskatonic setting is disabled.
            pagerAdapter?.let {
                val filteredCards = it.cards.filter { it.expansionSet != CardXML.MISKATONIC }
                setPagerAdapter(shuffleCards(filteredCards), it.cardClass)
            }

        }
    }

    private fun setPagerAdapter(cards: List<CardXML>, cardClass: Class<out CardXML>) {
        pagerAdapter = DeckPagerAdapter(
            supportFragmentManager,
            cards,
            cardClass,
            miskatonicSetting
        )
        viewPager?.adapter = pagerAdapter

        if (cardClass == Reckoning::class.java)
            viewPager?.currentItem = reckoningSeenIndex
    }

    private fun setReckoningSeenIndex() {
        if (pagerAdapter?.cardClass == Reckoning::class.java)
            reckoningSeenIndex = viewPager?.currentItem ?: 0
    }

    private fun readResource(resource: Int): Reader {
        return InputStreamReader(resources.openRawResource(resource))
    }

    private fun generateCultEncounterDeck(): List<CardXML> {
        val base = parseCultEncounterResource(R.raw.cult_encounter)
        val miskatonic = if (miskatonicSetting)
            parseCultEncounterResource(R.raw.cult_encounter_miskatonic)
        else
            emptyList()

        return shuffleCards(base + miskatonic)
    }

    private fun parseCultEncounterResource(resource: Int): List<CultEncounter> {
        val reader = readResource(resource)
        val cards = CultEncounter.parseReader(reader)
        reader.close()
        return cards
    }

    private fun generateExhibitEncounterDeck(): List<ExhibitEncounter> {
        val base = parseExhibitEncounterResource(R.raw.exhibit_encounter)
        val miskatonic = if (miskatonicSetting)
            parseExhibitEncounterResource(R.raw.exhibit_encounter_miskatonic)
        else
            emptyList()

        return shuffleCards(base + miskatonic)
    }

    private fun parseExhibitEncounterResource(resource: Int): List<ExhibitEncounter> {
        val reader = readResource(resource)
        val cards = ExhibitEncounter.parseReader(reader)
        reader.close()
        return cards
    }

    private fun generateInnsmouthLookDeck(): List<CardXML> {
        val base = parseInnsmouthLookResource(R.raw.innsmouth_look)
        val miskatonic = if (miskatonicSetting)
            parseInnsmouthLookResource(R.raw.innsmouth_look_miskatonic)
        else
            emptyList()

        return shuffleCards(base + miskatonic)
    }

    private fun parseInnsmouthLookResource(resource: Int): List<InnsmouthLook> {
        val reader = readResource(resource)
        val cards = InnsmouthLook.parseReader(reader)
        reader.close()
        return cards
    }

    private fun generateReckoningDeck(): List<Reckoning> {
        val base = parseReckoningResource(R.raw.reckoning)
        val miskatonic = if (miskatonicSetting)
            parseReckoningResource(R.raw.reckoning_miskatonic)
        else
            emptyList()

        return shuffleCards(base + miskatonic)
    }

    private fun parseReckoningResource(resource: Int): List<Reckoning> {
        val reader = readResource(resource)
        val cards = Reckoning.parseReader(reader)
        reader.close()
        return cards
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
            pagerAdapter?.let { setPagerAdapter(shuffleCards(it.cards), it.cardClass) }
        } else {
            when (position) {
                0 -> {
                    setReckoningSeenIndex()
                    setPagerAdapter(generateCultEncounterDeck(), CultEncounter::class.java)
                    supportActionBar?.title = titleStrings[position]
                }
                1 -> {
                    setReckoningSeenIndex()
                    setPagerAdapter(generateExhibitEncounterDeck(), ExhibitEncounter::class.java)
                    supportActionBar?.title = titleStrings[position]
                }
                2 -> {
                    setReckoningSeenIndex()
                    setPagerAdapter(generateInnsmouthLookDeck(), InnsmouthLook::class.java)
                    supportActionBar?.title = titleStrings[position]
                }
                3 -> {
                    if (reckoningDeck.isEmpty())
                        reckoningDeck = generateReckoningDeck()

                    setPagerAdapter(reckoningDeck, Reckoning::class.java)
                    supportActionBar?.title = titleStrings[position]
                }
                4 ->
                    startActivity(Intent(this, SettingsActivity::class.java))
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
            if (it.cardClass == Reckoning::class.java) {
                reckoningDeck = shuffleCards(reckoningDeck)
                reckoningSeenIndex = 0
                setPagerAdapter(shuffleCards(reckoningDeck), it.cardClass)
            } else {
                setPagerAdapter(shuffleCards(it.cards), it.cardClass)
            }
        }
    }

    private class DeckPagerAdapter(
        fm: FragmentManager,
        val cards: List<CardXML>,
        val cardClass: Class<out CardXML>,
        private val mMiskatonicSetting: Boolean
    ) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            if (position == 0) {
                when (cardClass) {
                    CultEncounter::class.java -> {
                        val arguments = Bundle()
                        arguments.putString("title", "Cult Encounter Deck")
                        val fragment = CultEncounterFragment()
                        fragment.arguments = arguments
                        return fragment
                    }
                    ExhibitEncounter::class.java -> {
                        val arguments = Bundle()
                        arguments.putString("title", "Exhibit Encounter Deck")
                        val fragment = ExhibitEncounterFragment()
                        fragment.arguments = arguments
                        return fragment
                    }
                    InnsmouthLook::class.java -> {
                        val arguments = Bundle()
                        val fragment = InnsmouthLookFragment()
                        arguments.putString("lore", "Innsmouth Look Deck")
                        fragment.arguments = arguments
                        return fragment
                    }
                    Reckoning::class.java -> {
                        val arguments = Bundle()
                        arguments.putString("title", "Reckoning Deck")
                        val fragment = ReckoningFragment()
                        fragment.arguments = arguments
                        return fragment
                    }
                    else -> throw RuntimeException(cardClass.name + " does not have a match")
                }
            } else {
                val cardPosition = position - 1
                when (cardClass) {
                    CultEncounter::class.java -> {
                        val card = cards[cardPosition] as CultEncounter
                        val arguments = CultEncounterFragment.exportCultEncounter(card)
                        val fragment = CultEncounterFragment()
                        fragment.arguments = arguments
                        return fragment
                    }
                    ExhibitEncounter::class.java -> {
                        val card = cards[cardPosition] as ExhibitEncounter
                        val arguments = ExhibitEncounterFragment.exportExhibitEncounter(card)
                        val fragment = ExhibitEncounterFragment()
                        fragment.arguments = arguments
                        return fragment
                    }
                    InnsmouthLook::class.java -> {
                        val card = cards[cardPosition] as InnsmouthLook
                        val arguments = InnsmouthLookFragment.exportInnsmouthLook(card)
                        val fragment = InnsmouthLookFragment()
                        fragment.arguments = arguments
                        return fragment
                    }
                    Reckoning::class.java -> {
                        val card = cards[cardPosition] as Reckoning
                        val arguments = ReckoningFragment.exportReckoning(card)
                        val fragment = ReckoningFragment()
                        fragment.arguments = arguments
                        return fragment
                    }
                    else -> throw RuntimeException(cardClass.name + " does not have a match")
                }
            }
        }

        override fun getCount(): Int {
            return if (cardClass == InnsmouthLook::class.java && mMiskatonicSetting)
                if (11 < cards.size) 11 else cards.size
            else if (cardClass == Reckoning::class.java && !mMiskatonicSetting)
                if (28 < cards.size) 28 else cards.size
            else if (cardClass == Reckoning::class.java && mMiskatonicSetting)
                if (42 < cards.size) 42 else cards.size
            else
                if (6 < cards.size) 6 else cards.size
        }
    }

    companion object {
        private val titleStrings = Arrays.asList(
            "Cult Encounter",
            "Exhibit Encounter",
            "Innsmouth Look",
            "Reckoning",
            "Settings"
        )

        private fun <T : CardXML> shuffleCards(cards: List<T>): List<T> {
            val shuffledCards = cards.map { it }
            Collections.shuffle(shuffledCards)
            return shuffledCards
        }
    }
}
