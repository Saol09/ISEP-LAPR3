package lapr.project.utils;

/**
 * The type Distances calculus.
 */
public class DistancesCalculus {
    /**
     * Distance double.
     *
     * @param lat1 the lat 1
     * @param lon1 the lon 1
     * @param lat2 the lat 2
     * @param lon2 the lon 2
     * @param unit the unit
     * @return the double
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

    /**
     * Distance double.
     *
     * @param latitudeA  the latitude a
     * @param longitudeA the longitude a
     * @param latitudeB  the latitude b
     * @param longitudeB the longitude b
     * @return the double
     */
    public static double distance(double latitudeA, double longitudeA, double latitudeB, double longitudeB) {
        //The ship hasn't travelled any distance yet
        if ((latitudeA == latitudeB) && (longitudeA == longitudeB)) {
            return 0;
        } else {
            double aux = longitudeA - longitudeB;
            double distance = Math.sin(Math.toRadians(latitudeA)) * Math.sin(Math.toRadians(latitudeB)) + Math.cos(Math.toRadians(latitudeA))
                    * Math.cos(Math.toRadians(latitudeB)) * Math.cos(Math.toRadians(aux));
            distance = Math.acos(distance);
            distance = Math.toDegrees(distance);
            distance = distance * 60 * 1.1515;
            distance = distance * 1.609344;
            return (distance);
        }
    }
}
