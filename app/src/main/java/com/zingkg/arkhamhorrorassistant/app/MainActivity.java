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
import java.util.Random;

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
                if (!card.mType.equals(CardXML.MISKATONIC))
                    filteredCards.add(card);
            }
            setPagerAdapter(shuffleCards(filteredCards), mPagerAdapter.getCardClass());
        }
    }

    private List<CardXML> shuffleCards(List<CardXML> cards) {
        List<CardXML> shuffledCards = new ArrayList<>();
        for (int i = 1; i < cards.size(); ++i)
            shuffledCards.add(cards.get(i));

        Collections.shuffle(shuffledCards);
        shuffledCards.add(0, cards.get(0));
        return shuffledCards;
    }

    private void setPagerAdapter(List<CardXML> cards, Class cardClass) {
        mPagerAdapter = new DeckPagerAdapter(getSupportFragmentManager(), cards, cardClass);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private Reader readResource(int resource) {
        return new InputStreamReader(getResources().openRawResource(resource));
    }

    private List<CardXML> createCultEncounterDeck() {
        List<CultEncounter> base = CultEncounter.parseFile(readResource(R.raw.cult_encounter));
        if (mMiskatonicSetting)
            base.addAll(CultEncounter.parseFile(readResource(R.raw.cult_encounter_miskatonic)));

        Collections.shuffle(base, new Random(System.currentTimeMillis()));
        base.add(0, new CultEncounter("Cult Encounter Deck", "", "", ""));
        return new ArrayList<CardXML>(base);
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
        Collections.shuffle(base, new Random(System.currentTimeMillis()));
        base.add(0, new ExhibitEncounter("Exhibit Encounter Deck", "", "", ""));
        return new ArrayList<CardXML>(base);
    }

    private List<CardXML> createInnsmouthLookDeck() {
        List<InnsmouthLook> base = InnsmouthLook.parseFile(readResource(R.raw.innsmouth_look));
        if (mMiskatonicSetting)
            base.addAll(InnsmouthLook.parseFile(readResource(R.raw.innsmouth_look_miskatonic)));

        Collections.shuffle(base, new Random(System.currentTimeMillis()));
        base.add(0, new InnsmouthLook("Innsmouth Look Deck", "", ""));
        return new ArrayList<CardXML>(base);
    }

    private List<CardXML> createReckoningDeck() {
        List<Reckoning> base = Reckoning.parseFile(readResource(R.raw.reckoning));
        if (mMiskatonicSetting)
            base.addAll(Reckoning.parseFile(readResource(R.raw.reckoning_miskatonic)));

        Collections.shuffle(base);
        base.add(0, new Reckoning("Reckoning Deck", "", ""));
        return new ArrayList<CardXML>(base);
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
        private Class mCardClass;

        public DeckPagerAdapter(FragmentManager fm, List<CardXML> cards, Class cardClass) {
            super(fm);
            mCards = cards;
            mCardClass = cardClass;
        }

        @Override
        public Fragment getItem(int position) {
            if (mCardClass == CultEncounter.class) {
                CultEncounter card = (CultEncounter) mCards.get(position);
                Bundle arguments = new Bundle();
                arguments.putString("title", card.mTitle);
                arguments.putString("lore", card.mLore);
                arguments.putString("entry", card.mEntry);
                arguments.putString("type", card.mType);
                CultEncounterFragment fragment = new CultEncounterFragment();
                fragment.setArguments(arguments);
                return fragment;
            } else if (mCardClass == ExhibitEncounter.class) {
                ExhibitEncounter card = (ExhibitEncounter) mCards.get(position);
                Bundle arguments = new Bundle();
                arguments.putString("title", card.mTitle);
                arguments.putString("entry", card.mEntry);
                arguments.putString("location", card.mLocation);
                arguments.putString("type", card.mType);
                ExhibitEncounterFragment fragment = new ExhibitEncounterFragment();
                fragment.setArguments(arguments);
                return fragment;
            } else if (mCardClass == InnsmouthLook.class) {
                InnsmouthLook card = (InnsmouthLook) mCards.get(position);
                Bundle arguments = new Bundle();
                arguments.putString("lore", card.mLore);
                arguments.putString("entry", card.mEntry);
                arguments.putString("type", card.mType);
                InnsmouthLookFragment fragment = new InnsmouthLookFragment();
                fragment.setArguments(arguments);
                return fragment;
            } else {
                Reckoning card = (Reckoning) mCards.get(position);
                Bundle arguments = new Bundle();
                arguments.putString("title", card.mTitle);
                arguments.putString("entry", card.mEntry);
                arguments.putString("type", card.mType);
                ReckoningFragment fragment = new ReckoningFragment();
                fragment.setArguments(arguments);
                return fragment;
            }
        }

        public List<CardXML> getCards() {
            return mCards;
        }

        public Class getCardClass() {
            return mCardClass;
        }

        @Override
        public int getCount() {
            if (mCardClass == InnsmouthLook.class)
                return 10;
            else
                return 5;
        }
    }
}
