package com.hana7.ddabong.dto.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiResponse(List<Candidate> candidates) {

    public Optional<String> getFirstCandidateText() {
        return Optional.ofNullable(candidates)
                .filter(c -> !c.isEmpty())
                .map(c -> c.get(0))
                .map(Candidate::content)
                .map(Content::parts)
                .filter(p -> !p.isEmpty())
                .map(p -> p.get(0))
                .map(Part::text);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Candidate(Content content) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Content(List<Part> parts) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Part(String text) {}
}
