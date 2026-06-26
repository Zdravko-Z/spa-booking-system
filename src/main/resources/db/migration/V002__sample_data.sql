-- Passwords: admin=admin123, zdravko=zdravkoTest, maria=Guest1, elena=staff1 ivan=staff2

INSERT INTO users (id, username, email, password, role) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'admin',   'admin@serenity.com',  '$2a$12$eognF6Im4YqPHBSYAWnZpeioUoLAjYG8ssFQD/5wfCGZQmq9OjS8y', 'ADMIN'),
('6ba7b810-9dad-41d1-80b4-00c04fd430c8', 'zdravko', 'zdravko@mail.com',    '$2a$12$qEfHuCvbzk2JvitWoRdcB.yw0g4YvPk0G7KNCUW3W.GRgu7Le7Hjy', 'CLIENT'),
('6ba7b811-9dad-41d1-80b4-00c04fd430c9', 'maria',   'maria@mail.com',      '$2a$12$AAPsV4G7UwEgQ6wZQIDR1OANyIjvt9OHiKou4vXA3s5c6SWFwHXHC', 'CLIENT'),
('6ba7b812-9dad-41d1-80b4-00c04fd430ca', 'elena',   'staff1@serenity.com', '$2a$12$.byMy1i51okQC9f8.k7KQuL.3.4/cIzBXFydbmYQMkL/Na4J/Wtk2', 'STAFF'),
('6ba7b813-9dad-41d1-80b4-00c04fd430cb', 'ivan',    'staff2@serenity.com', '$2a$12$JyNwfJ/8JFYO/zJPr18fAOgnoPoLnFl69/JEq5jyZrQbJGwFWwdUa', 'STAFF');

-- Guests (user_id references the hardcoded UUIDs from above)
INSERT INTO guests (id, user_id, first_name, last_name, phone) VALUES
('7c9e6679-7425-40de-944b-e07fc1f90ae7', '6ba7b810-9dad-41d1-80b4-00c04fd430c8', 'Zdravko', 'Ivanov',    '0888111222'), -- zdravko
('7c9e6679-7425-40de-944b-e07fc1f90ae8', '6ba7b811-9dad-41d1-80b4-00c04fd430c9', 'Maria',   'Georgieva', '0888333444'); -- maria

-- Spa staff (user_id references the hardcoded UUIDs from above)
INSERT INTO spa_staff (id, user_id, first_name, last_name, specialization) VALUES
('8f3b4e5a-6b7c-4d8e-9f0a-1b2c3d4e5f6a', '6ba7b812-9dad-41d1-80b4-00c04fd430ca', 'Elena', 'Todorova', 'Deep Tissue & Hot Stone'), -- elena
('8f3b4e5a-6b7c-4d8e-9f0a-1b2c3d4e5f6b', '6ba7b813-9dad-41d1-80b4-00c04fd430cb', 'Ivan',  'Petrov',   'Aromatherapy & Reflexology'); -- ivan

-- Spa rooms
INSERT INTO spa_rooms (id, room_number, name, capacity, status) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'R01', 'The Lotus Room',   1, 'AVAILABLE'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'R02', 'The Stone Suite',  1, 'AVAILABLE'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'R03', 'The Zen Garden',   2, 'AVAILABLE'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'R04', 'The Harmony Room', 1, 'MAINTENANCE');

-- Spa treatments
INSERT INTO spa_treatments (id, name, description, duration_minutes, price, deleted) VALUES
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d5e', 'Deep Tissue Massage', 'Targets deep layers of muscle to relieve chronic tension and pain.', 60, 65.00, FALSE),
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d5f', 'Hot Stone Therapy',   'Warm volcanic stones melt away tension and ease stiff muscles.',     90, 85.00, FALSE),
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d60', 'Aromatherapy',        'Essential oils blended with a relaxing full body massage.',           60, 60.00, FALSE),
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d61', 'Reflexology',         'Targeted pressure on foot points to restore natural energy flow.',    45, 50.00, FALSE),
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d62', 'Swedish Massage',     'Classic full body relaxation with long flowing strokes.',             60, 55.00, FALSE),
('b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d63', 'Facial Treatment',    'Deep cleansing and hydrating facial for all skin types.',             45, 55.00, FALSE);

-- Spa bookings (uses guest_id, room_id, staff_id directly from the hardcoded IDs above)
INSERT INTO spa_bookings (
    id, confirmation_code, guest_id, spa_room_id, spa_staff_id,
    booking_date, start_time, end_time, duration_minutes,
    total_price, status, notes, added_at, updated_at
) VALUES
(
    'c1d2e3f4-5a6b-4c7d-8e9f-0a1b2c3d4e5f', 'ZDR00001',
    '7c9e6679-7425-40de-944b-e07fc1f90ae7', -- Zdravko's guest ID
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', -- Lotus Room
    '8f3b4e5a-6b7c-4d8e-9f0a-1b2c3d4e5f6a', -- Elena (staff)
    '2026-07-10', '10:00:00', '11:30:00', 90, 85.00, 'PENDING',
    'Please use extra warm stones', NOW(), NOW()
),
(
    'c1d2e3f4-5a6b-4c7d-8e9f-0a1b2c3d4e60', 'ZDR00002',
    '7c9e6679-7425-40de-944b-e07fc1f90ae7', -- Zdravko's guest ID
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', -- Stone Suite
    '8f3b4e5a-6b7c-4d8e-9f0a-1b2c3d4e5f6b', -- Ivan (staff)
    '2026-07-05', '14:00:00', '15:00:00', 60, 65.00, 'CANCELLED',
    NULL, NOW(), NOW()
),
(
    'c1d2e3f4-5a6b-4c7d-8e9f-0a1b2c3d4e61', 'MAR00001',
    '7c9e6679-7425-40de-944b-e07fc1f90ae8', -- Maria's guest ID
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', -- Stone Suite
    '8f3b4e5a-6b7c-4d8e-9f0a-1b2c3d4e5f6b', -- Ivan (staff)
    '2026-07-15', '09:00:00', '10:45:00', 105, 115.00, 'PENDING',
    NULL, NOW(), NOW()
);

-- Spa booking treatments (uses booking_id and treatment_id directly)
INSERT INTO spa_booking_treatments (id, booking_id, treatment_id, price_at_booking) VALUES
(
    'd1e2f3a4-5b6c-4d7e-8f9a-0b1c2d3e4f5a',
    'c1d2e3f4-5a6b-4c7d-8e9f-0a1b2c3d4e5f', -- ZDR00001
    'b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d5f', -- Hot Stone
    85.00
),
(
    'd1e2f3a4-5b6c-4d7e-8f9a-0b1c2d3e4f5b',
    'c1d2e3f4-5a6b-4c7d-8e9f-0a1b2c3d4e60', -- ZDR00002
    'b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d5e', -- Deep Tissue
    65.00
),
(
    'd1e2f3a4-5b6c-4d7e-8f9a-0b1c2d3e4f5c',
    'c1d2e3f4-5a6b-4c7d-8e9f-0a1b2c3d4e61', -- MAR00001
    'b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d60', -- Aromatherapy
    60.00
),
(
    'd1e2f3a4-5b6c-4d7e-8f9a-0b1c2d3e4f5d',
    'c1d2e3f4-5a6b-4c7d-8e9f-0a1b2c3d4e61', -- MAR00001
    'b1c2d3e4-f5a6-4b7c-8d9e-0f1a2b3c4d61', -- Reflexology
    55.00
);