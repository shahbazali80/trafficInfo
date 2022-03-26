package com.example.trafficscotlandapp.ui.plannedroadworks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trafficscotlandapp.models.CurrentIncidents
import com.example.trafficscotlandapp.R
import com.example.trafficscotlandapp.adapters.TrafficInfoAdapter
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_planned_roadworks.*
import kotlinx.android.synthetic.main.fragment_planned_roadworks.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.jsoup.Jsoup
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

class PlannedRoadworksFragment : Fragment() {

    private var client = OkHttpClient()
    private var url = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx"
    var myResponse = ""

    private var titleTxt = ""
    private var descriptionTxt = ""
    private var pubDateTxt = ""
    private var linkTxt = ""
    private var latLongTxt = ""

    private val plannedRoadWorksList = ArrayList<CurrentIncidents>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_planned_roadworks, container, false)

        view.plannedRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            apiRequest()
        }
        return view

        return view
    }

    private fun apiRequest() {

        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("response", e.toString())
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                myResponse = response.body!!.string()
                fetchItemInfo()
                if(plannedRoadWorksList!=null) {
                    plannedProgressBar.visibility = View.INVISIBLE
                    setAdapterValues()
                }
            }
        })
    }

    private fun setAdapterValues() {

        activity?.runOnUiThread {
            val adapter = TrafficInfoAdapter(plannedRoadWorksList)
            requireView().plannedRecyclerView.adapter = adapter
        }
    }

    private fun fetchItemInfo() {
        try {

            var text = ""
            var foundItem = false

            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(StringReader(myResponse))
            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name

                when (eventType) {
                    XmlPullParser.START_TAG -> if (tagName.equals("item", ignoreCase = true)) {
                        foundItem = true
                    }
                    XmlPullParser.TEXT -> text = parser.text

                    XmlPullParser.END_TAG -> if (tagName.equals("item", ignoreCase = true)) {
                        foundItem = false
                        plannedRoadWorksList.add(
                            CurrentIncidents(titleTxt,descriptionTxt,pubDateTxt, latLongTxt,linkTxt)
                        )
                    } else if (foundItem && tagName.equals("title", ignoreCase = true)) {
                        titleTxt = text
                    } else if (foundItem && tagName.contains("description", ignoreCase = true)) {
                        descriptionTxt = Jsoup.parse(text).text()
                    } else if (foundItem && tagName.equals("link", ignoreCase = true)) {
                        linkTxt = text
                    } else if (foundItem && tagName.contains("point", ignoreCase = true)) {
                        latLongTxt = text
                    } else if (foundItem && tagName.equals("pubDate", ignoreCase = true)) {
                        pubDateTxt = text
                    }
                }
                eventType = parser.next()
            }

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}