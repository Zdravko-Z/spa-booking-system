-- Passwords: admin=admin123, zdravko=zdravkoTest, maria=Guest1, elena=staff1 ivan=staff2

INSERT INTO users (id, username, email, password, role) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'admin',   'admin@serenity.com',  '$2a$12$eognF6Im4YqPHBSYAWnZpeioUoLAjYG8ssFQD/5wfCGZQmq9OjS8y', 'ADMIN'),
('6ba7b810-9dad-41d1-80b4-00c04fd430c8', 'zdravko', 'zdravko@mail.com',    '$2a$12$qEfHuCvbzk2JvitWoRdcB.yw0g4YvPk0G7KNCUW3W.GRgu7Le7Hjy', 'CLIENT'),
('6ba7b811-9dad-41d1-80b4-00c04fd430c9', 'maria',   'maria@mail.com',      '$2a$12$AAPsV4G7UwEgQ6wZQIDR1OANyIjvt9OHiKou4vXA3s5c6SWFwHXHC', 'CLIENT'),
('6ba7b812-9dad-41d1-80b4-00c04fd430ca', 'elena',   'staff1@serenity.com', '$2a$12$.byMy1i51okQC9f8.k7KQuL.3.4/cIzBXFydbmYQMkL/Na4J/Wtk2', 'STAFF'),
('6ba7b813-9dad-41d1-80b4-00c04fd430cb', 'ivan',    'staff2@serenity.com', '$2a$12$JyNwfJ/8JFYO/zJPr18fAOgnoPoLnFl69/JEq5jyZrQbJGwFWwdUa', 'STAFF');

SET @admin_id   = (SELECT id FROM users WHERE username = 'admin');
SET @zdravko_id = (SELECT id FROM users WHERE username = 'zdravko');
SET @maria_id   = (SELECT id FROM users WHERE username = 'maria');
SET @elena_id   = (SELECT id FROM users WHERE username = 'elena');
SET @ivan_id    = (SELECT id FROM users WHERE username = 'ivan');

-- Guests
INSERT INTO guests (id, user_id, first_name, last_name, phone) VALUES
('7c9e6679-7425-40de-944b-e07fc1f90ae7', @zdravko_id, 'Zdravko', 'Ivanov',    '0888111222'),
('7c9e6679-7425-40de-944b-e07fc1f90ae8', @maria_id,   'Maria',   'Georgieva', '0888333444');

SET @zdravko_guest_id = (SELECT id FROM guests WHERE user_id = @zdravko_id);
SET @maria_guest_id   = (SELECT id FROM guests WHERE user_id = @maria_id);

-- Spa staff
INSERT INTO spa_staff (id, user_id, first_name, last_name, specialization) VALUES
('8f3b4e5a-6b7c-4d8e-9f0a-1b2c3d4e5f6a', @elena_id, 'Elena', 'Todorova', 'Deep Tissue & Hot Stone'),
('8f3b4e5a-6b7c-4d8e-9f0a-1b2c3d4e5f6b', @ivan_id,  'Ivan',  'Petrov',   'Aromatherapy & Reflexology');

SET @elena_staff_id = (SELECT id FROM spa_staff WHERE user_id = @elena_id);
SET @ivan_staff_id  = (SELECT id FROM spa_staff WHERE user_id = @ivan_id);

-- Spa rooms
INSERT INTO spa_rooms (id, room_number, name, capacity, status) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'R01', 'The Lotus Room',   1, 'AVAILABLE'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'R02', 'The Stone Suite',  1, 'AVAILABLE'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'R03', 'The Zen Garden',   2, 'AVAILABLE'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'R04', 'The Harmony Room', 1, 'MAINTENANCE');

SET @lotus_id = (SELECT id FROM spa_rooms WHERE room_number = 'R01');
SET @stone_id = (SELECT id FROM spa_rooms WHERE room_number = 'R02');

-- Spa treatments
INSERT INTO spa_treatments (id, name, description, duration_minutes, price, deleted) VALUES
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d5e', 'Deep Tissue Massage', 'Targets deep layers of muscle to relieve chronic tension and pain.', 60, 65.00, FALSE),
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d5f', 'Hot Stone Therapy',   'Warm volcanic stones melt away tension and ease stiff muscles.',     90, 85.00, FALSE),
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d60', 'Aromatherapy',        'Essential oils blended with a relaxing full body massage.',           60, 60.00, FALSE),
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d61', 'Reflexology',         'Targeted pressure on foot points to restore natural energy flow.',    45, 50.00, FALSE),
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d62', 'Swedish Massage',     'Classic full body relaxation with long flowing strokes.',             60, 55.00, FALSE),
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d63', 'Facial Treatment',    'Deep cleansing and hydrating facial for all skin types.',             45, 55.00, FALSE);

SET @deep_tissue_id = (SELECT id FROM spa_treatments WHERE name = 'Deep Tissue Massage');
SET @hot_stone_id   = (SELECT id FROM spa_treatments WHERE name = 'Hot Stone Therapy');
SET @aroma_id       = (SELECT id FROM spa_treatments WHERE name = 'Aromatherapy');
SET @reflex_id      = (SELECT id FROM spa_treatments WHERE name = 'Reflexology');

INSERT INTO spa_bookings (
    id, confirmation_code, guest_id, spa_room_id, spa_staff_id,
    booking_date, start_time, end_time, duration_minutes,
    total_price, status, notes, added_at, updated_at
) VALUES
('c1d2e3f4-5a6b-4c7d-8e9f-0a1b2c3d4e5f', 'ZDR00001', @zdravko_guest_id, @lotus_id, @elena_staff_id,
 '2026-07-10', '10:00:00', '11:30:00', 90, 85.00, 'PENDING',
 'Please use extra warm stones', NOW(), NOW()),

('c1d2e3f4-5a6b-4c7d-8e9f-0a1b2c3d4e60', 'ZDR00002', @zdravko_guest_id, @stone_id, @ivan_staff_id,
 '2026-07-05', '14:00:00', '15:00:00', 60, 65.00, 'CANCELLED',
 '2026-07-09', NOW(), NOW()),

('c1d2e3f4-5a6b-4c7d-8e9f-0a1b2c3d4e61', 'MAR00001', @maria_guest_id, @stone_id, @ivan_staff_id,
 '2026-07-15', '09:00:00', '10:45:00', 105, 115.00, 'PENDING',
 '2026-06-23', NOW(), NOW());

SET @zdrav_booking1 = (SELECT id FROM spa_bookings WHERE confirmation_code = 'ZDR00001');
SET @zdrav_booking2 = (SELECT id FROM spa_bookings WHERE confirmation_code = 'ZDR00002');
SET @maria_booking1 = (SELECT id FROM spa_bookings WHERE confirmation_code = 'MAR00001');

INSERT INTO spa_booking_treatments (id, booking_id, treatment_id, price_at_booking) VALUES
('d1e2f3a4-5b6c-4d7e-8f9a-0b1c2d3e4f5a', @zdrav_booking1, @hot_stone_id,   85.00),
('d1e2f3a4-5b6c-4d7e-8f9a-0b1c2d3e4f5b', @zdrav_booking2, @deep_tissue_id, 65.00),
('d1e2f3a4-5b6c-4d7e-8f9a-0b1c2d3e4f5c', @maria_booking1, @aroma_id,       60.00),
('d1e2f3a4-5b6c-4d7e-8f9a-0b1c2d3e4f5d', @maria_booking1, @reflex_id,      55.00);