DROP DATABASE IF EXISTS futabus;
-- Nếu cơ sở dữ liệu chưa tồn tại, thực hiện câu lệnh CREATE DATABASE
CREATE DATABASE IF NOT EXISTS futabus;

-- Sử dụng cơ sở dữ liệu 
USE futabus;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
--
-- --------------------------------------------------------

CREATE TABLE `users` (
  `id` int NOT NULL,
  `fullname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
  `phone_number` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
  `password` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `date_of_birth` date DEFAULT NULL,
  `role_id` int DEFAULT '1',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
CREATE TABLE `routes` (
  `route_id` INT NOT NULL,
  `start_location` VARCHAR(255) NOT NULL,
  `end_location` VARCHAR(255) NOT NULL,
  `distance` DECIMAL(10,2) NOT NULL,
  `estimated_duration` TIME NOT NULL,
  `pickup_dropoff_points` JSON,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
--
CREATE TABLE `roles` (
  `id` int NOT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--
CREATE TABLE `tokens` (
  `id` int NOT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `token_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `revoked` tinyint(1) NOT NULL,
  `expired` tinyint(1) NOT NULL,
  `user_id` int DEFAULT NULL,
  `is_mobile` tinyint(1) DEFAULT '0',
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
  `refresh_expiration_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--
CREATE TABLE `buses` (
  `bus_id` INT NOT NULL,
  `operator_id` INT NOT NULL,
  `bus_type` VARCHAR(50) NOT NULL,
  `total_seats` INT NOT NULL,
  `seat_map` JSON,
  `amenities` JSON,
  `license_plate` VARCHAR(20) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
-- 
CREATE TABLE `trips` (
  `trip_id` INT NOT NULL,
  `route_id` INT NOT NULL,
  `bus_id` INT NOT NULL,
  `departure_time` TIMESTAMP NOT NULL,
  `estimated_arrival_time` TIMESTAMP NOT NULL,
  `base_price` DECIMAL(10,2) NOT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'active',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
-- 
CREATE TABLE `tickets` (
  `ticket_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `trip_id` INT NOT NULL,
  `seat_number` VARCHAR(10) NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `promotion_id` INT,
  `qr_code` VARCHAR(255) UNIQUE,
  `status` VARCHAR(20) NOT NULL DEFAULT 'pending',
  `booking_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
-- 
CREATE TABLE `payments` (
  `payment_id` INT NOT NULL,
  `ticket_id` INT NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `method` VARCHAR(50) NOT NULL,
  `status` VARCHAR(20) NOT NULL DEFAULT 'pending',
  `transaction_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
-- 
CREATE TABLE `admins` (
  `admin_id` INT NOT NULL,
  `full_name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(50) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
-- 
CREATE TABLE `operators` (
  `operator_id` INT NOT NULL,
  `company_name` VARCHAR(255) NOT NULL,
  `contact_info` JSON,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
-- 
CREATE TABLE `notifications` (
  `notification_id` INT NOT NULL,
  `user_id` INT,
  `operator_id` INT,
  `content` TEXT NOT NULL,
  `channel` VARCHAR(50) NOT NULL,
  `sent_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `status` VARCHAR(20) NOT NULL DEFAULT 'sent'
) ENGINE=InnoDB;
-- 
CREATE TABLE `feedbacks` (
  `feedback_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `trip_id` INT NOT NULL,
  `content` TEXT NOT NULL,
  `rating` INT CHECK (`rating` >= 1 AND `rating` <= 5),
  `status` VARCHAR(20) NOT NULL DEFAULT 'pending',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
-- 
CREATE TABLE `transfers` (
  `transfer_id` INT NOT NULL,
  `ticket_id` INT NOT NULL,
  `pickup_location` VARCHAR(255) NOT NULL,
  `pickup_time` TIMESTAMP NOT NULL,
  `vehicle_type` VARCHAR(50) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE `otp` (
  `otp_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `otp_code` VARCHAR(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `expires_at` TIMESTAMP NOT NULL,
  `is_used` TINYINT(1) DEFAULT '0' COMMENT '0: Not used, 1: Used',
  `is_valid` TINYINT(1) DEFAULT '1' COMMENT '0: Invalid, 1: Valid',
  PRIMARY KEY (`otp_id`)
) ENGINE=INNODB;

CREATE TABLE `promotions` (
  `promotion_id` INT NOT NULL,
  `code` VARCHAR(50) UNIQUE NOT NULL,             -- Mã giảm giá (VD: FUTA20)
  `type` VARCHAR(50) NOT NULL,                    -- percent, fixed_amount, other
  `value` DECIMAL(10, 2) NOT NULL,                -- Giá trị giảm (%, VNĐ)
  `start_date` TIMESTAMP NOT NULL,
  `end_date` TIMESTAMP NOT NULL,
  `max_usage` INT,                                -- Số lần sử dụng tối đa (null nếu không giới hạn)
  `conditions` JSON,                              -- Điều kiện áp dụng (VD: tuyến, giá vé tối thiểu)
  `operator_id` INT,                              -- Liên kết với nhà xe (nullable nếu hệ thống tạo)
  `status` VARCHAR(20) NOT NULL DEFAULT 'active', -- active, expired, disabled
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
--
-- Chỉ mục cho các bảng đã đổ
--
ALTER TABLE `otp` 
	ADD KEY `user_id` (`user_id`);

ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `token` (`token`),
  ADD KEY `user_id` (`user_id`);

ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD KEY `role_id` (`role_id`);

ALTER TABLE `routes`
  ADD PRIMARY KEY (`route_id`);
  
ALTER TABLE `transfers`
  ADD PRIMARY KEY (`transfer_id`);
  
ALTER TABLE `feedbacks`
  ADD PRIMARY KEY (`feedback_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `trip_id` (`trip_id`);
  
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`notification_id`);
  
ALTER TABLE `admins`
  ADD PRIMARY KEY (`admin_id`),
  ADD UNIQUE (`email`);

ALTER TABLE `payments`
  ADD PRIMARY KEY (`payment_id`);
  
ALTER TABLE `tickets`
  ADD PRIMARY KEY (`ticket_id`);

ALTER TABLE `trips`
  ADD PRIMARY KEY (`trip_id`);
  
ALTER TABLE `buses`
  ADD PRIMARY KEY (`bus_id`),
  ADD UNIQUE (`license_plate`);  
  
ALTER TABLE `operators`
  ADD PRIMARY KEY (`operator_id`);

ALTER TABLE `promotions`
  ADD PRIMARY KEY (`promotion_id`);
--
-- AUTO_INCREMENT cho các bảng đã đổ
--
ALTER TABLE `buses`
  MODIFY `bus_id` INT NOT NULL AUTO_INCREMENT;
  
ALTER TABLE `trips`
  MODIFY `trip_id` INT NOT NULL AUTO_INCREMENT;

ALTER TABLE `notifications`
  MODIFY `notification_id` INT NOT NULL AUTO_INCREMENT;
 
ALTER TABLE `routes`
  MODIFY `route_id` INT NOT NULL AUTO_INCREMENT;

ALTER TABLE `tokens`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

ALTER TABLE `transfers`
  MODIFY `transfer_id` INT NOT NULL AUTO_INCREMENT;

ALTER TABLE `feedbacks`
  MODIFY `feedback_id` INT NOT NULL AUTO_INCREMENT;

ALTER TABLE `admins`
  MODIFY `admin_id` INT NOT NULL AUTO_INCREMENT;
  
ALTER TABLE `payments`
  MODIFY `payment_id` INT NOT NULL AUTO_INCREMENT;
  
ALTER TABLE `tickets`
  MODIFY `ticket_id` INT NOT NULL AUTO_INCREMENT;

ALTER TABLE `operators`
  MODIFY `operator_id` INT NOT NULL AUTO_INCREMENT;
  
ALTER TABLE `promotions`
  MODIFY `promotion_id` INT NOT NULL AUTO_INCREMENT;	  
--
-- Các ràng buộc cho các bảng đã đổ
  
ALTER TABLE `tokens`
  ADD CONSTRAINT `tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
COMMIT;

ALTER TABLE `transfers`
  ADD CONSTRAINT `fk_transfers_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `tickets`(`ticket_id`) ON DELETE RESTRICT;

ALTER TABLE `feedbacks`
  ADD CONSTRAINT `fk_feedbacks_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE RESTRICT,
  ADD CONSTRAINT `fk_feedbacks_trip` FOREIGN KEY (`trip_id`) REFERENCES `trips`(`trip_id`) ON DELETE RESTRICT;

ALTER TABLE `notifications`
  ADD CONSTRAINT `fk_notifications_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_notifications_operator` FOREIGN KEY (`operator_id`) REFERENCES `operators`(`operator_id`) ON DELETE SET NULL;
  
ALTER TABLE `payments`
  ADD CONSTRAINT `fk_payments_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `tickets`(`ticket_id`) ON DELETE RESTRICT;
  
ALTER TABLE `tickets`
  ADD CONSTRAINT `fk_tickets_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE RESTRICT,
  ADD CONSTRAINT `fk_tickets_trip` FOREIGN KEY (`trip_id`) REFERENCES `trips`(`trip_id`) ON DELETE RESTRICT,
  ADD CONSTRAINT `fk_tickets_promotion` FOREIGN KEY (`promotion_id`) REFERENCES `promotions`(`promotion_id`) ON DELETE SET NULL;

ALTER TABLE `trips`
  ADD CONSTRAINT `fk_trips_route` FOREIGN KEY (`route_id`) REFERENCES `routes`(`route_id`) ON DELETE RESTRICT,
  ADD CONSTRAINT `fk_trips_bus` FOREIGN KEY (`bus_id`) REFERENCES `buses`(`bus_id`) ON DELETE RESTRICT;
  
ALTER TABLE `buses`
  ADD CONSTRAINT `fk_buses_operator` FOREIGN KEY (`operator_id`) REFERENCES `operators` (`operator_id`) ON DELETE RESTRICT;
ALTER TABLE `otp`
  ADD CONSTRAINT `fk_otp_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
  
ALTER TABLE `promotions`
  ADD CONSTRAINT `fk_promotions_operator`
  FOREIGN KEY (`operator_id`) REFERENCES `operators`(`operator_id`)
  ON DELETE SET NULL;