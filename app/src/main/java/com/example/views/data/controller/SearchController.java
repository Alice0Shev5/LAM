package com.example.views.data.controller;


import android.content.Context;
import android.widget.Toast;

import com.example.views.map.manager.MapManager;
import com.example.views.map.placemark.GeometryProvider;

// Search import
import com.example.views.data.model.*;
import com.example.views.data.manager.SearchManager;

import java.util.List;


public class SearchController implements SearchManager.SearchResultListener {
    private SearchManager searchManager;
    private MapManager mapManager;
    private Context context;

    private boolean checkMap = true;

    public SearchController(Context context, MapManager mapManager) {
        this.context = context;
        this.mapManager = mapManager;
        this.searchManager = new SearchManager(context, this);
    }

    public void performSearch(String query) {
        if (query.isEmpty()) {
            Toast.makeText(context, "Введите запрос", Toast.LENGTH_SHORT).show();
            checkUpdatePlacemarks();
            return;
        }
        searchManager.performSearch(query);
    }

    public void loadAllPlaces() {
        searchManager.getAllPlaces();
    }

    // Возвращаем все метки на карту, если ранее показывали только одну категорию
    public void  checkUpdatePlacemarks(){
        if (!checkMap){
            mapManager.updateAllPlacemarks();
            checkMap = true;
        }
    }

    @Override
    public void onSearchResults(List<EntertainmentMap> results) {
        if (results == null || results.isEmpty()) {
            Toast.makeText(context, "Ничего не найдено", Toast.LENGTH_SHORT).show();
            return;
        }
        // Обработка результата (единичного и по категориям)
        if (results.size() == 1) {
            checkUpdatePlacemarks();  // проверка на месте ли все метки
            EntertainmentMap place = results.get(0);
            mapManager.moveCamera(place.getPoint());
            Toast.makeText(context, "Найдено: " + place.getName() + " (" + place.getRating() + "⭐)", Toast.LENGTH_SHORT).show();
        }
        else {
            checkMap = false;  // выставлены метки только одной категории
            mapManager.addOnlyOneMultiplePlacemarks(results);
        }
    }

    @Override
    public void onSearchAllResults(List<EntertainmentMap> results) {
        if (results == null || results.isEmpty()) {
            Toast.makeText(context, "Ничего не найдено", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создаем и устанавливаем точки
        GeometryProvider.createAndSetPointsColor(results);

        // Теперь отображаем метки, так как данные загружены
        if (mapManager != null) mapManager.updateAllPlacemarks();

        Toast.makeText(context, "Общее количество мест: " + results.size(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSearchError(String error) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }

    public void onLoading(boolean isLoading) {
        // binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}
