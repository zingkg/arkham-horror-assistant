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

/**
 * This fragment represents the text of a Reckoning card.
 */
public class ReckoningFragment extends CardFragment {
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
        Reckoning card = importReckoning(getArguments());
        if (card.entry.isEmpty()) {
            ((LinearLayout) view.findViewById(R.id.reckoning_layout)).setGravity(
                Gravity.CENTER_HORIZONTAL
            );
            view.findViewById(R.id.reckoning_divider).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) view.findViewById(R.id.reckoning_entry)).setText(Html.fromHtml(card.entry));
        }
        ((TextView) view.findViewById(R.id.reckoning_title)).setText(Html.fromHtml(card.title));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_decks, menu);
    }

    private static final String titleKey = "title";
    private static final String entryKey = "entry";
    private static final String expansionSetKey = "expansionSetKey";

    public static Bundle exportReckoning(Reckoning reckoning) {
        Bundle bundle = new Bundle();
        bundle.putString(titleKey, reckoning.title);
        bundle.putString(entryKey, reckoning.entry);
        bundle.putString(expansionSetKey, reckoning.expansionSet);
        return bundle;
    }

    public static Reckoning importReckoning(Bundle bundle) {
        return new Reckoning(
            bundle.getString(titleKey, ""),
            bundle.getString(entryKey, ""),
            bundle.getString(expansionSetKey, "")
        );
    }
}
