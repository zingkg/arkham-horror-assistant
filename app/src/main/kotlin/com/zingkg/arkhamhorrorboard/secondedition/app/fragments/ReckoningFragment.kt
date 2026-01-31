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
import com.zingkg.arkhamhorrorboard.secondedition.card.Cards
import com.zingkg.arkhamhorrorboard.secondedition.card.Reckoning

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
    val (title, entry, expansionSet) = importReckoning(arguments)
    if (entry.isEmpty()) {
      view?.findViewById<LinearLayout>(R.id.reckoning_layout)?.gravity = Gravity.CENTER_HORIZONTAL
      view?.findViewById<View>(R.id.reckoning_divider)?.visibility = View.INVISIBLE
      view?.findViewById<View>(R.id.reckoning_expansion_set_divider)?.visibility = View.INVISIBLE
    } else {
      view?.findViewById<TextView>(R.id.reckoning_entry)?.text = Html.fromHtml(entry)
      view?.findViewById<TextView>(R.id.reckoning_expansion_set)?.text = Html.fromHtml(expansionSet.shortName)
    }
    view?.findViewById<TextView>(R.id.reckoning_title)?.text = Html.fromHtml(title)
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu.menu_decks, menu)
  }

  companion object {
    private const val TITLE_KEY = "title"
    private const val ENTRY_KEY = "entry"
    private const val EXPANSION_SET_KEY = "expansionSetKey"

    fun exportReckoning(reckoning: Reckoning): Bundle {
      val bundle = Bundle()
      bundle.putString(TITLE_KEY, reckoning.title)
      bundle.putString(ENTRY_KEY, reckoning.entry)
      bundle.putString(EXPANSION_SET_KEY, reckoning.expansionSet.toString())
      return bundle
    }

    fun importReckoning(bundle: Bundle): Reckoning {
      return Reckoning(
        bundle.getString(TITLE_KEY, ""),
        bundle.getString(ENTRY_KEY, ""),
        Cards.ExpansionSet.valueOf(bundle.getString(EXPANSION_SET_KEY, Cards.ExpansionSet.BASE.name))
      )
    }
  }
}
