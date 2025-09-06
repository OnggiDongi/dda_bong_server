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

    @Value("${summary.chunk-size:20}")
    private int chunkSize;

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

        List<String> chunkSummaries = new ArrayList<>();
        for (int i = 0; i < lines.size(); i += chunkSize) {
            List<String> chunk = lines.subList(i, Math.min(i + chunkSize, lines.size()));
            String prompt = buildChunkPrompt(chunk);
            String chunkResult = geminiService.summarize(prompt);
            chunkSummaries.add(chunkResult);
        }

        String finalPrompt = buildMetaPrompt(chunkSummaries);
        return geminiService.summarize(finalPrompt);
    }

    private String buildChunkPrompt(List<String> chunk) {
        String joined = String.join("\n", chunk);
        return """
               너는 봉사활동 리뷰 요약 전문가야. 아래 리뷰들을 바탕으로 활동의 장단점을 명확히 요약해줘.

               요구사항:
               - 긍정적 평가와 부정적 평가를 구분해서 요약
               - 핵심적인 칭찬과 불만사항을 각 3개씩 불릿 포인트로 정리
               - 전반적인 활동 분위기와 추천 대상을 한 문장으로 요약

               리뷰:
               """ + joined;
    }

    private String buildMetaPrompt(List<String> chunkSummaries) {
        String joined = String.join("\n---\n", chunkSummaries);
        return """
               다음은 여러 봉사활동 리뷰 청크 요약이야. 이걸 최종적으로 하나로 통합해서 간결하게 만들어줘.

               요구사항:
               - 전체적인 긍정/부정 평가 경향 통합
               - 중복되는 칭찬/불만사항은 합치고, 가장 중요한 것들 위주로 정리
               - 최종적인 활동 분위기와 추천 대상 요약해서 간결하게 두문장 이내로

               1차 요약들:
               """ + joined;
    }
}
