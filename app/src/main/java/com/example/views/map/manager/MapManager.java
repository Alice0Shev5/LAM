package com.example.views.map.manager;


import android.content.Context;
import android.widget.Toast;

import com.example.views.R;


// Map import
import com.example.views.map.placemark.ClusterView;
import com.example.views.map.placemark.GeometryProvider;
import com.example.views.map.placemark.PlacemarkType;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.map.ClusterListener;
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.ui_view.ViewProvider;
import com.yandex.mapkit.map.CameraPosition;

// Search import
import com.example.views.data.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapManager {
    private static final float DEFAULT_ZOOM = 17f;
    private static final float PLACEMARK_ICON_SCALE = 0.1f;
    private static final int CLUSTER_RADIUS = 20;
    private static final int MIN_CLUSTER_SIZE = 15;

    private MapView mapView;
    private Map map;
    private ClusterizedPlacemarkCollection clusterizedCollection;
    private HashMap<PlacemarkType, ImageProvider> placemarkIcons;
    private ClusterListener clusterListener = createClusterListener();
    private MapObjectTapListener placemarkTapListener = createPlacemarkTapListener();
    private Context context;

    Map.CameraCallback cameraCallback = new Map.CameraCallback() {
        @Override
        public void onMoveFinished(boolean isFinished) {
            // Handle camera move finished ...
        }
    };

    public MapManager(Context context, MapView mapView) {
        this.context = context;
        this.mapView = mapView;
        this.map = mapView.getMapWindow().getMap();

        setupBaseMap();
        initPlacemarkIcons();

        updateAllPlacemarks();
    }

    private ClusterListener createClusterListener() {
        return cluster -> {
            List<PlacemarkType> placemarkTypes = new ArrayList<>();
            for (PlacemarkMapObject placemark : cluster.getPlacemarks()) {
                EntertainmentMap place = (EntertainmentMap) placemark.getUserData();
                if (place != null) {
                    placemarkTypes.add(place.getType());
                }
            }

            // Устанавливает внешний вид каждого кластера с использованием пользовательского представления
            // которое отображает значки кластера
            cluster.getAppearance().setView(
                    new ViewProvider(
                            new ClusterView(context) {{
                                setData(placemarkTypes);
                            }}
                    )
            );
            cluster.getAppearance().setZIndex(100f);
            //cluster.addClusterTapListener(clusterTapListener);
        };
    }

    private MapObjectTapListener createPlacemarkTapListener() {
        return (mapObject, point) -> {
            EntertainmentMap place = (EntertainmentMap) mapObject.getUserData();
            if (place != null) {
                String message = String.format("%s\nРейтинг: %s⭐\nАдрес: %s",
                        place.getName(),
                        place.getRating(),
                        place.getAddress());
                Toast.makeText(mapView.getContext(), message, Toast.LENGTH_LONG).show();
            }
            return true;
        };
    }

    private void initPlacemarkIcons() {
        placemarkIcons = new HashMap<>();
        placemarkIcons.put(PlacemarkType.BROWN, ImageProvider.fromResource(context, R.drawable.pin_brown));
        placemarkIcons.put(PlacemarkType.GREEN, ImageProvider.fromResource(context, R.drawable.pin_green));
        placemarkIcons.put(PlacemarkType.ORANGE, ImageProvider.fromResource(context, R.drawable.pin_orange));
        placemarkIcons.put(PlacemarkType.PINK, ImageProvider.fromResource(context, R.drawable.pin_pink));
        placemarkIcons.put(PlacemarkType.PURPLE, ImageProvider.fromResource(context, R.drawable.pin_purple));
        placemarkIcons.put(PlacemarkType.BLUE, ImageProvider.fromResource(context, R.drawable.pin_blue));
    }

    private void setupBaseMap() {
        map.move(GeometryProvider.getStartPosition());
        MapObjectCollection collection = map.getMapObjects().addCollection();
        clusterizedCollection = collection.addClusterizedPlacemarkCollection(clusterListener);
    }

    public void updateAllPlacemarks(){
        if (clusterizedCollection == null || placemarkIcons == null ) return;

        // Очищаем предыдущие метки
        clusterizedCollection.clear();

        PlacemarkType[] types = PlacemarkType.values();

        for (int i = 0; i < types.length; i++) {
            ImageProvider icon = placemarkIcons.get(types[i]);
            if (icon == null) continue;

            List<EntertainmentMap> placesColor = GeometryProvider.getPoints().get(i);
            if (placesColor != null && !placesColor.isEmpty()) {
                            addMultiplePlacemarks(placesColor, icon);
                        }
        }

        clusterizedCollection.clusterPlacemarks(CLUSTER_RADIUS, MIN_CLUSTER_SIZE);
    }

    public void addMultiplePlacemarks(List<EntertainmentMap> places, ImageProvider icon) {
        for (EntertainmentMap place : places) {
            createAndAddPlacemark(place, icon);
        }
    }


    // метки по одной категории
    public void addOnlyOneMultiplePlacemarks(List<EntertainmentMap> places) {
        clusterizedCollection.clear();
        ImageProvider icon = placemarkIcons.get(places.get(0).getType());
        for (EntertainmentMap place : places) {
            createAndAddPlacemark(place, icon);
        }
        clusterizedCollection.clusterPlacemarks(CLUSTER_RADIUS, MIN_CLUSTER_SIZE);
    }

    private void createAndAddPlacemark(EntertainmentMap place, ImageProvider icon) {
        PlacemarkMapObject placemark = clusterizedCollection.addPlacemark();
        placemark.setGeometry(place.getPoint());
        placemark.setIcon(icon);
        placemark.setIconStyle(new IconStyle().setScale(PLACEMARK_ICON_SCALE));
        placemark.setUserData(place);
        placemark.addTapListener(placemarkTapListener);
    }

    public void moveCamera(Point point) {
        map.move(
                new CameraPosition(
                        point,
                        DEFAULT_ZOOM,
                        310.0f,
                        0.0f),
                new Animation(Animation.Type.LINEAR, 1f),
                cameraCallback
        );
        // map.move(new CameraPosition(point, 17f, 0, 0));
    }

    public void onStart() {
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }
}
