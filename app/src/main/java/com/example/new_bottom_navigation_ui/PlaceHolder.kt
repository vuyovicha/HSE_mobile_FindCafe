package com.hse.findcafe

data class PlaceHolder(
    val results: List<Result>,
    val summary: Summary
)

data class Address(
    val country: String,
    val countryCode: String,
    val countryCodeISO3: String,
    val countrySecondarySubdivision: String,
    val countrySubdivision: String,
    val countryTertiarySubdivision: String,
    val freeformAddress: String,
    val localName: String,
    val municipality: String,
    val municipalitySubdivision: String,
    val postalCode: String,
    val streetName: String,
    val streetNumber: String
)

data class BtmRightPoint(
    val lat: Double,
    val lon: Double
)

data class EntryPoint(
    val position: Position,
    val type: String
)

data class Position(
    val lat: Double,
    val lon: Double
)

data class Result(
    val address: Address,
    val entryPoints: List<EntryPoint>,
    val id: String,
    val position: Position,
    val score: Double,
    val type: String,
    val viewport: Viewport
)

data class Summary(
    val fuzzyLevel: Int,
    val numResults: Int,
    val offset: Int,
    val query: String,
    val queryTime: Int,
    val queryType: String,
    val totalResults: Int
)

data class TopLeftPoint(
    val lat: Double,
    val lon: Double
)

data class Viewport(
    val btmRightPoint: BtmRightPoint,
    val topLeftPoint: TopLeftPoint
)