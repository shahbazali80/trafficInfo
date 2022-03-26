package com.example.trafficscotlandapp.models

import com.google.android.gms.maps.model.LatLng

data class CurrentIncidents(
    val title : String,
    val description : String,
    val pubDate : String,
    val latLng : String,
    val link : String
) {
}