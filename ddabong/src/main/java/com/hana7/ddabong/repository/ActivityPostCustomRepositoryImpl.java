package com.hana7.ddabong.repository;

import com.hana7.ddabong.entity.Activity;
import com.hana7.ddabong.entity.ActivityPost;
import com.hana7.ddabong.entity.QActivity;
import com.hana7.ddabong.entity.QActivityPost;
import com.hana7.ddabong.enums.Category;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Repository
public class ActivityPostCustomRepositoryImpl implements ActivityPostCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public ActivityPostCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ActivityPost> findAllActivityPost(Pageable pageable, String preferredRegion, String searchRegion, List<Category> categories) {
        QActivityPost qActivityPost = QActivityPost.activityPost;
        QActivity qActivity = QActivity.activity;

        BooleanExpression regionFilter = hasText(searchRegion) ? qActivityPost.location.contains(searchRegion) : null;
        BooleanExpression categoryFilter = (categories != null && !categories.isEmpty()) ? qActivity.category.in(categories) : null;

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        if (!hasText(searchRegion) && hasText(preferredRegion)) {
            OrderSpecifier<Integer> preferFirst =
                    new CaseBuilder()
                            .when(qActivityPost.location.contains(preferredRegion)).then(1)
                            .otherwise(0)
                            .desc();
            orderSpecifiers.add(preferFirst);
        }
        // 최신순
        orderSpecifiers.add(qActivityPost.id.desc());

        List<ActivityPost> activityPosts = jpaQueryFactory.selectFrom(qActivityPost)
                .join(qActivityPost.activity, qActivity)
                .where(regionFilter, categoryFilter)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(activityPosts, pageable, activityPosts.size());
    }
}
