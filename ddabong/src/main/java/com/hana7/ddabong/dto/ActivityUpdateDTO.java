package com.hana7.ddabong.dto;

import com.hana7.ddabong.enums.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityUpdateDTO {
    private Long id;
    private String title;
    private String content;
    private Category category;
}
