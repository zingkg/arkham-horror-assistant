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
import com.zingkg.arkhamhorrorboard.secondedition.card.Cards
import com.zingkg.arkhamhorrorboard.secondedition.card.CultEncounter

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
    val (title, lore, entry, expansionSet) = importCultEncounter(arguments)
    if (lore.isEmpty() && entry.isEmpty()) {
      view?.findViewById<View>(R.id.cult_encounter_title_divider)?.visibility = View.INVISIBLE
      view?.findViewById<View>(R.id.cult_encounter_expansion_set_divider)?.visibility = View.INVISIBLE
    } else {
      view?.findViewById<TextView>(R.id.cult_encounter_lore)?.text = Html.fromHtml(lore)
      view?.findViewById<TextView>(R.id.cult_encounter_entry)?.text = Html.fromHtml(entry)
      view?.findViewById<TextView>(R.id.cult_encounter_expansion_set)?.text = Html.fromHtml(expansionSet.shortName)
    }
    (view?.findViewById<View>(R.id.cult_encounter_title) as TextView).text = Html.fromHtml(title)
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu.menu_decks, menu)
  }

  companion object {
    private const val TITLE_KEY = "title"
    private const val LORE_KEY = "lore"
    private const val ENTRY_KEY = "entry"
    private const val EXPANSION_SET_KEY = "expansionSet"

    fun exportCultEncounter(cultEncounter: CultEncounter): Bundle {
      val bundle = Bundle()
      bundle.putString(TITLE_KEY, cultEncounter.title)
      bundle.putString(LORE_KEY, cultEncounter.lore)
      bundle.putString(ENTRY_KEY, cultEncounter.entry)
      bundle.putString(EXPANSION_SET_KEY, cultEncounter.expansionSet.toString())
      return bundle
    }

    fun importCultEncounter(bundle: Bundle): CultEncounter {
      return CultEncounter(
        bundle.getString(TITLE_KEY, ""),
        bundle.getString(LORE_KEY, ""),
        bundle.getString(ENTRY_KEY, ""),
        Cards.ExpansionSet.valueOf(bundle.getString(EXPANSION_SET_KEY, Cards.ExpansionSet.BASE.name))
      )
    }
  }
}
