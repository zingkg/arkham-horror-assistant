package com.zingkg.arkhamhorrorassistant.app.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        if (card.lore.isEmpty() && card.entry.isEmpty()) {
            // Simply a title.
            view.findViewById(R.id.innsmouth_line).setAlpha(0);
            view.findViewById(R.id.innsmouth_layout).setBackgroundResource(
                R.drawable.innsmouth_look_back
            );
        } else if (card.entry.contains("devoured")) {
            TextView loreView = (TextView) view.findViewById(R.id.innsmouth_lore);
            TextView entryView = (TextView) view.findViewById(R.id.innsmouth_entry);
            view.findViewById(R.id.innsmouth_layout).setBackgroundResource(
                R.drawable.innsmouth_look_devoured
            );
            setCardText(loreView, entryView, card);
            Point point = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(point);
            final float yTranslation = point.y / 8f;
            loreView.setTranslationY(yTranslation);
            view.findViewById(R.id.innsmouth_line).setTranslationY(yTranslation);
            entryView.setTranslationY(yTranslation);
        } else if (card.expansionSet.equals("miskatonic")) {
            view.findViewById(R.id.innsmouth_layout).setBackgroundResource(
                R.drawable.innsmouth_look_front_miskatonic
            );
            setCardText(
                (TextView) view.findViewById(R.id.innsmouth_lore),
                (TextView) view.findViewById(R.id.innsmouth_entry),
                card
            );
        } else {
            setCardText(
                (TextView) view.findViewById(R.id.innsmouth_lore),
                (TextView) view.findViewById(R.id.innsmouth_entry),
                card
            );
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_decks, menu);
    }

    private static void setCardText(TextView lore, TextView entry, InnsmouthLook card) {
        lore.setText(Html.fromHtml(card.lore));
        entry.setText(Html.fromHtml(card.entry));
    }
}
