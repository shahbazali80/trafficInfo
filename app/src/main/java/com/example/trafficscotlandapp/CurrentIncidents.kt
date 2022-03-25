package com.example.trafficscotlandapp

import com.google.android.gms.maps.model.LatLng

data class CurrentIncidents(
    val title : String,
    val description : String,
    val pubDate : String,
    val latLng : LatLng,
    val link : String
) {
}