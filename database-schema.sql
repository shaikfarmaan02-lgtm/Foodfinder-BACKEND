/* =====================================================
   RESET DATABASE - MATCHES SPRING BACKEND EXACTLY
   ===================================================== */

DROP DATABASE IF EXISTS food_donation_db;
CREATE DATABASE food_donation_db;
USE food_donation_db;


/* =====================================================
   USERS TABLE - Complete User Profile
   ===================================================== */

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(10),
    organization VARCHAR(255),
    role ENUM('DONOR','NGO','ADMIN') NOT NULL,
    approval_status ENUM('PENDING_APPROVAL','APPROVED','REJECTED','SUSPENDED') NOT NULL DEFAULT 'PENDING_APPROVAL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_approval_status (approval_status)
);


/* =====================================================
   FOOD LISTING TABLE - Main table for food donations
   ===================================================== */

CREATE TABLE food_listing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    food_name VARCHAR(100) NOT NULL,
    quantity VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    expiry_time DATETIME,
    donor_id BIGINT NOT NULL,
    status ENUM('AVAILABLE','CLAIMED') NOT NULL DEFAULT 'AVAILABLE',
    created_at DATETIME NOT NULL,

    FOREIGN KEY (donor_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_donor_id (donor_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
);


/* =====================================================
   FOOD DONATIONS TABLE - Secondary table (used by Claim)
   ===================================================== */

CREATE TABLE food_donations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    food_name VARCHAR(100) NOT NULL,
    quantity VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    expiry_time DATETIME,
    donor_id BIGINT NOT NULL,
    status ENUM('AVAILABLE','CLAIMED') NOT NULL DEFAULT 'AVAILABLE',
    created_at DATETIME NOT NULL,

    FOREIGN KEY (donor_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_donor_id (donor_id),
    INDEX idx_status (status)
);


/* =====================================================
   FOOD REQUESTS TABLE - NGO requests for food
   ===================================================== */

CREATE TABLE food_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    donation_id BIGINT NOT NULL,
    ngo_id BIGINT NOT NULL,
    status ENUM('PENDING','ACCEPTED','REJECTED','COLLECTED') NOT NULL DEFAULT 'PENDING',
    request_time DATETIME NOT NULL,
    accepted_time DATETIME,
    collected_time DATETIME,
    rejection_reason VARCHAR(500),

    FOREIGN KEY (donation_id) REFERENCES food_listing(id) ON DELETE CASCADE,
    FOREIGN KEY (ngo_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_donation_id (donation_id),
    INDEX idx_ngo_id (ngo_id),
    INDEX idx_status (status),
    INDEX idx_request_time (request_time)
);


/* =====================================================
   DISTRIBUTIONS TABLE - Food distribution tracking
   ===================================================== */

CREATE TABLE distributions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    donation_id BIGINT NOT NULL,
    food_request_id BIGINT NOT NULL,
    donor_id BIGINT NOT NULL,
    ngo_id BIGINT NOT NULL,
    status ENUM('PENDING','IN_TRANSIT','DELIVERED','CANCELLED') NOT NULL DEFAULT 'PENDING',
    distribution_date DATETIME NOT NULL,
    delivered_date DATETIME,
    distribution_location VARCHAR(255),
    notes TEXT,

    FOREIGN KEY (donation_id) REFERENCES food_listing(id) ON DELETE CASCADE,
    FOREIGN KEY (food_request_id) REFERENCES food_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (donor_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (ngo_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_donation_id (donation_id),
    INDEX idx_food_request_id (food_request_id),
    INDEX idx_donor_id (donor_id),
    INDEX idx_ngo_id (ngo_id),
    INDEX idx_status (status)
);


/* =====================================================
   CLAIMS TABLE - NGO claims for food donations
   ===================================================== */

CREATE TABLE claims (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    donation_id BIGINT NOT NULL,
    ngo_id BIGINT NOT NULL,
    claim_time DATETIME NOT NULL,

    FOREIGN KEY (donation_id) REFERENCES food_donations(id) ON DELETE CASCADE,
    FOREIGN KEY (ngo_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_donation_id (donation_id),
    INDEX idx_ngo_id (ngo_id),
    INDEX idx_claim_time (claim_time)
);


/* =====================================================
   FEEDBACK TABLE - User feedback on donations
   ===================================================== */

CREATE TABLE feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    donation_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comment TEXT NOT NULL,
    created_at DATETIME NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (donation_id) REFERENCES food_listing(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_donation_id (donation_id),
    INDEX idx_rating (rating),
    CHECK (rating >= 1 AND rating <= 5)
);


/* =====================================================
   CONTACT MESSAGES TABLE - User contact inquiries
   ===================================================== */

CREATE TABLE contact_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    message TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    resolved BOOLEAN DEFAULT false,
    admin_notes TEXT,

    INDEX idx_email (email),
    INDEX idx_created_at (created_at),
    INDEX idx_resolved (resolved)
);


/* =====================================================
   DEFAULT USER DATA (FOR TESTING)
   ===================================================== */

INSERT INTO users (name, email, password, phone, city, role, approval_status) VALUES
('Farmaan', 'farmaan@gmail.com', 'Farmaan123', '9876543210', 'Hyderabad', 'ADMIN', 'APPROVED'),
('Donor One', 'donor1@gmail.com', 'donor123', '9876543211', 'Hyderabad', 'DONOR', 'APPROVED'),
('NGO One', 'ngo1@gmail.com', 'ngo123', '9876543212', 'Secunderabad', 'NGO', 'APPROVED'),
('Donor Two', 'donor2@gmail.com', 'donor456', '9876543213', 'Bangalore', 'DONOR', 'APPROVED'),
('NGO Two', 'ngo2@gmail.com', 'ngo456', '9876543214', 'Pune', 'NGO', 'APPROVED');


/* =====================================================
   SAMPLE FOOD LISTINGS (FOR TESTING)
   ===================================================== */

INSERT INTO food_listing (food_name, quantity, location, expiry_time, donor_id, status, created_at)
VALUES
('Fresh Rice', '50 kg', 'Hyderabad Downtown', '2026-04-10 18:00:00', 2, 'AVAILABLE', NOW()),
('Biryani', '30 portions', 'Secunderabad Restaurant', '2026-04-08 20:00:00', 2, 'AVAILABLE', NOW()),
('Vegetables Mix', '100 kg', 'Bangalore Market', '2026-04-07 17:00:00', 4, 'AVAILABLE', NOW()),
('Bread & Butter', '200 units', 'Pune Bakery', '2026-04-06 16:00:00', 4, 'CLAIMED', NOW());


/* =====================================================
   SAMPLE FOOD DONATIONS (FOR TESTING)
   ===================================================== */

INSERT INTO food_donations (food_name, quantity, location, expiry_time, donor_id, status, created_at)
VALUES
('Cooked Meals', '100 portions', 'Hyderabad', '2026-04-09 12:00:00', 2, 'AVAILABLE', NOW()),
('Packaged Food', '50 boxes', 'Secunderabad', '2026-04-12 10:00:00', 4, 'CLAIMED', NOW());


/* =====================================================
   SAMPLE FOOD REQUESTS (FOR TESTING)
   ===================================================== */

INSERT INTO food_requests (donation_id, ngo_id, status, request_time, accepted_time)
VALUES
(1, 3, 'ACCEPTED', NOW(), NOW()),
(2, 5, 'PENDING', NOW(), NULL),
(3, 3, 'ACCEPTED', NOW(), NOW());


/* =====================================================
   SAMPLE DISTRIBUTIONS (FOR TESTING)
   ===================================================== */

INSERT INTO distributions (donation_id, food_request_id, donor_id, ngo_id, status, distribution_date, distribution_location)
VALUES
(1, 1, 2, 3, 'DELIVERED', NOW(), 'Hyderabad NGO Office'),
(2, 2, 4, 5, 'IN_TRANSIT', NOW(), 'Pune Distribution Center');


/* =====================================================
   SAMPLE CLAIMS (FOR TESTING)
   ===================================================== */

INSERT INTO claims (donation_id, ngo_id, claim_time)
VALUES
(1, 3, NOW()),
(2, 5, NOW());


/* =====================================================
   SAMPLE FEEDBACK (FOR TESTING)
   ===================================================== */

INSERT INTO feedback (user_id, donation_id, rating, comment, created_at)
VALUES
(3, 1, 5, 'Excellent quality food! Thank you for the donation.', NOW()),
(5, 3, 4, 'Good food, received on time.', NOW());


/* =====================================================
   SAMPLE CONTACT MESSAGES (FOR TESTING)
   ===================================================== */

INSERT INTO contact_messages (name, email, subject, phone, message, created_at, resolved)
VALUES
('John Doe', 'john@example.com', 'Query about donation process', '9999999999', 'How can I donate food?', NOW(), false),
('Jane Smith', 'jane@example.com', 'Feedback inquiry', '8888888888', 'Great platform!', NOW(), true);


/* =====================================================
   VERIFY SCHEMA
   ===================================================== */

SHOW TABLES;

DESCRIBE users;
DESCRIBE food_listing;
DESCRIBE food_donations;
DESCRIBE food_requests;
DESCRIBE distributions;
DESCRIBE claims;
DESCRIBE feedback;
DESCRIBE contact_messages;

SELECT * FROM users;
SELECT * FROM food_listing;
SELECT * FROM food_requests;
SELECT * FROM distributions;
