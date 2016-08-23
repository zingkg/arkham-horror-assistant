package com.zingkg.arkhamhorrorassistant.app.fragments;

import android.os.Bundle;
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
            getArguments().getString("type", "")
        );
        TextView loreView = (TextView) view.findViewById(R.id.innsmouth_lore);
        if (card.mEntry.isEmpty())
            loreView.setTextSize(getTitleDimensionPixelSize());
        else
            ((TextView) view.findViewById(R.id.innsmouth_entry)).setText(card.mEntry);

        loreView.setText(card.mLore);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_decks, menu);
    }
}
