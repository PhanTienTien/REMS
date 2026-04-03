-- Sample property images to fix 404 errors
-- Run this script to add sample property images

INSERT INTO property_images (property_id, image_url, is_thumbnail, created_at) VALUES
(1, '/uploads/properties/property1.jpg', true, NOW()),
(1, '/uploads/properties/property2.jpg', false, NOW()),
(2, '/uploads/properties/property2.jpg', true, NOW()),
(2, '/uploads/properties/property3.jpg', false, NOW()),
(3, '/uploads/properties/property3.jpg', true, NOW()),
(3, '/uploads/properties/property1.jpg', false, NOW());

-- Update sample properties to have images if they don't exist
UPDATE properties p 
SET image_url = '/uploads/properties/property1.jpg' 
WHERE p.id IN (1, 2, 3) 
AND (p.image_url IS NULL OR p.image_url = '');

-- Create sample properties if none exist
INSERT IGNORE INTO properties (title, address, description, price, type, status, created_by, created_at, updated_at) VALUES
('Luxury Villa', '123 Main St', 'Beautiful luxury villa with pool', 500000, 'SALE', 'AVAILABLE', 1, NOW(), NOW()),
('Modern Apartment', '456 Oak Ave', 'Modern apartment in city center', 250000, 'SALE', 'AVAILABLE', 1, NOW(), NOW()),
('Beach House', '789 Beach Rd', 'Stunning beachfront property', 750000, 'SALE', 'AVAILABLE', 1, NOW(), NOW());
