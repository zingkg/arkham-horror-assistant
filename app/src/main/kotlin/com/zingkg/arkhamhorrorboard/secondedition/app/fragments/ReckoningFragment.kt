package com.zingkg.arkhamhorrorboard.secondedition.app.fragments

import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.zingkg.arkhamhorrorboard.secondedition.app.R
import com.zingkg.arkhamhorrorboard.secondedition.xml.Reckoning

class ReckoningFragment : CardFragment() {
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
        return inflater?.inflate(R.layout.fragment_reckoning, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val (title, entry) = importReckoning(arguments)
        if (entry.isEmpty()) {
            view?.findViewById<LinearLayout>(R.id.reckoning_layout)?.gravity = Gravity.CENTER_HORIZONTAL
            view?.findViewById<View>(R.id.reckoning_divider)?.visibility = View.INVISIBLE
        } else {
            view?.findViewById<TextView>(R.id.reckoning_entry)?.text = Html.fromHtml(entry)
        }
        view?.findViewById<TextView>(R.id.reckoning_title)?.text = Html.fromHtml(title)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_decks, menu)
    }

    companion object {
        private val titleKey = "title"
        private val entryKey = "entry"
        private val expansionSetKey = "expansionSetKey"

        fun exportReckoning(reckoning: Reckoning): Bundle {
            val bundle = Bundle()
            bundle.putString(titleKey, reckoning.title)
            bundle.putString(entryKey, reckoning.entry)
            bundle.putString(expansionSetKey, reckoning.expansionSet)
            return bundle
        }

        fun importReckoning(bundle: Bundle): Reckoning {
            return Reckoning(
                bundle.getString(titleKey, ""),
                bundle.getString(entryKey, ""),
                bundle.getString(expansionSetKey, "")
            )
        }
    }
}
