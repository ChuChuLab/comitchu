INSERT INTO user (created_at, github_id, updated_at, github_username, avatar_url)
VALUES (NOW(), 123123, NOW(), 'ramge132', 'https://test.com/u/000000000?v=0');

INSERT INTO language (lang)
VALUES ('comit'), ('Java'), ('Python'), ('JavaScript'), ('TypeScript'), ('C'), ('C++'), ('C#'), ('Swift'), ('Go');

INSERT INTO chu (created_at, exp, user_id, name, level, status, lang, background, last_status_updated_at)
VALUES (NOW(), 0, 1, '커밋츄', 15, 'NORMAL', 'comit', 'flower', NOW());