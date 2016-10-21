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
            view.findViewById(R.id.cult_encounter_title_divider).setVisibility(View.INVISIBLE);
            titleView.setTextSize(getTitleDimensionPixelSize());
        } else {
            TextView loreText = (TextView) view.findViewById(R.id.cult_encounter_lore);
            TextView entryText = (TextView) view.findViewById(R.id.cult_encounter_entry);
            final float textSize = calculateCardTextSize(getScreenLength());
            loreText.setText(Html.fromHtml(card.lore));
            loreText.setTextSize(textSize);
            entryText.setText(Html.fromHtml(card.entry));
            entryText.setTextSize(textSize);
        }
        titleView.setText(Html.fromHtml(card.title));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_decks, menu);
    }
}
