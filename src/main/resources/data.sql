INSERT INTO accessibility
SELECT *
FROM (SELECT 0, 'en') x
WHERE NOT EXISTS(SELECT * FROM accessibility);