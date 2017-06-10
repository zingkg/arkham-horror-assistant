package com.zingkg.arkhamhorrorassistant.app.fragments;

import android.content.res.Resources;
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

/**
 * This fragment represents the text of an Innsmouth Look card.
 */
public class InnsmouthLookFragment extends CardFragment {
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
        InnsmouthLook card = importInnsmouthLook(getArguments());
        TextView loreView = (TextView) view.findViewById(R.id.innsmouth_lore);
        if (!card.lore.isEmpty() && card.entry.isEmpty()) {
            // Simply a title.
            loreView.setTextSize(calculateTitleTextSize());
            view.findViewById(R.id.innsmouth_divider).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) view.findViewById(R.id.innsmouth_entry)).setText(Html.fromHtml(card.entry));
        }
        loreView.setText(Html.fromHtml(card.lore));
    }

    /**
     * Calculates the text size for a title.
     *
     * @return The text size that is defined in the dimens.xml file.
     */
    private float calculateTitleTextSize() {
        Resources resources = getResources();
        return resources.getDimension(R.dimen.title) / resources.getDisplayMetrics().density;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_decks, menu);
    }

    private static final String loreKey = "lore";
    private static final String entryKey = "entry";
    private static final String expansionSetKey = "expansionSet";

    public static Bundle exportInnsmouthLook(InnsmouthLook innsmouthLook) {
        Bundle bundle = new Bundle();
        bundle.putString(loreKey, innsmouthLook.lore);
        bundle.putString(entryKey, innsmouthLook.entry);
        bundle.putString(expansionSetKey, innsmouthLook.expansionSet);
        return bundle;
    }

    public static InnsmouthLook importInnsmouthLook(Bundle bundle) {
        return new InnsmouthLook(
            bundle.getString(loreKey, ""),
            bundle.getString(entryKey, ""),
            bundle.getString(expansionSetKey, "")
        );
    }
}
