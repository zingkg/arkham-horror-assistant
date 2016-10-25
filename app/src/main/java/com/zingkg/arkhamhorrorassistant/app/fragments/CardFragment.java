package com.zingkg.arkhamhorrorassistant.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.zingkg.arkhamhorrorassistant.app.R;

/**
 * This abstract class is meant to be extended by each card.
 */
public abstract class CardFragment extends Fragment {
    private CardCallbacks mCallbacks;

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
        Activity activity = (Activity) context;
        try {
            mCallbacks = (CardFragment.CardCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement CardCallbacks");
        }
    }

    /**
     * An interface meant to be implemented by the enclosing activity. This is so the CardFragments
     * can communicate with the activity.
     */
    public interface CardCallbacks {
        /**
         * This function is meant to be run when the done menu item is clicked. Typically this will
         * cause a shuffle of the cards and display the title fragment.
         */
        void onDoneItemClick();
    }
}
