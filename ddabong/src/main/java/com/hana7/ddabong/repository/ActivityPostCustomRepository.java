package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityPostCustomRepository {
    Page<ActivityPost> findAllActivityPostAndRecruitmentEndAfter(Pageable pageable, String preferredRegion, String searchRegion, List<Category> categories);
}
