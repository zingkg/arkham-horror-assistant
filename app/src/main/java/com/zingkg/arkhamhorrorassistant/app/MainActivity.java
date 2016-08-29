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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zingkg.arkhamhorrorassistant.app.fragments.CultEncounterFragment;
import com.zingkg.arkhamhorrorassistant.app.fragments.DeckFragment;
import com.zingkg.arkhamhorrorassistant.app.fragments.ExhibitEncounterFragment;
import com.zingkg.arkhamhorrorassistant.app.fragments.InnsmouthLookFragment;
import com.zingkg.arkhamhorrorassistant.app.fragments.ReckoningFragment;
import com.zingkg.arkhamhorrorassistant.xml.CardXML;
import com.zingkg.arkhamhorrorassistant.xml.CultEncounter;
import com.zingkg.arkhamhorrorassistant.xml.ExhibitEncounter;
import com.zingkg.arkhamhorrorassistant.xml.InnsmouthLook;
import com.zingkg.arkhamhorrorassistant.xml.Reckoning;

public class MainActivity
    extends AppCompatActivity
    implements ListView.OnItemClickListener, DeckFragment.DeckCallbacks {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mViewPager;
    private DeckPagerAdapter mPagerAdapter;
    private int mLastPosition;
    private boolean mMiskatonicSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        List<String> titleStrings = new ArrayList<>();
        titleStrings.add("Cult Encounter");
        titleStrings.add("Exhibit Encounter");
        titleStrings.add("Innsmouth Look");
        titleStrings.add("Reckoning");
        titleStrings.add("Settings");
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, titleStrings));
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
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setTitle("Cult Encounter");
        mLastPosition = 0;

        // Instantiate a view pager and a pager adapter.
        mMiskatonicSetting = getMiskatonicSetting();
        mViewPager = (ViewPager) findViewById(R.id.fragment_container);
        setPagerAdapter(createCultEncounterDeck(), CultEncounter.class);
    }

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
            List<CardXML> miskatonic;
            if (mPagerAdapter.getCardClass() == CultEncounter.class) {
                miskatonic = new ArrayList<CardXML>(
                    CultEncounter.parseFile(readResource(R.raw.cult_encounter_miskatonic))
                );
            } else if (mPagerAdapter.getCardClass() == ExhibitEncounter.class) {
                miskatonic = new ArrayList<CardXML>(
                    ExhibitEncounter.parseFile(readResource(R.raw.exhibit_encounter_miskatonic))
                );
            } else if (mPagerAdapter.getCardClass() == InnsmouthLook.class) {
                miskatonic = new ArrayList<CardXML>(
                    InnsmouthLook.parseFile(readResource(R.raw.innsmouth_look_miskatonic))
                );
            } else if (mPagerAdapter.getCardClass() == Reckoning.class) {
                miskatonic = new ArrayList<CardXML>(
                    Reckoning.parseFile(readResource(R.raw.reckoning_miskatonic))
                );
            } else {
                miskatonic = new ArrayList<>();
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

    private void setPagerAdapter(List<CardXML> cards, Class<? extends CardXML> cardClass) {
        mPagerAdapter = new DeckPagerAdapter(
            getSupportFragmentManager(),
            cards,
            cardClass,
            mMiskatonicSetting
        );
        mViewPager.setAdapter(mPagerAdapter);
    }

    private Reader readResource(int resource) {
        return new InputStreamReader(getResources().openRawResource(resource));
    }

    private List<CardXML> createCultEncounterDeck() {
        List<CultEncounter> base = CultEncounter.parseFile(readResource(R.raw.cult_encounter));
        if (mMiskatonicSetting)
            base.addAll(CultEncounter.parseFile(readResource(R.raw.cult_encounter_miskatonic)));

        return shuffleCards(base);
    }

    private List<CardXML> createExhibitEncounterDeck() {
        List<ExhibitEncounter> base = ExhibitEncounter.parseFile(
            readResource(R.raw.exhibit_encounter)
        );
        if (mMiskatonicSetting) {
            base.addAll(
                ExhibitEncounter.parseFile(readResource(R.raw.exhibit_encounter_miskatonic))
            );
        }
        return shuffleCards(base);
    }

    private List<CardXML> createInnsmouthLookDeck() {
        List<InnsmouthLook> base = InnsmouthLook.parseFile(readResource(R.raw.innsmouth_look));
        if (mMiskatonicSetting)
            base.addAll(InnsmouthLook.parseFile(readResource(R.raw.innsmouth_look_miskatonic)));

        return shuffleCards(base);
    }

    private List<CardXML> createReckoningDeck() {
        List<Reckoning> base = Reckoning.parseFile(readResource(R.raw.reckoning));
        if (mMiskatonicSetting)
            base.addAll(Reckoning.parseFile(readResource(R.raw.reckoning_miskatonic)));

        return shuffleCards(base);
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
                    setPagerAdapter(createCultEncounterDeck(), CultEncounter.class);
                    getSupportActionBar().setTitle("Cult Encounter");
                    break;
                case 1:
                    setPagerAdapter(createExhibitEncounterDeck(), ExhibitEncounter.class);
                    getSupportActionBar().setTitle("Exhibit Encounter");
                    break;
                case 2:
                    setPagerAdapter(createInnsmouthLookDeck(), InnsmouthLook.class);
                    getSupportActionBar().setTitle("Innsmouth Look");
                    break;
                case 3:
                    setPagerAdapter(createReckoningDeck(), Reckoning.class);
                    getSupportActionBar().setTitle("Reckoning");
                    break;
                case 4:
                    startActivity(new Intent(this, SettingsActivity.class));
                    break;
            }
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

    private static class DeckPagerAdapter extends FragmentStatePagerAdapter {
        private List<CardXML> mCards;
        private Class<? extends CardXML> mCardClass;
        private boolean mMiskatonicSetting;

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
                    arguments.putString("lore", "Innsmouth Look Deck");
                    InnsmouthLookFragment fragment = new InnsmouthLookFragment();
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
                    Bundle arguments = new Bundle();
                    arguments.putString("title", card.title);
                    arguments.putString("lore", card.lore);
                    arguments.putString("entry", card.entry);
                    arguments.putString("expansionSet", card.expansionSet);
                    CultEncounterFragment fragment = new CultEncounterFragment();
                    fragment.setArguments(arguments);
                    return fragment;
                } else if (mCardClass == ExhibitEncounter.class) {
                    ExhibitEncounter card = (ExhibitEncounter) mCards.get(cardPosition);
                    Bundle arguments = new Bundle();
                    arguments.putString("title", card.title);
                    arguments.putString("entry", card.entry);
                    arguments.putString("location", card.location);
                    arguments.putString("expansionSet", card.expansionSet);
                    ExhibitEncounterFragment fragment = new ExhibitEncounterFragment();
                    fragment.setArguments(arguments);
                    return fragment;
                } else if (mCardClass == InnsmouthLook.class) {
                    InnsmouthLook card = (InnsmouthLook) mCards.get(cardPosition);
                    Bundle arguments = new Bundle();
                    arguments.putString("lore", card.lore);
                    arguments.putString("entry", card.entry);
                    arguments.putString("expansionSet", card.expansionSet);
                    InnsmouthLookFragment fragment = new InnsmouthLookFragment();
                    fragment.setArguments(arguments);
                    return fragment;
                } else if (mCardClass == Reckoning.class) {
                    Reckoning card = (Reckoning) mCards.get(cardPosition);
                    Bundle arguments = new Bundle();
                    arguments.putString("title", card.title);
                    arguments.putString("entry", card.entry);
                    arguments.putString("expansionSet", card.expansionSet);
                    ReckoningFragment fragment = new ReckoningFragment();
                    fragment.setArguments(arguments);
                    return fragment;
                } else {
                    throw new RuntimeException(mCardClass.getName() + " does not have a match");
                }
            }
        }

        public List<CardXML> getCards() {
            return mCards;
        }

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

    protected static <T extends CardXML> List<CardXML> shuffleCards(List<T> cards) {
        List<CardXML> shuffledCards = new ArrayList<>();
        shuffledCards.addAll(cards);
        Collections.shuffle(shuffledCards);
        return shuffledCards;
    }
}
