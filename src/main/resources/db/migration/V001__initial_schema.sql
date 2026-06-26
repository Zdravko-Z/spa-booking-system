CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CLIENT'
);

CREATE TABLE guests (
    id UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    user_id UUID NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE spa_staff (
    id UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    specialization VARCHAR(100),
    user_id UUID NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE spa_rooms (
    id UUID PRIMARY KEY,
    room_number VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100),
    capacity INT NOT NULL DEFAULT 1,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
);

CREATE TABLE spa_treatments (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    duration_minutes INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    deleted BOOLEAN default FALSE
);

CREATE TABLE spa_bookings (
    id UUID PRIMARY KEY,
    confirmation_code VARCHAR(50) NOT NULL UNIQUE,
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    duration_minutes INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    notes TEXT,
    added_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    guest_id UUID NOT NULL,
    spa_room_id UUID NOT NULL,
    spa_staff_id UUID NOT NULL,
    FOREIGN KEY (guest_id) REFERENCES guests(id),
    FOREIGN KEY (spa_room_id) REFERENCES spa_rooms(id),
    FOREIGN KEY (spa_staff_id) REFERENCES spa_staff(id)
);

CREATE TABLE spa_booking_treatments (
    id UUID PRIMARY KEY,
    price_at_booking DECIMAL(10, 2) NOT NULL,
    booking_id UUID NOT NULL,
    treatment_id UUID NOT NULL,
    CONSTRAINT uq_booking_treatment UNIQUE (booking_id, treatment_id),
    FOREIGN KEY (booking_id)   REFERENCES spa_bookings(id)   ON DELETE CASCADE,
    FOREIGN KEY (treatment_id) REFERENCES spa_treatments(id)
);