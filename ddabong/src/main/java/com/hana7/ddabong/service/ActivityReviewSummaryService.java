package com.hana7.ddabong.service;

import com.hana7.ddabong.entity.ActivityReview;
import com.hana7.ddabong.repository.ActivityReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityReviewSummaryService {

    private final ActivityReviewRepository activityReviewRepository;
    private final GeminiService geminiService;

    public String summarizeForActivity(Long activityId) {
        List<ActivityReview> reviews = activityReviewRepository.findByActivity_Id(activityId);
        if (reviews.isEmpty()) {
            return "아직 등록된 봉사 리뷰가 없습니다.";
        }

        List<String> lines = new ArrayList<>(reviews.size());
        for (ActivityReview r : reviews) {
            String line = String.format("평점:%d, 내용:%s", r.getRate(), r.getContent());
            lines.add(line);
        }

        String prompt = buildPrompt(lines);
        return geminiService.summarize(prompt);
    }

    private String buildPrompt(List<String> lines) {
        String joined = String.join("\n", lines);
        return """
               너는 봉사활동 리뷰 요약 전문가야. 아래 리뷰들을 바탕으로 활동의 장단점을 명확히 요약해줘.

               요구사항:
               - 전반적인 활동 분위기와 추천 대상을 두 문장 이내 요약
               - 이 모든걸 50자 이내로 간결하게 작성
               - 따뜻한 말투로 작성
               - 글자를 강조하기 위한 ** 같은 마크다운 문법 사용 금지

               리뷰:
               """ + joined;
    }
}
