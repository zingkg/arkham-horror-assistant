package com.zingkg.arkhamhorrorboard.secondedition.app.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.zingkg.arkhamhorrorboard.secondedition.app.R
import com.zingkg.arkhamhorrorboard.secondedition.xml.CultEncounter

class CultEncounterFragment : CardFragment() {
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
        return inflater?.inflate(R.layout.fragment_cult_encounter, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val (title, lore, entry) = importCultEncounter(arguments)
        if (lore.isEmpty() && entry.isEmpty()) {
            view?.findViewById<View>(R.id.cult_encounter_title_divider)?.visibility = View.INVISIBLE
        } else {
            view?.findViewById<TextView>(R.id.cult_encounter_lore)?.text = Html.fromHtml(lore)
            view?.findViewById<TextView>(R.id.cult_encounter_entry)?.text = Html.fromHtml(entry)
        }
        (view?.findViewById<View>(R.id.cult_encounter_title) as TextView).text = Html.fromHtml(title)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_decks, menu)
    }

    companion object {
        private val titleKey = "title"
        private val loreKey = "lore"
        private val entryKey = "entry"
        private val expansionSetKey = "expansionSet"

        fun exportCultEncounter(cultEncounter: CultEncounter): Bundle {
            val bundle = Bundle()
            bundle.putString(titleKey, cultEncounter.title)
            bundle.putString(loreKey, cultEncounter.lore)
            bundle.putString(entryKey, cultEncounter.entry)
            bundle.putString(expansionSetKey, cultEncounter.expansionSet)
            return bundle
        }

        fun importCultEncounter(bundle: Bundle): CultEncounter {
            return CultEncounter(
                bundle.getString(titleKey, ""),
                bundle.getString(loreKey, ""),
                bundle.getString(entryKey, ""),
                bundle.getString(expansionSetKey, "")
            )
        }
    }
}
