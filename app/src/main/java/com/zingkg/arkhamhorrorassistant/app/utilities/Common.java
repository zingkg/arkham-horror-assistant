package com.zingkg.arkhamhorrorassistant.app.utilities;

import android.app.Activity;
import android.content.Context;

import com.zingkg.arkhamhorrorassistant.app.fragments.DeckFragment;

public class Common {
    public static DeckFragment.DeckCallbacks castDeckCallbacks(Activity activity) {
        try {
            return (DeckFragment.DeckCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement DeckCallbacks");
        }
    }

    public static DeckFragment.DeckCallbacks castDeckCallbacks(Context context) {
        return castDeckCallbacks((Activity) context);
    }
}
