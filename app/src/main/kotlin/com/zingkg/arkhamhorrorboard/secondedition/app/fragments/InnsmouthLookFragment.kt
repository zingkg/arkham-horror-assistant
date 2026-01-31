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
import com.zingkg.arkhamhorrorboard.secondedition.card.InnsmouthLook

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
    return inflater?.inflate(R.layout.fragment_innsmouth_look, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val (lore, entry, expansionSet) = importInnsmouthLook(arguments)
    val loreView = view?.findViewById<TextView>(R.id.innsmouth_look_lore)
    if (lore.isNotEmpty() && entry.isEmpty()) {
      // Simply a title.
      loreView?.textSize = calculateTitleTextSize()
      view?.findViewById<View>(R.id.innsmouth_look_divider)?.visibility = View.INVISIBLE
      view?.findViewById<View>(R.id.innsmouth_look_expansion_set_divider)?.visibility = View.INVISIBLE
    } else {
      view?.findViewById<TextView>(R.id.innsmouth_look_entry)?.text = Html.fromHtml(entry)
      view?.findViewById<TextView>(R.id.innsmouth_look_expansion_set)?.text = Html.fromHtml(expansionSet.shortName)
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
    private const val LORE_KEY = "lore"
    private const val ENTRY_KEY = "entry"
    private const val EXPANSION_SET_KEY = "expansionSet"

    fun exportInnsmouthLook(innsmouthLook: InnsmouthLook): Bundle {
      val bundle = Bundle()
      bundle.putString(LORE_KEY, innsmouthLook.lore)
      bundle.putString(ENTRY_KEY, innsmouthLook.entry)
      bundle.putString(EXPANSION_SET_KEY, innsmouthLook.expansionSet.toString())
      return bundle
    }

    fun importInnsmouthLook(bundle: Bundle): InnsmouthLook {
      return InnsmouthLook(
        bundle.getString(LORE_KEY, ""),
        bundle.getString(ENTRY_KEY, ""),
        Cards.ExpansionSet.valueOf(bundle.getString(EXPANSION_SET_KEY, Cards.ExpansionSet.BASE.name))
      )
    }
  }
}
