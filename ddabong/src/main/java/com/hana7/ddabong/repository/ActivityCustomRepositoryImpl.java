package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.entity.QActivity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityCustomRepositoryImpl implements ActivityCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public ActivityCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Activity> findActivityByInstitution(Long institutionId, Pageable pageable, String keyWord) {
        QActivity qActivity = QActivity.activity;

        List<Activity> activities = jpaQueryFactory.selectFrom(qActivity)
                .where(
                        qActivity.title.containsIgnoreCase(keyWord),
                        qActivity.institution.id.eq(institutionId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(activities, pageable, activities.size());
    }
}
