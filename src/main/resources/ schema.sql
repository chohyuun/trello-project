-- 전문 검색을 위한 FULLTEXT 인덱스 생성
ALTER TABLE card ADD FULLTEXT INDEX idx_card_content (title, description);

-- 복합 인덱스 생성 (주요 검색 조건들을 포함)
ALTER TABLE card ADD INDEX idx_card_search (list_id, board_id, due_date, id);

-- 멤버 검색을 위한 인덱스
ALTER TABLE card ADD INDEX idx_card_member (member_id);
