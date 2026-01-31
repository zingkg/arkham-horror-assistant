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
import com.zingkg.arkhamhorrorboard.secondedition.card.NeighborhoodThreeLocations

class NeighborhoodThreeLocationsFragment : CardFragment() {
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
    return inflater?.inflate(R.layout.fragment_neighborhood_three_locations, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val neighborhood = importNeighborhood(arguments)
    if (neighborhood.location2Title.isEmpty() && neighborhood.location2Entry.isEmpty()) {
      view?.findViewById<View>(R.id.neighborhood_title_1_divider)?.visibility = View.INVISIBLE
      view?.findViewById<View>(R.id.neighborhood_title_2_divider)?.visibility = View.INVISIBLE
      view?.findViewById<View>(R.id.neighborhood_expansion_set_divider)?.visibility = View.INVISIBLE
    } else {
      view?.findViewById<TextView>(R.id.neighborhood_title_1)?.text = Html.fromHtml(neighborhood.location1Title)
      view?.findViewById<TextView>(R.id.neighborhood_entry_1)?.text = Html.fromHtml(neighborhood.location1Entry)
      view?.findViewById<TextView>(R.id.neighborhood_title_2)?.text = Html.fromHtml(neighborhood.location2Title)
      view?.findViewById<TextView>(R.id.neighborhood_entry_2)?.text = Html.fromHtml(neighborhood.location2Entry)
      view?.findViewById<TextView>(R.id.neighborhood_title_3)?.text = Html.fromHtml(neighborhood.location3Title)
      view?.findViewById<TextView>(R.id.neighborhood_entry_3)?.text = Html.fromHtml(neighborhood.location3Entry)
      view?.findViewById<TextView>(R.id.neighborhood_expansion_set)?.text = Html.fromHtml(neighborhood.expansionSet.shortName)
    }
    view?.findViewById<TextView>(R.id.neighborhood_title_1)?.text = Html.fromHtml(neighborhood.location1Title)
    view?.findViewById<TextView>(R.id.neighborhood_entry_1)?.text = Html.fromHtml(neighborhood.location1Entry)
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu.menu_decks, menu)
  }

  companion object {
    private const val TITLE_1_KEY = "title1"
    private const val ENTRY_1_KEY = "entry1"
    private const val TITLE_2_KEY = "title2"
    private const val ENTRY_2_KEY = "entry2"
    private const val TITLE_3_KEY = "title3"
    private const val ENTRY_3_KEY = "entry3"
    private const val EXPANSION_SET_KEY = "expansionSet"

    fun exportNeighborhood(neighborhoodThreeLocations: NeighborhoodThreeLocations): Bundle {
      val bundle = Bundle()
      bundle.putString(TITLE_1_KEY, neighborhoodThreeLocations.location1Title)
      bundle.putString(ENTRY_1_KEY, neighborhoodThreeLocations.location1Entry)
      bundle.putString(TITLE_2_KEY, neighborhoodThreeLocations.location2Title)
      bundle.putString(ENTRY_2_KEY, neighborhoodThreeLocations.location2Entry)
      bundle.putString(TITLE_3_KEY, neighborhoodThreeLocations.location3Title)
      bundle.putString(ENTRY_3_KEY, neighborhoodThreeLocations.location3Entry)
      bundle.putString(EXPANSION_SET_KEY, neighborhoodThreeLocations.expansionSet.toString())
      return bundle
    }

    fun importNeighborhood(bundle: Bundle): NeighborhoodThreeLocations {
      return NeighborhoodThreeLocations(
        bundle.getString(TITLE_1_KEY, ""),
        bundle.getString(ENTRY_1_KEY, ""),
        bundle.getString(TITLE_2_KEY, ""),
        bundle.getString(ENTRY_2_KEY, ""),
        bundle.getString(TITLE_3_KEY, ""),
        bundle.getString(ENTRY_3_KEY, ""),
        Cards.ExpansionSet.valueOf(bundle.getString(EXPANSION_SET_KEY, Cards.ExpansionSet.BASE.name))
      )
    }
  }
}