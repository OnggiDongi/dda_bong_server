-- Institution
INSERT INTO institution (name, email, password, phone_number, detail) VALUES
( '하나금융', 'hana@hana.com', '1234', '010-1111-1111', '하나금융그룹입니다.'),
( '네이버', 'naver@naver.com', '1234', '010-2222-2222', '네이버입니다.'),
( '카카오', 'kakao@kakao.com', '1234', '010-5555-5555', '카카오입니다.'),
( '삼성', 'ㅌsamsung@samsung.com', '1234', '010-6666-6666', '삼성입니다.'),
( '개미인력', 'lg@lg.com', '1234', '010-7777-7777', '안녕하세요. 개미인력입니다.');

-- User
INSERT INTO user (name, email, password, phone_number, total_hour, birthdate, is_kakao, preferred_region) VALUES
( '이지민', 'dlwlals1289@gmail.com', '$2a$10$EYqOBzgUpHpkeXn8GK00pe9SlVnL.x9enonzA3aPxTpIQSspazde2', '010-5299-4149', 10, '2000-06-27', false, '경기도 파주시'),
( '김봉사', 'kim@ddabong.com', '1234', '010-3333-3333', 10, '1990-01-01', false, '서울'),
( '이봉사', 'lee@ddabong.com', '1234', '010-4444-4444', 20, '1995-05-05', true, '경기'),
( '박봉사', 'park@ddabong.com', '1234', '010-8888-8888', 5, '2000-02-10', false, '인천'),
( '최봉사', 'choi@ddabong.com', '1234', '010-9999-9999', 30, '1988-11-20', true, '부산'),
( '정봉사', 'jung@ddabong.com', '1234', '010-1010-1010', 15, '1998-07-07', false, '대구'),
( '강봉사', 'kang@ddabong.com', '1234', '010-2020-2020', 25, '1993-03-15', true, '광주'),
( '조봉사', 'cho@ddabong.com', '1234', '010-3030-3030', 8, '2001-09-01', false, '대전'),
( '윤봉사', 'yoon@ddabong.com', '1234', '010-4040-4040', 12, '1996-12-25', true, '울산'),
( '장봉사', 'jang@ddabong.com', '1234', '010-5050-5050', 18, '1991-06-30', false, '세종'),
( '임봉사', 'lim@ddabong.com', '1234', '010-6060-6060', 22, '1997-08-18', true, '제주');

-- User Preferred Category
INSERT INTO user_preferred_category (user_id, preferred_category) VALUES
(1, 'LIVING'), (1, 'EDUCATION'), (2, 'SAFETY'), (3, 'CULTURE'), (4, 'ENVIRONMENT'),
(5, 'PUBLIC'), (6, 'GLOBAL'), (7, 'LIVING'), (8, 'EDUCATION'), (9, 'SAFETY'), (10, 'CULTURE');

-- Activity
INSERT INTO activity (title, content, category, institution) VALUES
( '독거노인 생활 지원', '독거노인 분들의 양치를 시켜드려요.', 'LIVING', 1),
( '청소년 코딩 교육', '청소년들에게 코딩을 가르칩니다.', 'EDUCATION', 2),
( '해변 정화 활동', '해변의 쓰레기를 줍습니다.', 'ENVIRONMENT', 3),
( '어린이 안전 교육', '어린이들에게 안전 교육을 실시합니다.', 'SAFETY', 4),
( '다문화 가정 지원', '다문화 가정을 지원합니다.', 'CULTURE', 5);

-- ActivityPost
INSERT INTO activity_post (title, content, start_at, end_at, recruitment_start, recruitment_end, is_accept, capacity, location, image_url, activity_id) VALUES
( '독거노인 방문 봉사', '독거노인 분들을 방문하여 악어새가 되어드립니다.', '2025-09-01 10:00:00', '2025-09-01 12:00:00', '2025-08-20 09:00:00', '2025-08-31 18:00:00', true, 10, '서울시 강남구', 'http://example.com/image1.jpg', 1),
( '코딩 멘토링', '청소년들에게 코딩 멘토링을 제공합니다.', '2025-09-10 14:00:00', '2025-09-10 16:00:00', '2025-08-25 09:00:00', '2025-09-05 18:00:00', true, 5, '경기도 성남시', 'http://example.com/image2.jpg', 2),
( '제주 해변 플로깅', '제주 해변에서 플로깅을 진행합니다.', '2025-09-15 09:00:00', '2025-09-15 11:00:00', '2025-09-01 09:00:00', '2025-09-10 18:00:00', true, 20, '제주시 한림읍', 'http://example.com/image3.jpg', 3),
( '초등학교 교통 안전 캠페인', '초등학생들의 등하굣길 교통 안전을 위한 캠페인을 진행합니다.', '2025-09-20 08:00:00', '2025-09-20 10:00:00', '2025-09-05 09:00:00', '2025-09-15 18:00:00', true, 15, '부산시 해운대구', 'http://example.com/image4.jpg', 4),
( '외국인 주민 한국 문화 체험', '외국인 주민들에게 한국 문화를 소개하고 함께 체험합니다.', '2025-09-25 13:00:00', '2025-09-25 16:00:00', '2025-09-10 09:00:00', '2025-09-20 18:00:00', true, 12, '서울시 용산구', 'http://example.com/image5.jpg', 5);

-- Applicant
INSERT INTO applicant ( hours, status, user_id, activity_post_id) VALUES
( 2.00, 'APPROVED', 1, 1), ( 2.00, 'PENDING', 2, 1), ( 2.00, 'APPROVED', 2, 2),
( 2.00, 'APPROVED', 3, 3), ( 2.00, 'REJECTED', 4, 3), ( 2.00, 'APPROVED', 5, 4),
( 3.00, 'PENDING', 6, 5), ( 3.00, 'APPROVED', 7, 5), ( 2.00, 'APPROVED', 8, 1),
(2.00, 'PENDING', 9, 2), (2.00, 'APPROVED', 10, 3);

-- Likes
INSERT INTO likes ( user_id, activity_post_id) VALUES
( 1, 2), ( 2, 1), ( 3, 4), ( 4, 5), ( 5, 3),
( 6, 1), ( 7, 2), ( 8, 5), ( 9, 4), ( 10, 1);

-- ActivityReview
INSERT INTO activity_review ( rate, content, image_url, activity_id, user_id) VALUES
( 5, '정말 보람있는 시간이었습니다.', 'http://example.com/review1.jpg', 1, 1),
( 4, '아이들이 똑똑해서 가르치는 재미가 있었어요.', 'http://example.com/review2.jpg', 2, 2),
( 5, '제주 바다가 깨끗해져서 뿌듯합니다.', 'http://example.com/review3.jpg', 3, 3),
( 4, '아이들이 즐거워하는 모습을 보니 좋았습니다.', 'http://example.com/review4.jpg', 4, 5),
( 5, '다양한 나라의 친구들을 만날 수 있어서 유익했습니다.', 'http://example.com/review5.jpg', 5, 7);

-- UserReview
INSERT INTO user_review ( health_status, diligence_level, attitude, memo, user_id) VALUES
( 5, 5, 5, '성실하고 착한 봉사자입니다.', 1), ( 4, 4, 4, '열정적인 모습이 보기 좋았습니다.', 2),
( 5, 4, 5, '시간 약속을 잘 지킵니다.', 3), ( 3, 4, 3, '조금 더 적극적으로 참여했으면 좋겠습니다.', 4),
( 5, 5, 5, '항상 웃는 얼굴로 봉사에 임합니다.', 5), ( 4, 5, 4, '책임감이 강하고 꼼꼼합니다.', 6),
( 5, 4, 5, '친화력이 좋아 다른 봉사자들과 잘 어울립니다.', 7), ( 4, 4, 4, '묵묵히 자기 역할을 다합니다.', 8),
( 5, 5, 5, '솔선수범하는 모습이 인상적입니다.', 9), (4, 4, 5, '긍정적인 에너지가 넘칩니다.', 10);

-- Certification
INSERT INTO certification ( hour, user_id) VALUES
( 10, 1), ( 20, 2), ( 5, 3), ( 30, 4), ( 15, 5),
( 25, 6), ( 8, 7), ( 12, 8), ( 18, 9), (22, 10);

-- SupportRequest
INSERT INTO support_request ( supply, detail, status, activity_post_id) VALUES
( '간식', '어르신들께 드릴 간식이 필요합니다.', 'APPROVED', 1),
( '노트북', '교육용 노트북이 부족합니다.', 'PENDING', 2),
( '쓰레기 봉투', '해변 정화 활동에 사용할 대용량 쓰레기 봉투가 필요합니다.', 'APPROVED', 3),
( '안전 조끼', '아이들의 눈에 잘 띄는 안전 조끼가 필요합니다.', 'REJECTED', 4),
( '한복', '외국인 주민들이 입어볼 한복이 필요합니다.', 'PENDING', 5);
