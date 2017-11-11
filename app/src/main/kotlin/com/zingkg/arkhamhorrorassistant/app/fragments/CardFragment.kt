package app.fragments

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.view.MenuItem

import com.zingkg.arkhamhorrorassistant.app.R

abstract class CardFragment : Fragment() {
    private var callbacks: CardCallbacks? = null

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_done -> {
                callbacks?.onDoneItemClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as Activity?
        try {
            callbacks = activity as CardCallbacks?
        } catch (e: ClassCastException) {
            throw ClassCastException(activity?.toString() + " must implement CardCallbacks")
        }
    }

    interface CardCallbacks {
        fun onDoneItemClick()
    }
}
