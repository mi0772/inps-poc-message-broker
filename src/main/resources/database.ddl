-- db.applicazioni definition

CREATE TABLE `applicazioni` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `app_name` varchar(250) NOT NULL,
                                `app_key` varchar(250) NOT NULL,
                                `user_id` varchar(250) DEFAULT NULL,
                                `identity_provider` varchar(250) DEFAULT NULL,
                                `codice_archivio` varchar(250) DEFAULT NULL,
                                `progetto` varchar(250) DEFAULT NULL,
                                `queue` varchar(100) NOT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;


-- db.arca_events_pending definition

CREATE TABLE `arca_events_pending` (
                                       `id` int(11) NOT NULL AUTO_INCREMENT,
                                       `id_applicazione` int(11) NOT NULL DEFAULT '0',
                                       `arca_key` varchar(11) DEFAULT NULL,
                                       `stato` int(11) DEFAULT NULL,
                                       `xml` text NOT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3516 DEFAULT CHARSET=latin1;
