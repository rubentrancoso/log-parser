drop DATABASE IF EXISTS log;
create DATABASE log CHARACTER SET utf8 COLLATE utf8_bin;
use log;
CREATE TABLE bannedip
(
  ip       VARCHAR(255) NOT NULL
    PRIMARY KEY,
  note     VARCHAR(255) NULL,
  requests INT          NULL
)
  ENGINE = MyISAM;

CREATE TABLE logline
(
  id         INT AUTO_INCREMENT
    PRIMARY KEY,
  agent      VARCHAR(255) NULL,
  date       DATETIME     NULL,
  httpstatus INT          NOT NULL,
  ip         VARCHAR(255) NULL,
  method     VARCHAR(255) NULL
)
  ENGINE = MyISAM;



