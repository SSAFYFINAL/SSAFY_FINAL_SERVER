package com.ssafy.pjtaserver.utils;

import java.util.*;

public class SimilarityCalculatorUtil {

    // 두 리스트의 코사인 유사도 계산
    public static double calculateCosineSimilarity(List<String> list1, List<String> list2) {
        Set<String> allItems = new HashSet<>();
        allItems.addAll(list1);
        allItems.addAll(list2);

        // 빈도 계산
        Map<String, Integer> freq1 = calculateFrequency(list1, allItems);
        Map<String, Integer> freq2 = calculateFrequency(list2, allItems);

        // 내적과 크기 계산
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (String item : allItems) {
            dotProduct += freq1.getOrDefault(item, 0) * freq2.getOrDefault(item, 0);
            magnitude1 += Math.pow(freq1.getOrDefault(item, 0), 2);
            magnitude2 += Math.pow(freq2.getOrDefault(item, 0), 2);
        }

        // 유사도 반환
        return (magnitude1 == 0 || magnitude2 == 0) ? 0.0 : (dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2)));
    }

    private static Map<String, Integer> calculateFrequency(List<String> list, Set<String> allItems) {
        Map<String, Integer> frequency = new HashMap<>();
        allItems.forEach(item -> frequency.put(item, 0));
        for (String item : list) {
            frequency.put(item, frequency.getOrDefault(item, 0) + 1);
        }
        return frequency;
    }
}