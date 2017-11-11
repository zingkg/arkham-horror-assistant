package app.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.zingkg.arkhamhorrorassistant.app.R
import com.zingkg.arkhamhorrorassistant.xml.ExhibitEncounter

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
        val (title, entry, location) = importExhibitEncounter(arguments)
        if (entry.isEmpty() && location.isEmpty()) {
            view?.findViewById<View>(R.id.exhibit_encounter_title_divider)?.visibility = View.INVISIBLE
            view?.findViewById<View>(R.id.exhibit_encounter_entry_divider)?.visibility = View.INVISIBLE
        } else {
            view?.findViewById<TextView>(R.id.exhibit_encounter_entry)?.text = Html.fromHtml(entry)
            view?.findViewById<TextView>(R.id.exhibit_encounter_location)?.text = Html.fromHtml(location)
        }
        (view?.findViewById<View>(R.id.exhibit_encounter_title) as TextView).text = Html.fromHtml(title)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_decks, menu)
    }

    companion object {
        private val titleKey = "title"
        private val entryKey = "entry"
        private val locationKey = "location"
        private val expansionSetKey = "expansionSet"

        fun exportExhibitEncounter(exhibitEncounter: ExhibitEncounter): Bundle {
            val bundle = Bundle()
            bundle.putString(titleKey, exhibitEncounter.title)
            bundle.putString(entryKey, exhibitEncounter.entry)
            bundle.putString(locationKey, exhibitEncounter.location)
            bundle.putString(expansionSetKey, exhibitEncounter.expansionSet)
            return bundle
        }

        fun importExhibitEncounter(bundle: Bundle): ExhibitEncounter {
            return ExhibitEncounter(
                bundle.getString(titleKey, ""),
                bundle.getString(entryKey, ""),
                bundle.getString(locationKey, ""),
                bundle.getString(expansionSetKey, "")
            )
        }
    }
}
