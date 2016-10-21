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
import com.zingkg.arkhamhorrorassistant.xml.InnsmouthLook;

public class InnsmouthLookFragment extends DeckFragment {
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
        return inflater.inflate(R.layout.fragment_innsmouth, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InnsmouthLook card = new InnsmouthLook(
            getArguments().getString("lore", ""),
            getArguments().getString("entry", ""),
            getArguments().getString("expansionSet", "")
        );
        TextView loreView = (TextView) view.findViewById(R.id.innsmouth_lore);
        if (!card.lore.isEmpty() && card.entry.isEmpty()) {
            // Simply a title.
            loreView.setTextSize(getTitleDimensionPixelSize());
            view.findViewById(R.id.innsmouth_divider).setVisibility(View.INVISIBLE);
        } else {
            TextView entryText = (TextView) view.findViewById(R.id.innsmouth_entry);
            final float textSize = calculateCardTextSize(getScreenLength());
            loreView.setTextSize(textSize);
            entryText.setTextSize(textSize);
            entryText.setText(Html.fromHtml(card.entry));
        }
        loreView.setText(Html.fromHtml(card.lore));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_decks, menu);
    }
}
