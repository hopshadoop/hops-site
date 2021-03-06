 CREATE TABLE `variables` (
  `id` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=ndbcluster DEFAULT CHARSET=latin1 COLLATE=latin1_general_cs;

CREATE TABLE `registered_cluster` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `public_id` varchar(200) NOT NULL UNIQUE,
  `org_name` varchar(100) NOT NULL UNIQUE,
  `email` varchar(100) NOT NULL UNIQUE,
  `cert` blob NOT NULL,
  `dela_endpoint` varchar(100) NOT NULL,
  `http_endpoint` varchar(100) NOT NULL,
  `date_registered` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  `email` varchar(150) NOT NULL,
  `cluster_id` int(11) NOT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index2` (`email`,`cluster_id`),
  FOREIGN KEY (`cluster_id`) REFERENCES `registered_cluster` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `dataset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `public_id` varchar(1000) NOT NULL UNIQUE,
  `owner_user_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `published_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `readme_path` varchar(150) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `size` BIGINT DEFAULT '0',
  `rating` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`owner_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `cluster_heartbeat` (
  `cluster_id` int(11) NOT NULL UNIQUE,
  `date_last_ping` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cluster_id`),
  FOREIGN KEY (`cluster_id`) REFERENCES `registered_cluster` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `dataset_status` (
    `id` int(11) NOT NULL,
    `status` varchar(45),
    PRIMARY KEY (`id`)
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `live_dataset` (
  `dataset_id` int(11) NOT NULL,
  `cluster_id` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`dataset_id`,`cluster_id`),
  FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`cluster_id`) REFERENCES `cluster_heartbeat` (`cluster_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`status`) REFERENCES `dataset_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `dataset_health` (
  `dataset_id` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `count` int(11) NOT NULL DEFAULT '0',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`dataset_id`, `status`),
  FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`status`) REFERENCES `dataset_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dataset_id` int(11) NOT NULL,
  `users` int(11) NOT NULL,
  `content` varchar(2000) NOT NULL,
  `date_published` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`users`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `comment_issue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_id` int(11) NOT NULL,
  `users` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  `date_reported` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `msg` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`users`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `dataset_issue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dataset_id` int(11) NOT NULL,
  `users` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  `msg` varchar(2000) DEFAULT NULL,
  `date_reported` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`users`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `dataset_rating` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dataset_id` int(11) NOT NULL,
  `users` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `date_published` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index4` (`dataset_id`,`users`),
  FOREIGN KEY (`users`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `dataset_category` (
  `category_id` int(11) NOT NULL,
  `dataset_id` int(11) NOT NULL,
  PRIMARY KEY (`category_id`,`dataset_id`),
  FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

