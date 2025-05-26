package com.example.views.data.manager;


import static com.example.views.data.manager.CategorySearch.searchCategories;

import android.content.Context;
import android.widget.Toast;

// Search import
import com.example.views.data.api.*;
import com.example.views.data.repository.*;
import com.example.views.data.model.*;

import java.util.List;


public class SearchManager {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final float DEFAULT_MIN_RATING = 0f;
    private static final String DEFAULT_SORT_FIELD = "rating";
    private static final SortDirection DEFAULT_SORT_DIR = SortDirection.DESC;

    private final EntertainmentRepository repository;
    private final Context context;
    private final SearchResultListener listener;

    public interface SearchResultListener {
        void onSearchResults(List<EntertainmentMap> results);
        void onSearchAllResults(List<EntertainmentMap> results);
        void onSearchError(String error);
        void onLoading(boolean isLoading);
    }

    public SearchManager(Context context, SearchResultListener listener) {
        this.context = context;
        this.listener = listener;
        this.repository = new EntertainmentRepository(RetrofitClient.getApiService());
    }

    public void getAllPlaces() {
        listener.onLoading(true);

        repository.getAll(new EntertainmentRepository.ApiCallback<List<EntertainmentMap>>() {
            @Override
            public void onSuccess(List<EntertainmentMap> places) {
                listener.onLoading(false); // Загрузка завершена
                listener.onSearchAllResults(places); // Передаем результаты
            }

            @Override
            public void onError(String error) {
                listener.onLoading(false); // Загрузка завершена (с ошибкой)
                listener.onSearchError("Не удалось загрузить места: " + error); // Передаем ошибку
            }
        });
    }

    public void performSearch(String query) {
        listener.onLoading(true);

        List<Integer> categories = extractCategoriesFromQuery(query);
        String searchQuery = (categories == null || categories.isEmpty()) ? query : null;

        repository.search(
                searchQuery,
                categories,
                DEFAULT_MIN_RATING,
                DEFAULT_PAGE,
                DEFAULT_PAGE_SIZE,
                DEFAULT_SORT_FIELD,
                DEFAULT_SORT_DIR,
                new EntertainmentRepository.ApiCallback<ApiPageResponse<EntertainmentMap>>() {
                    @Override
                    public void onSuccess(ApiPageResponse<EntertainmentMap> page) {
                        listener.onLoading(false);
                        listener.onSearchResults(page.getContent());
                    }

                    @Override
                    public void onError(String error) {
                        listener.onLoading(false);
                        listener.onSearchError(error);
                    }
                }
        );
    }

    private List<Integer> extractCategoriesFromQuery(String query) {
        List<Integer> categories = searchCategories(query);
        return categories.isEmpty() ? null : categories;
    }

}