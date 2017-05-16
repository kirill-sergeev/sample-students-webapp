create or replace function student_avg_mark_by_discipline(integer, integer) returns numeric
LANGUAGE SQL
AS $$
SELECT avg(m.mark)
    FROM users u, disciplines d, courses c, lessons l, marks m, groups g
    WHERE u.group_id = g.id AND g.id = c.group_id AND
          c.discipline_id = d.id AND c.id = l.course_id AND
          l.id = m.lesson_id AND m.user_id = u.id AND u.id = $1 AND
          d.id = $2
    GROUP BY u.id, d.id;
$$;
