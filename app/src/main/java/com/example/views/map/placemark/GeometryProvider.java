package com.example.views.map.placemark;

import com.example.views.data.model.EntertainmentMap;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeometryProvider {
    private static final CameraPosition startPosition = new CameraPosition(
            new Point(53.19, 50.118),
            13.5f,
            310.0f,
            0.0f);

    private static List<EntertainmentMap> placesBrown = new ArrayList<>();
    private static List<EntertainmentMap> placesGreen = new ArrayList<>();
    private static List<EntertainmentMap> placesOrange = new ArrayList<>();
    private static List<EntertainmentMap> placesPink = new ArrayList<>();
    private static List<EntertainmentMap> placesPurple = new ArrayList<>();
    private static List<EntertainmentMap> placesBlue = new ArrayList<>();

    private static List<List<EntertainmentMap>> points = Arrays.asList(
            placesBrown, placesGreen, placesOrange, placesPink, placesPurple, placesBlue);

    private static boolean checkForNewIcon = false;

    // getter&setter
    public static CameraPosition getStartPosition(){
        return startPosition;
    }

    public static List<EntertainmentMap> getPlacesBrown() {
        return placesBrown;
    }

    public static void setPlacesBrown(List<EntertainmentMap> placesBrown) {
        GeometryProvider.placesBrown = placesBrown != null ? placesBrown : new ArrayList<>();
        updatePointsList();
    }

    public static List<EntertainmentMap> getPlacesGreen() {
        return placesGreen;
    }

    public static void setPlacesGreen(List<EntertainmentMap> placesGreen) {
        GeometryProvider.placesGreen = placesGreen != null ? placesGreen : new ArrayList<>();
        updatePointsList();
    }

    public static List<EntertainmentMap> getPlacesOrange() {
        return placesOrange;
    }

    public static void setPlacesOrange(List<EntertainmentMap> placesOrange) {
        GeometryProvider.placesOrange = placesOrange != null ? placesOrange : new ArrayList<>();
        updatePointsList();
    }

    public static List<EntertainmentMap> getPlacesPink() {
        return placesPink;
    }

    public static void setPlacesPink(List<EntertainmentMap> placesPink) {
        GeometryProvider.placesPink = placesPink != null ? placesPink : new ArrayList<>();
        updatePointsList();
    }

    public static List<EntertainmentMap> getPlacesPurple() {
        return placesPurple;
    }

    public static void setPlacesPurple(List<EntertainmentMap> placesPurple) {
        GeometryProvider.placesPurple = placesPurple != null ? placesPurple : new ArrayList<>();
        updatePointsList();
    }

    public static List<EntertainmentMap> getPlacesBlue() {
        return placesBlue;
    }

    public static void setPlacesBlue(List<EntertainmentMap> placesBlue) {
        GeometryProvider.placesBlue = placesBlue != null ? placesBlue : new ArrayList<>();
        updatePointsList();
    }

    public static List<List<EntertainmentMap>> getPoints() {
        checkForNewIcon = false;
        return points;
    }

    public static void setPoints(List<List<EntertainmentMap>> points) {
        GeometryProvider.points = points != null ? points : new ArrayList<>();
    }

    private static void updatePointsList() {
        points = Arrays.asList(placesBrown, placesGreen, placesOrange, placesPink, placesPurple, placesBlue);
    }

    public static void createAndSetPointsColor(List<EntertainmentMap> results) {
        checkForNewIcon = true;
        // Очищаем все списки перед заполнением
        placesBrown.clear();
        placesGreen.clear();
        placesOrange.clear();
        placesPink.clear();
        placesPurple.clear();
        placesBlue.clear();

        if (results == null) return;

        for (EntertainmentMap place : results) {
            int category_place = place.getCategory_id();
            switch (category_place) {
                case 1:
                case 2:
                    placesBrown.add(place);
                    break;
                case 11:
                    placesGreen.add(place);
                    break;
                case 14:
                    placesOrange.add(place);
                    break;
                case 7:
                    placesPink.add(place);
                    break;
                case 3:
                case 5:
                    placesBlue.add(place);
                    break;
                case 13:
                    placesPurple.add(place);
                    break;
            }
        }
        updatePointsList();
    }

    public static boolean isCheckForNewIcon() {
        return checkForNewIcon;
    }

    public static void setCheckForNewIcon(boolean checkForNewIcon) {
        GeometryProvider.checkForNewIcon = checkForNewIcon;
    }
}