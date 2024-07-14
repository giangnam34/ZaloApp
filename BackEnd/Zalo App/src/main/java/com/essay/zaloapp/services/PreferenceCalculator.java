package com.essay.zaloapp.services;

import com.essay.zaloapp.domain.models.User;

public interface PreferenceCalculator {
    // Các giá trị của isLike, numComment, numShare : -1, 0, 1
    // -1 : giảm giá trị đi 1,
    //  0 : không thay đổi giá trị
    //  1 : tăng giá trị lên 1
    void updatePreferenceUser(User user, Long postID, Long numLike, Long numComment, Long numShare) throws Exception;
}
