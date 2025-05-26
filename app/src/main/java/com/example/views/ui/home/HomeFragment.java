package com.example.views.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.views.data.controller.SearchController;
import com.example.views.databinding.FragmentHomeBinding;

import com.example.views.map.manager.MapManager;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private MapManager mapManager;
    private SearchController searchController;

    private boolean isDataLoaded = false; // Флаг для отслеживания загрузки данных


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Помечаем, что данные нужно загрузить
        isDataLoaded = false;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // инициализация карты и поиска
        mapManager = new MapManager(requireContext(), binding.mapview);
        searchController = new SearchController(requireContext(), mapManager);

        // взаимодействие с поисковой строкой
        setupSearch();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Загружаем данные только при первом создании
        if (!isDataLoaded && searchController != null) {
            searchController.loadAllPlaces();
            isDataLoaded = true;
        }
    }


    private void setupSearch() {
        binding.searchInput.setVisibility(View.VISIBLE);
        binding.searchButton.setVisibility(View.VISIBLE);
        binding.searchButton.setOnClickListener(this::onSearchClicked);
    }
    private void onSearchClicked(View v) {
        String query = binding.searchInput.getText().toString().trim();
        if (searchController != null) {
            searchController.performSearch(query);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mapManager != null) mapManager.onStart();
    }

    @Override
    public void onStop() {
        if (mapManager != null) mapManager.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        mapManager = null;
        searchController = null;
        binding = null;
        super.onDestroyView();
    }
}