CREATE TABLE `registered_cluster` (
  `cluster_id` varchar(200) NOT NULL,
  `search_endpoint` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `cert` varchar(1000) NOT NULL,
  `gvod_endpoint` varchar(100) NOT NULL,
  `heartbeats_missed` bigint(20) NOT NULL,
  `date_registered` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_last_ping` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`cluster_id`)
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  `email` varchar(150) NOT NULL,
  `cluster_id` varchar(200) NOT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index2` (`email`,`cluster_id`),
  KEY `fk_users_1_idx` (`cluster_id`),
  FOREIGN KEY (`cluster_id`) REFERENCES `registered_cluster` (`cluster_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `dataset` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `public_id` varchar(1000) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `made_public_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `owner` varchar(150) NOT NULL,
  `cluster_id` varchar(200) NOT NULL,
  `readme` mediumtext DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `public_id_UNIQUE` (`public_id`),
  KEY `fk_dataset_1_idx` (`cluster_id`), 
  FOREIGN KEY (`cluster_id`) REFERENCES `registered_cluster` (`cluster_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dataset_id` int(11) NOT NULL,
  `users` int(11) NOT NULL,
  `content` varchar(2000) NOT NULL,
  `date_published` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_comment_1_idx` (`dataset_id`),
  KEY `fk_comment_2_idx` (`users`),
  FOREIGN KEY (`users`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`Id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `comment_issue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_id` int(11) NOT NULL,
  `users` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  `date_reported` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `msg` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_comment_issue_1_idx` (`comment_id`),
  KEY `fk_comment_issue_2_idx` (`users`),
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
  KEY `fk_dataset_issue_1_idx` (`dataset_id`),
  KEY `fk_dataset_issue_2_idx` (`users`),
  FOREIGN KEY (`users`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `dataset_rating` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dataset_id` int(11) NOT NULL,
  `users` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `date_published` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dataset_rating_1_idx` (`dataset_id`),
  KEY `fk_dataset_rating_2_idx` (`users`),
  FOREIGN KEY (`users`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`Id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `popular_dataset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dataset_id` int(11) NOT NULL,
  `manifest` mediumtext NOT NULL,
  `partners` mediumtext NOT NULL,
  `leeches` int(11) NOT NULL,
  `seeds` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dataset_id_UNIQUE` (`dataset_id`),
  KEY `fk_dataset_rating_1_idx` (`dataset_id`),
  FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`Id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `dataset_category` (
  `category_id` int(11) NOT NULL,
  `dataset_id` int(11) NOT NULL,
  PRIMARY KEY (`category_id`,`dataset_id`),
  KEY `fk_dataset_category_2_idx` (`dataset_id`),
  FOREIGN KEY (`dataset_id`) REFERENCES `dataset` (`Id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=ndbcluster DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

