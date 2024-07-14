package com.essay.zaloapp.services;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.IOException;
import java.util.List;

public interface RecommendSystemService {
    List<RecommendedItem> recommendSystem(Long userId, Long age) throws IOException, TasteException;
}
