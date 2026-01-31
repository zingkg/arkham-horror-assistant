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
import com.zingkg.arkhamhorrorboard.secondedition.card.ExhibitEncounter

class ExhibitEncounterFragment : CardFragment() {
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
    return inflater?.inflate(R.layout.fragment_exhibit_encounter, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val (title, entry, location, expansionSet) = importExhibitEncounter(arguments)
    if (entry.isEmpty() && location.isEmpty()) {
      view?.findViewById<View>(R.id.exhibit_encounter_title_divider)?.visibility = View.INVISIBLE
      view?.findViewById<View>(R.id.exhibit_encounter_entry_divider)?.visibility = View.INVISIBLE
      view?.findViewById<View>(R.id.exhibit_encounter_expansion_set_divider)?.visibility = View.INVISIBLE
    } else {
      view?.findViewById<TextView>(R.id.exhibit_encounter_entry)?.text = Html.fromHtml(entry)
      view?.findViewById<TextView>(R.id.exhibit_encounter_location)?.text = Html.fromHtml(location)
      view?.findViewById<TextView>(R.id.exhibit_encounter_location)?.text = Html.fromHtml(expansionSet.shortName)
    }
    (view?.findViewById<View>(R.id.exhibit_encounter_title) as TextView).text = Html.fromHtml(title)
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu.menu_decks, menu)
  }

  companion object {
    private const val TITLE_KEY = "title"
    private const val ENTRY_KEY = "entry"
    private const val LOCATION_KEY = "location"
    private const val EXPANSION_SET_KEY = "expansionSet"

    fun exportExhibitEncounter(exhibitEncounter: ExhibitEncounter): Bundle {
      val bundle = Bundle()
      bundle.putString(TITLE_KEY, exhibitEncounter.title)
      bundle.putString(ENTRY_KEY, exhibitEncounter.entry)
      bundle.putString(LOCATION_KEY, exhibitEncounter.location)
      bundle.putString(EXPANSION_SET_KEY, exhibitEncounter.expansionSet.toString())
      return bundle
    }

    fun importExhibitEncounter(bundle: Bundle): ExhibitEncounter {
      return ExhibitEncounter(
        bundle.getString(TITLE_KEY, ""),
        bundle.getString(ENTRY_KEY, ""),
        bundle.getString(LOCATION_KEY, ""),
        Cards.ExpansionSet.valueOf(bundle.getString(EXPANSION_SET_KEY, Cards.ExpansionSet.BASE.name))
      )
    }
  }
}
