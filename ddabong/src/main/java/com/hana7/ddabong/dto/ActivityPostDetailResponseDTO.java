package com.hana7.ddabong.dto;

import com.hana7.ddabong.entity.*;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class ActivityPostDetailResponseDTO {

	private Long id;

	private Long activityId;

	private String title;

	private String content;

	private String dDay;

	private String startDate;

	private String recruitmentEndDate;

	private int time;

	private String category;

	private String institutionName;

	private String institutionPhoneNumber;

	private int capacity;

	private String location;

	private String imageUrl;

	private double totalAvgScore;

	private List<ActivityReviewResponseDTO> reviews;

	private List<String> supports;

}
