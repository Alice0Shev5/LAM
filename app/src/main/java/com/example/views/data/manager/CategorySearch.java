package com.example.views.data.manager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategorySearch {
    // Пример данных из таблицы (id -> название категории)
    private static final Map<Integer, String> CATEGORIES = new HashMap<Integer, String>() {{
        put(1, "Музеи");
        put(2, "Галереи искусств");
        put(3, "Театры");
        put(4, "Исторические места и памятники");
        put(5, "Концертные залы");
        put(6, "Культурные центры");
        put(7, "Бары");
        put(8, "Клубы");
        put(9, "Пабы");
        put(10, "Караоке");
        put(11, "Парки и сады");
        put(12, "Парки аттракционов");
        put(13, "Мастер-классы и студии");
        put(14, "Квесты");
    }};


    // Поиск категорий по запросу
    public static List<Integer> searchCategories(String query) {
        List<Integer> results = new ArrayList<>();
        // Заменяем пробелы на ".*", чтобы искать слова в любом порядке
        String pattern = ".*" + query.toLowerCase().replace(" ", ".*") + ".*";

        for (Map.Entry<Integer, String> entry : CATEGORIES.entrySet()) {
            if (entry.getValue().toLowerCase().matches(pattern)) {
                results.add(entry.getKey()); // Добавляем ID (ключ) вместо названия
            }
        }
        return results;
    }
}
