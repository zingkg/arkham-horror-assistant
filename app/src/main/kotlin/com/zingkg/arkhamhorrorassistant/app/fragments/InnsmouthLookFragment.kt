package com.zingkg.arkhamhorrorassistant.app.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.zingkg.arkhamhorrorassistant.app.R
import com.zingkg.arkhamhorrorassistant.xml.InnsmouthLook

class InnsmouthLookFragment : CardFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.fragment_innsmouth, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val (lore, entry) = importInnsmouthLook(arguments)
        val loreView = view?.findViewById<TextView>(R.id.innsmouth_lore)
        if (!lore.isEmpty() && entry.isEmpty()) {
            // Simply a title.
            loreView?.textSize = calculateTitleTextSize()
            view?.findViewById<View>(R.id.innsmouth_divider)?.visibility = View.INVISIBLE
        } else {
            view?.findViewById<TextView>(R.id.innsmouth_entry)?.text = Html.fromHtml(entry)
        }
        loreView?.text = Html.fromHtml(lore)
    }

    private fun calculateTitleTextSize(): Float {
        val resources = resources
        return resources.getDimension(R.dimen.title) / resources.displayMetrics.density
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_decks, menu)
    }

    companion object {
        private val loreKey = "lore"
        private val entryKey = "entry"
        private val expansionSetKey = "expansionSet"

        fun exportInnsmouthLook(innsmouthLook: InnsmouthLook): Bundle {
            val bundle = Bundle()
            bundle.putString(loreKey, innsmouthLook.lore)
            bundle.putString(entryKey, innsmouthLook.entry)
            bundle.putString(expansionSetKey, innsmouthLook.expansionSet)
            return bundle
        }

        fun importInnsmouthLook(bundle: Bundle): InnsmouthLook {
            return InnsmouthLook(
                bundle.getString(loreKey, ""),
                bundle.getString(entryKey, ""),
                bundle.getString(expansionSetKey, "")
            )
        }
    }
}
