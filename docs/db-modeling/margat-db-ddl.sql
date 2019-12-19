-- 회원
DROP TABLE IF EXISTS members RESTRICT;

-- 포스트
DROP TABLE IF EXISTS posts RESTRICT;

-- 사진
DROP TABLE IF EXISTS photos RESTRICT;

-- 메시지
DROP TABLE IF EXISTS messages RESTRICT;

-- 코멘트
DROP TABLE IF EXISTS comments RESTRICT;

-- 좋아요
DROP TABLE IF EXISTS likes RESTRICT;

-- 회원
CREATE TABLE members (
  mem_no        INTEGER      NOT NULL, -- 회원번호
  name          VARCHAR(50)  NOT NULL, -- 이름
  pwd           VARCHAR(50)  NOT NULL, -- 비밀번호
  email         VARCHAR(40)  NOT NULL, -- 이메일
  tel           VARCHAR(30)  NOT NULL, -- 전화번호
  profile_photo VARCHAR(255) NULL,     -- 프로필사진
  follow        INTEGER      NULL     DEFAULT 0, -- 팔로우
  folling       INTEGER      NULL     DEFAULT 0, -- 팔로잉
  email_key     VARCHAR(255) NULL,     -- 인증키
  mem_state     INTEGER      NULL     DEFAULT 0, -- 회원상태
  mem_grade     INTEGER      NULL      -- 회원등급
);

-- 회원
ALTER TABLE members
  ADD CONSTRAINT PK_members -- 회원 기본키
    PRIMARY KEY (
      mem_no -- 회원번호
    );

-- 회원 유니크 인덱스
CREATE UNIQUE INDEX UIX_members
  ON members ( -- 회원
    tel ASC -- 전화번호
  );

ALTER TABLE members
  MODIFY COLUMN mem_no INTEGER NOT NULL AUTO_INCREMENT;

-- 포스트
CREATE TABLE posts (
  post_no   INTEGER  NOT NULL, -- 포스트번호
  mem_no    INTEGER  NOT NULL, -- 회원번호
  post_cont TEXT     NOT NULL, -- 내용
  post_cdt  DATETIME NOT NULL  -- 작성일
);

-- 포스트
ALTER TABLE posts
  ADD CONSTRAINT PK_posts -- 포스트 기본키
    PRIMARY KEY (
      post_no -- 포스트번호
    );

ALTER TABLE posts
  MODIFY COLUMN post_no INTEGER NOT NULL AUTO_INCREMENT;

-- 사진
CREATE TABLE photos (
  photo_no   INTEGER      NOT NULL, -- 사진번호
  post_no    INTEGER      NULL,     -- 포스트번호
  message_no INTEGER      NULL,     -- 메시지번호
  photo_name VARCHAR(255) NOT NULL  -- 사진이름
);

-- 사진
ALTER TABLE photos
  ADD CONSTRAINT PK_photos -- 사진 기본키
    PRIMARY KEY (
      photo_no -- 사진번호
    );

ALTER TABLE photos
  MODIFY COLUMN photo_no INTEGER NOT NULL AUTO_INCREMENT;

-- 메시지
CREATE TABLE messages (
  message_no   INTEGER    NOT NULL, -- 메시지번호
  sender       INTEGER    NOT NULL, -- 보낸사람
  receiver     INTEGER    NOT NULL, -- 받는사람
  message_cont TEXT       NOT NULL, -- 내용
  is_read      TINYINT(1) NOT NULL DEFAULT false -- 수신여부
);

-- 메시지
ALTER TABLE messages
  ADD CONSTRAINT PK_messages -- 메시지 기본키
    PRIMARY KEY (
      message_no -- 메시지번호
    );

ALTER TABLE messages
  MODIFY COLUMN message_no INTEGER NOT NULL AUTO_INCREMENT;

-- 코멘트
CREATE TABLE comments (
  comment_no   INTEGER  NOT NULL, -- 코멘트번호
  post_no      INTEGER  NOT NULL, -- 포스트번호
  comment_no2  INTEGER  NULL,     -- 코멘트번호2
  comment_cont TEXT     NOT NULL, -- 내용
  comment_cdt  DATETIME NOT NULL  -- 작성일
);

-- 코멘트
ALTER TABLE comments
  ADD CONSTRAINT PK_comments -- 코멘트 기본키
    PRIMARY KEY (
      comment_no -- 코멘트번호
    );

ALTER TABLE comments
  MODIFY COLUMN comment_no INTEGER NOT NULL AUTO_INCREMENT;

-- 좋아요
CREATE TABLE likes (
  like_no     INTEGER NOT NULL, -- 좋아요번호
  like_mem_no INTEGER NOT NULL, -- 좋아요회원번호
  post_no     INTEGER NULL,     -- 포스트번호
  comment_no  INTEGER NULL      -- 코멘트번호
);

-- 좋아요
ALTER TABLE likes
  ADD CONSTRAINT PK_likes -- 좋아요 기본키
    PRIMARY KEY (
      like_no -- 좋아요번호
    );

ALTER TABLE likes
  MODIFY COLUMN like_no INTEGER NOT NULL AUTO_INCREMENT;

-- 포스트
ALTER TABLE posts
  ADD CONSTRAINT FK_members_TO_posts -- 회원 -> 포스트
    FOREIGN KEY (
      mem_no -- 회원번호
    )
    REFERENCES members ( -- 회원
      mem_no -- 회원번호
    );

-- 사진
ALTER TABLE photos
  ADD CONSTRAINT FK_posts_TO_photos -- 포스트 -> 사진
    FOREIGN KEY (
      post_no -- 포스트번호
    )
    REFERENCES posts ( -- 포스트
      post_no -- 포스트번호
    );

-- 사진
ALTER TABLE photos
  ADD CONSTRAINT FK_messages_TO_photos -- 메시지 -> 사진
    FOREIGN KEY (
      message_no -- 메시지번호
    )
    REFERENCES messages ( -- 메시지
      message_no -- 메시지번호
    );

-- 메시지
ALTER TABLE messages
  ADD CONSTRAINT FK_members_TO_messages -- 회원 -> 메시지
    FOREIGN KEY (
      sender -- 보낸사람
    )
    REFERENCES members ( -- 회원
      mem_no -- 회원번호
    );

-- 메시지
ALTER TABLE messages
  ADD CONSTRAINT FK_members_TO_messages2 -- 회원 -> 메시지2
    FOREIGN KEY (
      receiver -- 받는사람
    )
    REFERENCES members ( -- 회원
      mem_no -- 회원번호
    );

-- 코멘트
ALTER TABLE comments
  ADD CONSTRAINT FK_posts_TO_comments -- 포스트 -> 코멘트
    FOREIGN KEY (
      post_no -- 포스트번호
    )
    REFERENCES posts ( -- 포스트
      post_no -- 포스트번호
    );

-- 코멘트
ALTER TABLE comments
  ADD CONSTRAINT FK_comments_TO_comments -- 코멘트 -> 코멘트
    FOREIGN KEY (
      comment_no2 -- 코멘트번호2
    )
    REFERENCES comments ( -- 코멘트
      comment_no -- 코멘트번호
    );

-- 좋아요
ALTER TABLE likes
  ADD CONSTRAINT FK_posts_TO_likes -- 포스트 -> 좋아요
    FOREIGN KEY (
      post_no -- 포스트번호
    )
    REFERENCES posts ( -- 포스트
      post_no -- 포스트번호
    );

-- 좋아요
ALTER TABLE likes
  ADD CONSTRAINT FK_comments_TO_likes -- 코멘트 -> 좋아요
    FOREIGN KEY (
      comment_no -- 코멘트번호
    )
    REFERENCES comments ( -- 코멘트
      comment_no -- 코멘트번호
    );

-- 좋아요
ALTER TABLE likes
  ADD CONSTRAINT FK_members_TO_likes -- 회원 -> 좋아요
    FOREIGN KEY (
      like_mem_no -- 좋아요회원번호
    )
    REFERENCES members ( -- 회원
      mem_no -- 회원번호
    );