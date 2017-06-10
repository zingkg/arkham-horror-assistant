package com.zingkg.arkhamhorrorassistant.app.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zingkg.arkhamhorrorassistant.app.R;
import com.zingkg.arkhamhorrorassistant.xml.ExhibitEncounter;

/**
 * This fragment represents the text of an Exhibit Encounter card.
 */
public class ExhibitEncounterFragment extends CardFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_exhibit_encounter, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ExhibitEncounter card = importExhibitEncounter(getArguments());
        if (card.entry.isEmpty() && card.location.isEmpty()) {
            view.findViewById(R.id.exhibit_encounter_title_divider).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.exhibit_encounter_entry_divider).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) view.findViewById(R.id.exhibit_encounter_entry)).setText(
                Html.fromHtml(card.entry)
            );
            ((TextView) view.findViewById(R.id.exhibit_encounter_location)).setText(
                Html.fromHtml(card.location)
            );
        }
        ((TextView) view.findViewById(R.id.exhibit_encounter_title)).setText(
            Html.fromHtml(card.title)
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_decks, menu);
    }

    private static final String titleKey = "title";
    private static final String entryKey = "entry";
    private static final String locationKey = "location";
    private static final String expansionSetKey = "expansionSet";

    public static Bundle exportExhibitEncounter(ExhibitEncounter exhibitEncounter) {
        Bundle bundle = new Bundle();
        bundle.putString(titleKey, exhibitEncounter.title);
        bundle.putString(entryKey, exhibitEncounter.entry);
        bundle.putString(locationKey, exhibitEncounter.location);
        bundle.putString(expansionSetKey, exhibitEncounter.expansionSet);
        return bundle;
    }

    public static ExhibitEncounter importExhibitEncounter(Bundle bundle) {
        return new ExhibitEncounter(
            bundle.getString(titleKey, ""),
            bundle.getString(entryKey, ""),
            bundle.getString(locationKey, ""),
            bundle.getString(expansionSetKey, "")
        );
    }
}
