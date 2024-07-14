package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.models.Preference;
import com.essay.zaloapp.repository.PreferenceRepository;
import com.essay.zaloapp.services.RecommendSystemService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RecommendSystemServiceImpl implements RecommendSystemService {

    @Autowired
    private PreferenceRepository preferenceRepository;

    public DataModel getDataModel(Long age) {
        Long minAge = (age / 20) *20;
        Long maxAge = minAge + 20;
        List<Preference> preferences = preferenceRepository.findByAgeUserAfterAndAgeUserBefore(minAge, maxAge);
        FastByIDMap<PreferenceArray> preferenceMap = new FastByIDMap<>();

        Map<Long, List<Preference>> groupedPreferences = preferences.stream()
                .collect(Collectors.groupingBy(Preference::getUserID));

        for (Map.Entry<Long, List<Preference>> entry : groupedPreferences.entrySet()) {
            List<Preference> userPreferences = entry.getValue();
            PreferenceArray prefsForUser = new GenericUserPreferenceArray(userPreferences.size());
            for (int i = 0; i < userPreferences.size(); i++) {
                Preference pref = userPreferences.get(i);
                prefsForUser.set(i, new GenericPreference(pref.getUserID(), pref.getPostID(), pref.getPreference().floatValue()));
            }
            preferenceMap.put(entry.getKey(), prefsForUser);
        }

        return new GenericDataModel(preferenceMap);
    }

    @Override
    public List<RecommendedItem> recommendSystem(Long userId, Long age) throws IOException, TasteException {
        try {
            DataModel model = getDataModel(age);
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

            int numberUser = model.getNumUsers() > 0 ? model.getNumUsers() : 1;
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(numberUser, similarity, model);

            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // Lấy tất cả các mục (items) trong mô hình dữ liệu
            List<RecommendedItem> allRecommendations = new ArrayList<>();
            var itemIDs = model.getItemIDs();

            // Tính toán độ ưa thích của người dùng đối với tất cả các mục
            for (LongPrimitiveIterator it = itemIDs; it.hasNext(); ) {
                long itemId = it.next();
                float preference = recommender.estimatePreference(userId, itemId);
                if (!Float.isNaN(preference)) {
                    allRecommendations.add(new RecommendedItem() {
                        @Override
                        public long getItemID() {
                            return itemId;
                        }

                        @Override
                        public float getValue() {
                            return preference;
                        }

                        @Override
                        public String toString() {
                            return "RecommendedItem[itemID=" + itemId + ", value=" + preference + "]";
                        }
                    });
                }
            }
            return allRecommendations;
        } catch (Exception e){
            return new ArrayList<>();
        }
    }
}
