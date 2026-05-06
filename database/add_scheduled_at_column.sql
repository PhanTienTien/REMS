-- Add scheduled_at column to bookings table
-- This column stores the preferred date/time for property viewing

ALTER TABLE bookings
ADD COLUMN scheduled_at DATETIME NULL
AFTER note;

-- Add comment for documentation
ALTER TABLE bookings
MODIFY COLUMN scheduled_at DATETIME NULL COMMENT 'Thời gian khách hàng muốn hẹn xem BĐS';
