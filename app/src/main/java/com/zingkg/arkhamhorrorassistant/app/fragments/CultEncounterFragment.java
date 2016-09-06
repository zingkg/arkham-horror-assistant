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
import com.zingkg.arkhamhorrorassistant.xml.CultEncounter;

public class CultEncounterFragment extends DeckFragment {
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
        return inflater.inflate(R.layout.fragment_cult_encounter, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CultEncounter card = new CultEncounter(
            getArguments().getString("title", ""),
            getArguments().getString("lore", ""),
            getArguments().getString("entry", ""),
            getArguments().getString("expansionSet", "")
        );
        TextView titleView = (TextView) view.findViewById(R.id.cult_encounter_title);
        if (card.lore.isEmpty() && card.entry.isEmpty()) {
            titleView.setTextSize(getTitleDimensionPixelSize());
            view.findViewById(R.id.cult_encounter_lore_divider).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) view.findViewById(R.id.cult_encounter_lore)).setText(
                Html.fromHtml(card.lore)
            );
            ((TextView) view.findViewById(R.id.cult_encounter_entry)).setText(
                Html.fromHtml(card.entry)
            );
        }
        ((TextView) view.findViewById(R.id.cult_encounter_title)).setText(
            Html.fromHtml(card.title)
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_decks, menu);
    }
}
