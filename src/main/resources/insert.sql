CREATE OR REPLACE FUNCTION student_avg_mark_by_discipline(INTEGER, INTEGER, OUT NUMERIC)
  RETURNS NUMERIC AS
'SELECT avg(mark)
 FROM users u, disciplines d, courses c, lessons l, marks m, groups g
 WHERE m.user_id = u.id
       AND u.group_id = g.id
       AND g.id = c.group_id
       AND c.id = l.course_id
       AND l.id = m.lesson_id
       AND c.discipline_id = d.id
       AND u.id = $1
       AND d.id = $2
 GROUP BY u.id, d.id;'
LANGUAGE SQL
IMMUTABLE
RETURNS NULL ON NULL INPUT;