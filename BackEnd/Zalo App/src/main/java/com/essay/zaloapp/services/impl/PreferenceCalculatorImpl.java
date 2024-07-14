package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.models.Preference;
import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.repository.PreferenceRepository;
import com.essay.zaloapp.services.PreferenceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PreferenceCalculatorImpl implements PreferenceCalculator {

    @Autowired
    private PreferenceRepository preferenceRepository;

    private static final Double wLike = 0.1;
    private static final Double wComment = 0.3;
    private static final Double wShare = 0.6;

    private Double calculatePreference(int isLike, int numComment, int numShare) {
        int maxComments = 5;
        int maxShares = 3;

        double numLikes = isLike;
        numComment = Math.min(numComment, maxComments);
        numShare = Math.min(numShare,maxShares);

        double preference = wLike * numLikes +
                wComment * numComment +
                wShare * numShare;

        return preference;
    }

    // Các giá trị của isLike, numComment, numShare : -1, 0, 1
    // -1 : giảm giá trị đi 1,
    //  0 : không thay đổi giá trị
    //  1 : tăng giá trị lên 1
    @Override
    public void updatePreferenceUser(User user, Long postID, Long numLike, Long numComment, Long numShare) throws Exception {
        try {
            Preference preference = preferenceRepository.findFirstByUserIDAndPostID(user.getId(), postID);
            if (preference == null) {
                Long age = (new Date().getTime() - user.getBirthDay().getTime()) / (1000l * 60 * 60 * 24 * 365);
                preference = Preference.builder()
                        .userID(user.getId())
                        .postID(postID)
                        .ageUser(age)
                        .numLike(0L)
                        .numComment(0L)
                        .numShare(0L)
                        .build();
            }
            preference.setNumLike(preference.getNumLike() + numLike);
            preference.setNumComment(preference.getNumComment() + numComment);
            preference.setNumShare(preference.getNumShare() + numShare);
            preference.setPreference(calculatePreference(preference.getNumLike().intValue(),preference.getNumComment().intValue(),preference.getNumShare().intValue()));
            preferenceRepository.save(preference);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
