package com.example.new_bottom_navigation_ui

import java.lang.Math.toRadians
import kotlin.math.*

class CalculatingPoints(var x: Double, var y: Double) {

    companion object {
        fun getCenterFromDegrees(data: Array<CalculatingPoints>): CalculatingPoints {
            if (data.isEmpty()){
                throw Exception("пусто...");
            }

            var numCoords = data.size;

            var X = 0.0;
            var Y = 0.0;
            var Z = 0.0;

            for(el: CalculatingPoints in data){
                var lat = el.x * Math.PI / 180;
                var lon = el.y * Math.PI / 180;

                var a = cos(lat) * cos(lon);
                var b = cos(lat) * sin(lon);
                var c = sin(lat);

                X += a;
                Y += b;
                Z += c;
            }

            X /= numCoords;
            Y /= numCoords;
            Z /= numCoords;

            var lon = Math.atan2(Y, X);
            var hyp = Math.sqrt(X * X + Y * Y);
            var lat = Math.atan2(Z, hyp);

            var newX = (lat * 180 / Math.PI);
            var newY = (lon * 180 / Math.PI);

            return CalculatingPoints(newX, newY)
        }

        fun getSegmentCenter (point1 : PointString, point2 : PointString) : PointString {
            val doublePoint =  getCenterFromDegrees(arrayOf(
                CalculatingPoints(point1.latitude.toDouble(), point1.longitude.toDouble()),
                CalculatingPoints(point2.latitude.toDouble(), point2.longitude.toDouble())
                ))
            return PointString(doublePoint.x.toString(), doublePoint.y.toString())
        }

        fun getDistance(point1 : PointString, point2 : PointString) : Double {
            return distVincenty(point1.latitude.toDouble(), point1.longitude.toDouble(), point2.latitude.toDouble(), point2.longitude.toDouble())
        }


        /**
         * Calculates geodetic distance between two points specified by latitude/longitude using Vincenty inverse formula
         * for ellipsoids
         *
         * @param lat1
         * first point latitude in decimal degrees
         * @param lon1
         * first point longitude in decimal degrees
         * @param lat2
         * second point latitude in decimal degrees
         * @param lon2
         * second point longitude in decimal degrees
         * @returns distance in meters between points with 5.10<sup>-4</sup> precision
         * @see [Originally posted here](http://www.movable-type.co.uk/scripts/latlong-vincenty.html)
         */

        fun distVincenty(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double
        ): Double {
            val a = 6378137.0
            val b = 6356752.314245
            val f = 1 / 298.257223563 // WGS-84 ellipsoid params
            val L = Math.toRadians(lon2 - lon1)
            val u1 = atan((1 - f) * tan(toRadians(lat1)))
            val u2 = atan((1 - f) * tan(toRadians(lat2)))
            val sinU1 = sin(u1)
            val cosU1 = cos(u1)
            val sinU2 = sin(u2)
            val cosU2 = cos(u2)
            var sinLambda: Double
            var cosLambda: Double
            var sinSigma: Double
            var cosSigma: Double
            var sigma: Double
            var sinAlpha: Double
            var cosSqAlpha: Double
            var cos2SigmaM: Double
            var lambda = L
            var lambdaP: Double
            var iterLimit = 100.0
            do {
                sinLambda = sin(lambda)
                cosLambda = cos(lambda)
                sinSigma = sqrt(
                    cosU2 * sinLambda * (cosU2 * sinLambda)
                            + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
                )
                if (sinSigma == 0.0) return 0.0 // co-incident points
                cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda
                sigma = atan2(sinSigma, cosSigma)
                sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma
                cosSqAlpha = 1 - sinAlpha * sinAlpha
                cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha
                if (java.lang.Double.isNaN(cos2SigmaM)) cos2SigmaM =
                    0.0 // equatorial line: cosSqAlpha=0 (§6)
                val C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha))
                lambdaP = lambda
                lambda = L + ((1 - C) * f * sinAlpha
                        * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM))))
            } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0)
            if (iterLimit == 0.0) return Double.NaN // formula failed to converge
            val uSq = cosSqAlpha * (a * a - b * b) / (b * b)
            val A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)))
            val B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)))
            val deltaSigma = (B
                    * sinSigma
                    * (cos2SigmaM + B
                    / 4
                    * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - (B / 6 * cos2SigmaM
                    * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)))))
            return b * A * (sigma - deltaSigma) / 1000 //from m to km?
        }
    }



}

