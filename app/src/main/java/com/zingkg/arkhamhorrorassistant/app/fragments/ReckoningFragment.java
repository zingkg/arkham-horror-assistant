package com.zingkg.arkhamhorrorassistant.app.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zingkg.arkhamhorrorassistant.app.R;
import com.zingkg.arkhamhorrorassistant.xml.Reckoning;

public class ReckoningFragment extends DeckFragment {
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
        return inflater.inflate(R.layout.fragment_reckoning, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Reckoning card = new Reckoning(
            getArguments().getString("title", ""),
            getArguments().getString("entry", ""),
            getArguments().getString("expansionSet", "")
        );
        TextView titleView = (TextView) view.findViewById(R.id.reckoning_title);
        if (card.entry.isEmpty()) {
            titleView.setTextSize(getTitleDimensionPixelSize());
            ((LinearLayout) view.findViewById(R.id.reckoning_layout)).setGravity(
                Gravity.CENTER_HORIZONTAL
            );
            view.findViewById(R.id.reckoning_divider).setVisibility(View.INVISIBLE);
        } else {
            TextView entryText = (TextView) view.findViewById(R.id.reckoning_entry);
            entryText.setText(Html.fromHtml(card.entry));
            entryText.setTextSize(calculateCardTextSize(getScreenLength()));
        }
        titleView.setText(Html.fromHtml(card.title));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_decks, menu);
    }
}
