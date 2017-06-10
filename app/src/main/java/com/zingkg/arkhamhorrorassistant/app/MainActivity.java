package com.zingkg.arkhamhorrorassistant.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.zingkg.arkhamhorrorassistant.app.fragments.CardFragment;
import com.zingkg.arkhamhorrorassistant.app.fragments.CultEncounterFragment;
import com.zingkg.arkhamhorrorassistant.app.fragments.ExhibitEncounterFragment;
import com.zingkg.arkhamhorrorassistant.app.fragments.InnsmouthLookFragment;
import com.zingkg.arkhamhorrorassistant.app.fragments.ReckoningFragment;
import com.zingkg.arkhamhorrorassistant.xml.CardXML;
import com.zingkg.arkhamhorrorassistant.xml.CultEncounter;
import com.zingkg.arkhamhorrorassistant.xml.ExhibitEncounter;
import com.zingkg.arkhamhorrorassistant.xml.InnsmouthLook;
import com.zingkg.arkhamhorrorassistant.xml.Reckoning;

import org.xmlpull.v1.XmlPullParserException;

/**
 * Main activity that contains the fragments representing cards.
 */
public class MainActivity
    extends AppCompatActivity
    implements ListView.OnItemClickListener, CardFragment.CardCallbacks {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mViewPager;
    private DeckPagerAdapter mPagerAdapter;
    private int mLastPosition;
    private boolean mMiskatonicSetting;
    private final List<String> mTitleStrings = Arrays.asList(
        "Cult Encounter",
        "Exhibit Encounter",
        "Innsmouth Look",
        "Reckoning",
        "Settings"
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mTitleStrings));
        mDrawerList.setOnItemClickListener(this);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
            this,                   // host Activity
            mDrawerLayout,           // DrawerLayout object
            toolbar,                // nav drawer image to replace 'Up' caret
            R.string.drawer_open,   // "open drawer" description for accessibility
            R.string.drawer_close   // "close drawer" description for accessibility
        ) {
            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mLastPosition = 0;
        getSupportActionBar().setTitle(mTitleStrings.get(mLastPosition));

        // Instantiate a view pager and a pager adapter.
        mMiskatonicSetting = getMiskatonicSetting();
        mViewPager = (ViewPager) findViewById(R.id.fragment_container);
        setPagerAdapter(generateCultEncounterDeck(), CultEncounter.class);
    }

    /**
     * Returns if the Misktaonic Horror expansion set is enabled.
     *
     * @return True if the user enabled the Miskatonic Horror expansion set. False otherwise. 
     */
    private boolean getMiskatonicSetting() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
            getString(R.string.miskatonic_horror_expansion_title),
            false
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        final boolean previousMiskatonicSetting = mMiskatonicSetting;
        mMiskatonicSetting = getMiskatonicSetting();
        // If the miskatonic setting is enabled, the miskatonic cards should be read and added. If
        // the miskatonic setting is disabled, the miskatonic cards should be removed from the set.
        if (previousMiskatonicSetting != mMiskatonicSetting && mMiskatonicSetting) {
            // Miskatonic setting is enabled.
            List<CardXML> miskatonic = Collections.emptyList();
            if (mPagerAdapter.getCardClass() == CultEncounter.class) {
                miskatonic = castList(parseCultEncounterResource(R.raw.cult_encounter_miskatonic));
            } else if (mPagerAdapter.getCardClass() == ExhibitEncounter.class) {
                miskatonic = castList(
                    parseExhibitEncounterResource(R.raw.exhibit_encounter_miskatonic)
                );
            } else if (mPagerAdapter.getCardClass() == InnsmouthLook.class) {
                miskatonic = castList(parseInnsmouthLookResource(R.raw.innsmouth_look_miskatonic));
            } else if (mPagerAdapter.getCardClass() == Reckoning.class) {
                miskatonic = castList(parseReckoningResource(R.raw.reckoning_miskatonic));
            }
            List<CardXML> cards = mPagerAdapter.getCards();
            cards.addAll(miskatonic);
            setPagerAdapter(shuffleCards(cards), mPagerAdapter.getCardClass());
        } else if (previousMiskatonicSetting != mMiskatonicSetting) {
            // Miskatonic setting is disabled.
            List<CardXML> filteredCards = new ArrayList<>();
            for (CardXML card : mPagerAdapter.getCards()) {
                if (!card.expansionSet.equals(CardXML.MISKATONIC))
                    filteredCards.add(card);
            }
            setPagerAdapter(shuffleCards(filteredCards), mPagerAdapter.getCardClass());
        }
    }

    private <T extends CardXML> List<CardXML> castList(List<T> list) {
        @SuppressWarnings("unchecked")
        List<CardXML> result = (List<CardXML>) list;
        return result;
    }

    /**
     * Sets the pager adapter with the cards and given card class. This will affect which cards are
     * being shown to the user.
     *
     * @param cards
     *     The cards that are parsed and to be shown to the user.
     * @param cardClass
     *     The card's class.
     */
    private void setPagerAdapter(List<CardXML> cards, Class<? extends CardXML> cardClass) {
        mPagerAdapter = new DeckPagerAdapter(
            getSupportFragmentManager(),
            cards,
            cardClass,
            mMiskatonicSetting
        );
        mViewPager.setAdapter(mPagerAdapter);
    }

    /**
     * Opens the resource id and returns the reader.
     *
     * @param resource
     *     The Android raw resource id to be used.
     */
    private Reader readResource(int resource) {
        return new InputStreamReader(getResources().openRawResource(resource));
    }

    /**
     * Generates the Cult Encounter deck and returns it as the abstract CardXML type. If the
     * Miskatonic setting is enabled, this will also parse the Miskatonic Cult Encounters.
     *
     * @return A list of Cult Encounters.
     */
    private List<CardXML> generateCultEncounterDeck() {
        List<CultEncounter> base = parseCultEncounterResource(R.raw.cult_encounter);
        List<CultEncounter> allCards = new ArrayList<>(base.size());
        allCards.addAll(base);
        if (mMiskatonicSetting)
            allCards.addAll(parseCultEncounterResource(R.raw.cult_encounter_miskatonic));

        return shuffleCards(allCards);
    }

    private List<CultEncounter> parseCultEncounterResource(int resource) {
        Reader reader = readResource(R.raw.cult_encounter);
        List<CultEncounter> cards = CultEncounter.parseReader(reader);
        try {
            reader.close();
        } catch (IOException e) {
            Log.w(TAG, "Error closing cult encounter reader " + e);
        }
        return cards;
    }

    /**
     * Generates the Exhibit Encounter deck and returns it as an abstract CardXML type. If the
     * Miskatonic setting is enabled, this will also parse the Miskatonic Exhibit Encounters.
     *
     * @return A list of Exhibit Encounters.
     */
    private List<CardXML> generateExhibitEncounterDeck() {
        List<ExhibitEncounter> base = parseExhibitEncounterResource(R.raw.exhibit_encounter);
        List<ExhibitEncounter> allCards = new ArrayList<>(base.size());
        allCards.addAll(base);
        if (mMiskatonicSetting)
            allCards.addAll(parseExhibitEncounterResource(R.raw.exhibit_encounter_miskatonic));

        return shuffleCards(allCards);
    }

    private List<ExhibitEncounter> parseExhibitEncounterResource(int resource) {
        Reader reader = readResource(R.raw.cult_encounter);
        List<ExhibitEncounter> cards = ExhibitEncounter.parseReader(reader);
        try {
            reader.close();
        } catch (IOException e) {
            Log.w(TAG, "Error closing exhibit encounter reader " + e);
        }
        return cards;
    }

    /**
     * Generates the Innsmouth Look deck and returns it as an abstract CardXML type. If the Miskatonic
     * setting is enabled, this will also parse the Miskatonic Innsmouth Look cards.
     *
     * @return A list of Innsmouth Look cards.
     */
    private List<CardXML> generateInnsmouthLookDeck() {
        List<InnsmouthLook> base = parseInnsmouthLookResource(R.raw.innsmouth_look);
        List<InnsmouthLook> allCards = new ArrayList<>(base.size());
        allCards.addAll(base);
        if (mMiskatonicSetting)
            allCards.addAll(parseInnsmouthLookResource(R.raw.innsmouth_look_miskatonic));

        return shuffleCards(allCards);
    }

    private List<InnsmouthLook> parseInnsmouthLookResource(int resource) {
        Reader reader = readResource(resource);
        List<InnsmouthLook> cards = InnsmouthLook.parseReader(reader);
        try {
            reader.close();
        } catch (IOException e) {
            Log.w(TAG, "Error closing innsmouth look reader " + e);
        }
        return cards;
    }

    /**
     * Generates the Reckoning deck and returns it as an abstract CardXML list. If the Miskatonic
     * setting is enabled, this will also parse the Miskatonic Reckoning cards.
     *
     * @return A list of Reckoning cards.
     */
    private List<CardXML> generateReckoningDeck() {
        List<Reckoning> base = parseReckoningResource(R.raw.reckoning);
        List<Reckoning> allCards = new ArrayList<>(base.size());
        allCards.addAll(base);
        if (mMiskatonicSetting)
            allCards.addAll(parseReckoningResource(R.raw.reckoning_miskatonic));

        return shuffleCards(allCards);
    }

    private List<Reckoning> parseReckoningResource(int resource) {
        Reader reader = readResource(resource);
        List<Reckoning> cards = Reckoning.parseReader(reader);
        try {
            reader.close();
        } catch (IOException e) {
            Log.w(TAG, "Error closing reckoning reader " + e);
        }
        return cards;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        // noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Start the settings activity.
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (mLastPosition == position) {
            setPagerAdapter(shuffleCards(mPagerAdapter.getCards()), mPagerAdapter.getCardClass());
        } else {
            switch (position) {
                case 0:
                    setPagerAdapter(generateCultEncounterDeck(), CultEncounter.class);
                    break;
                case 1:
                    setPagerAdapter(generateExhibitEncounterDeck(), ExhibitEncounter.class);
                    break;
                case 2:
                    setPagerAdapter(generateInnsmouthLookDeck(), InnsmouthLook.class);
                    break;
                case 3:
                    setPagerAdapter(generateReckoningDeck(), Reckoning.class);
                    break;
            }
            getSupportActionBar().setTitle(mTitleStrings.get(position));
            mLastPosition = position;
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDoneItemClick() {
        setPagerAdapter(shuffleCards(mPagerAdapter.getCards()), mPagerAdapter.getCardClass());
    }

    private static final String TAG = "MainActivity";

    /**
     * An adapter that will be used to display the fragments representing each card.
     */
    private static class DeckPagerAdapter extends FragmentStatePagerAdapter {
        private List<CardXML> mCards;
        private Class<? extends CardXML> mCardClass;
        private boolean mMiskatonicSetting;

        /**
         * Constructor for the Deck Pager Adapter. This will take in cards, the card class for
         * comparisons, and if the miskatonic setting is enabled or not.
         *
         * @param fm
         *     The Android FragmentManager that the activity uses.
         * @param cards
         *     The cards that will be displayed to the user.
         * @param cardClass
         *     The class of each card given in the list.
         * @param miskatonicSetting
         *     A setting whether the Miskatonic expansion is enabled or not.
         */
        public DeckPagerAdapter(
            FragmentManager fm,
            List<CardXML> cards,
            Class<? extends CardXML> cardClass,
            boolean miskatonicSetting
        ) {
            super(fm);
            mCards = cards;
            mCardClass = cardClass;
            mMiskatonicSetting = miskatonicSetting;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (mCardClass == CultEncounter.class) {
                    Bundle arguments = new Bundle();
                    arguments.putString("title", "Cult Encounter Deck");
                    CultEncounterFragment fragment = new CultEncounterFragment();
                    fragment.setArguments(arguments);
                    return fragment;
                } else if (mCardClass == ExhibitEncounter.class) {
                    Bundle arguments = new Bundle();
                    arguments.putString("title", "Exhibit Encounter Deck");
                    ExhibitEncounterFragment fragment = new ExhibitEncounterFragment();
                    fragment.setArguments(arguments);
                    return fragment;
                } else if (mCardClass == InnsmouthLook.class) {
                    Bundle arguments = new Bundle();
                    InnsmouthLookFragment fragment = new InnsmouthLookFragment();
                    arguments.putString("lore", "Innsmouth Look Deck");
                    fragment.setArguments(arguments);
                    return fragment;
                } else if (mCardClass == Reckoning.class) {
                    Bundle arguments = new Bundle();
                    arguments.putString("title", "Reckoning Deck");
                    ReckoningFragment fragment = new ReckoningFragment();
                    fragment.setArguments(arguments);
                    return fragment;
                } else {
                    throw new RuntimeException(mCardClass.getName() + " does not have a match");
                }
            } else {
                final int cardPosition = position - 1;
                if (mCardClass == CultEncounter.class) {
                    CultEncounter card = (CultEncounter) mCards.get(cardPosition);
                    Bundle arguments = CultEncounterFragment.exportCultEncounter(card);
                    CultEncounterFragment fragment = new CultEncounterFragment();
                    fragment.setArguments(arguments);
                    return fragment;
                } else if (mCardClass == ExhibitEncounter.class) {
                    ExhibitEncounter card = (ExhibitEncounter) mCards.get(cardPosition);
                    Bundle arguments = ExhibitEncounterFragment.exportExhibitEncounter(card);
                    ExhibitEncounterFragment fragment = new ExhibitEncounterFragment();
                    fragment.setArguments(arguments);
                    return fragment;
                } else if (mCardClass == InnsmouthLook.class) {
                    InnsmouthLook card = (InnsmouthLook) mCards.get(cardPosition);
                    Bundle arguments = InnsmouthLookFragment.exportInnsmouthLook(card);
                    InnsmouthLookFragment fragment = new InnsmouthLookFragment();
                    fragment.setArguments(arguments);
                    return fragment;
                } else if (mCardClass == Reckoning.class) {
                    Reckoning card = (Reckoning) mCards.get(cardPosition);
                    Bundle arguments = ReckoningFragment.exportReckoning(card);
                    ReckoningFragment fragment = new ReckoningFragment();
                    fragment.setArguments(arguments);
                    return fragment;
                } else {
                    throw new RuntimeException(mCardClass.getName() + " does not have a match");
                }
            }
        }

        /**
         * Returns the cards that the pager adapter currently has.
         *
         * @return The cards in this pager adapter.
         */
        public List<CardXML> getCards() {
            return mCards;
        }

        /**
         * Returns the card class the pager adapter is assigned.
         *
         * @return The card class of the pager adapter.
         */
        public Class<? extends CardXML> getCardClass() {
            return mCardClass;
        }

        @Override
        public int getCount() {
            if (mCardClass == InnsmouthLook.class && mMiskatonicSetting)
                return 11 < mCards.size() ? 11 : mCards.size();
            else
                return 6 < mCards.size() ? 6 : mCards.size();
        }
    }

    /**
     * Shuffles the cards in the list. This is written functionally, and will not cause side effects
     * for the input cards.
     *
     * @param cards
     *     The cards that will be shuffled.
     * @return A list of shuffled cards.
     */
    protected static <T extends CardXML> List<CardXML> shuffleCards(List<T> cards) {
        List<CardXML> shuffledCards = new ArrayList<>(cards.size());
        shuffledCards.addAll(cards);
        Collections.shuffle(shuffledCards);
        return Collections.unmodifiableList(shuffledCards);
    }
}
