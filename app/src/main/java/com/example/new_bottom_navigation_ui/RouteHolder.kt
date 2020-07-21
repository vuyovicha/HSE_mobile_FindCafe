package com.example.new_bottom_navigation_ui

data class RouteHolder(
    val formatVersion: String,
    val routes: List<Route>
)

data class Leg(
    val points: List<Point>,
    val summary: SummaryRoute
)

data class Point(
    val latitude: Double,
    val longitude: Double
)

data class Route(
    val legs: List<Leg>,
    val sections: List<Section>,
    val summary: SummaryRoute
)

data class Section(
    val endPointIndex: Int,
    val sectionType: String,
    val startPointIndex: Int,
    val travelMode: String
)

data class SummaryRoute(
    val arrivalTime: String,
    val departureTime: String,
    val lengthInMeters: Int,
    val trafficDelayInSeconds: Int,
    val travelTimeInSeconds: Int
)
