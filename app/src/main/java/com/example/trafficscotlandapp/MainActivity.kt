package com.example.trafficscotlandapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader


class MainActivity : AppCompatActivity() {

    var client = OkHttpClient()
    private var url = "https://trafficscotland.org/rss/feeds/currentincidents.aspx"
    var myResponse = ""

    var titleTxt = ""
    var descriptionTxt = ""
    var pubDateTxt = ""
    var linkTxt = ""
    var latTxt = ""
    var longTxt = ""

    val accidentList = ArrayList<CurrentIncidents>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                //Log.d("response", myResponse)
                //testing()
                againTesting()
            }
        })

        for (item in accidentList){
            Log.d("array", item.title+"\n")
            Log.d("array", item.description+"\n")
            Log.d("array", item.link+"\n")
            Log.d("array", item.pubDate+"\n\n")
        }
    }

    private fun againTesting() {
        try {

            var response = ""
            var text = ""
            var foundItem = false

            val buffer = StringBuffer()

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
                    } else if (foundItem && tagName.equals("title", ignoreCase = true)) {
                        //buffer.append("title: $text\n")
                        titleTxt = text
                    } else if (foundItem && tagName.contains("description", ignoreCase = true)) {
                        //buffer.append("description: $text\n")
                        descriptionTxt = text
                    } else if (foundItem && tagName.equals("link", ignoreCase = true)) {
                        //buffer.append("link: $text\n")
                        linkTxt = text
                    } else if (foundItem && tagName.contains("point", ignoreCase = true)) {
                        //buffer.append("point: $text\n")
                        val latLngParts: List<String> = text.split(" ")
                        latTxt = latLngParts[0]
                        longTxt = latLngParts[1]
                    } else if (foundItem && tagName.equals("pubDate", ignoreCase = true)) {
                        //buffer.append("pubDate: $text\n")
                        pubDateTxt = text
                    }
                }

                if(foundItem)
                    accidentList.add(CurrentIncidents(titleTxt,descriptionTxt,pubDateTxt, LatLng(latTxt.toDoubleOrNull()?:0.0,longTxt.toDoubleOrNull()?:0.0),linkTxt))

                eventType = parser.next()
            }

            //Log.d("tagVales", buffer.toString())
            Log.d("tagVales", accidentList.toString())

            tv_response.text = response

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun testing() {
        try {

            var response = ""

            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(StringReader(myResponse))
            var eventType = xpp.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_DOCUMENT -> {
                        //System.out.println("Start document");
                    }
                    XmlPullParser.END_DOCUMENT -> {
                        //System.out.println("End document");
                    }
                    XmlPullParser.START_TAG -> {
                        println("Start tag "+xpp.name);
                    }
                    XmlPullParser.END_TAG -> {
                        println("End tag "+xpp.name);
                    }
                    XmlPullParser.TEXT -> {
                        println("Text "+xpp.text);
                        response += xpp.text
                        Log.d("response", xpp.text)
                    }
                }
                eventType = xpp.next()
            }

            tv_response.text = response

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}