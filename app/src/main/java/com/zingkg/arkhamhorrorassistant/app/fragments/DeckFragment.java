package com.zingkg.arkhamhorrorassistant.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.zingkg.arkhamhorrorassistant.app.R;

public abstract class DeckFragment extends Fragment {
    private DeckCallbacks mCallbacks;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                mCallbacks.onDoneItemClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = castDeckCallbacks(context);
    }

    protected int getTitleDimensionPixelSize() {
        return getResources().getDimensionPixelSize(R.dimen.title);
    }

    public interface DeckCallbacks {
        void onDoneItemClick();
    }

    public static DeckFragment.DeckCallbacks castDeckCallbacks(Context context) {
        Activity activity = (Activity) context;
        try {
            return (DeckFragment.DeckCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement DeckCallbacks");
        }
    }
}
