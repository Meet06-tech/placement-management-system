-- Seed branches as per DB_Design.pdf examples
-- This runs after Hibernate creates the schema (spring.jpa.defer-datasource-initialization=true needed)

INSERT INTO branches (code, name) VALUES
    ('CSE',  'Computer Science & Engineering'),
    ('IT',   'Information Technology'),
    ('ECE',  'Electronics & Communication Engineering'),
    ('ME',   'Mechanical Engineering'),
    ('CE',   'Civil Engineering'),
    ('MCA',  'Master of Computer Applications')
ON CONFLICT (code) DO NOTHING;

-- Seed skills
INSERT INTO skills (name) VALUES
    ('Java'),
    ('Python'),
    ('C++'),
    ('SQL'),
    ('Spring Boot'),
    ('Flutter'),
    ('React'),
    ('Git'),
    ('JavaScript'),
    ('TypeScript'),
    ('Node.js'),
    ('Docker')
ON CONFLICT (name) DO NOTHING;
