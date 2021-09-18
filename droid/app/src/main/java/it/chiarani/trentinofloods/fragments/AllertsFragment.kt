package it.chiarani.trentinofloods.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.chiarani.trentinofloods.R
import it.chiarani.trentinofloods.adapters.AllertAdapter
import it.chiarani.trentinofloods.adapters.OnItemClickListener
import it.chiarani.trentinofloods.data.AllertItem
import it.chiarani.trentinofloods.databinding.FragmentAllertsBinding
import it.chiarani.trentinofloods.viewModels.ProtCivViewModel
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*


class AllertsFragment : Fragment(R.layout.fragment_allerts), OnItemClickListener {

    private lateinit var binding: FragmentAllertsBinding
    private val allertItems = mutableListOf<AllertItem>()
    private val viewModel: ProtCivViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAllertsBinding.bind(view)

        binding.cardSearchIcon.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getAllerts().observe(viewLifecycleOwner) {
            val doc: Document = it

            val links: Elements = doc.select("ul li span a")
            val dates: Elements = doc.select("ul li span span")

            var counter = 0
            for (link in links) {
                try {
                    val onclick_attr: String = link.attr("onclick")
                    val sub_onclick = onclick_attr.substring(13)
                    val sub_link = sub_onclick.split(",".toRegex()).toTypedArray()[0]
                    val document_link = sub_link.substring(0, sub_link.length - 1)
                    var final_link = "http://avvisi.protezionecivile.tn.it$document_link"
                    final_link = "https://docs.google.com/gview?url=$final_link&embedded=true"
                    val date_pt1: String = dates.get(counter).text().split(" ").get(1)
                    val date_pt2: String = dates.get(counter).text().split(" ").get(2)
                    val date_pt3: String = dates.get(counter).text().split(" ").get(3)

                    // add element to list
                    allertItems.add(
                        AllertItem(
                            link.text(),
                            date_pt1,
                            date_pt2,
                            date_pt3,
                            final_link
                        )
                    )
                } catch (ex: Exception) {
                    // nothing
                    val e = ex.message
                }
                counter++
                if (counter >= 10) break
            }

            val mAdapter = AllertAdapter(allertItems, this)
            val linearLayoutManagerTags = LinearLayoutManager(
                requireActivity().applicationContext
            )

            linearLayoutManagerTags.orientation = RecyclerView.VERTICAL

            binding.fragmentAllertRv.setLayoutManager(linearLayoutManagerTags)

            binding.fragmentAllertRv.setAdapter(mAdapter)
            binding.fragmentAllertRv.visibility = View.VISIBLE
            binding.fragmentAvalancheAnimLoad.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 750
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onItemClick(position: Int) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(allertItems[position].link)
        startActivity(i)
    }

}