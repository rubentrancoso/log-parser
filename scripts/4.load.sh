#!/bin/bash
. SENHA_MYSQL
mysql -u root -p$SENHA_MYSQL -h 127.0.0.1 -e "
use log;

LOAD DATA LOCAL INFILE
'./csv_base_area.csv'
INTO TABLE area
FIELDS TERMINATED BY '|' 
ENCLOSED BY '\"'
LINES TERMINATED BY '\n'
(nome,gestor);

use dashboard;
LOAD DATA LOCAL INFILE
'./csv_base_torre.csv'
INTO TABLE base_torre
FIELDS TERMINATED BY '|' 
ENCLOSED BY '\"'
LINES TERMINATED BY '\n'
(nome,nome_area);

use dashboard;
LOAD DATA LOCAL INFILE
'./csv_tecnologia.csv'
INTO TABLE tecnologia
FIELDS TERMINATED BY '|' 
ENCLOSED BY '\"'
LINES TERMINATED BY '\n'
(nome);

use dashboard;
LOAD DATA LOCAL INFILE
'./csv_produto.csv'
INTO TABLE produto
FIELDS TERMINATED BY '|' 
ENCLOSED BY '\"'
LINES TERMINATED BY '\n'
(nome);

use dashboard;
LOAD DATA LOCAL INFILE
'./csv_pilares.csv'
INTO TABLE pilar
FIELDS TERMINATED BY '|' 
ENCLOSED BY '\"'
LINES TERMINATED BY '\n'
(nome);

use dashboard;
LOAD DATA LOCAL INFILE
'./csv_catalogo.csv'
INTO TABLE base_carga
FIELDS TERMINATED BY '|' 
ENCLOSED BY '\"'
LINES TERMINATED BY '\n'
(atividade,torre,pilares,tecnologia,produto,automatizado);

-- cria a tabela torre associadas a area
INSERT INTO
  torre (nome, area_id)
select base_torre.nome as nome, area.id as area_id  from base_torre, area where base_torre.nome_area=area.nome;

-- padroniza os valores
UPDATE
  base_carga
SET
  automatizado=LOWER(automatizado);

UPDATE
  base_carga
SET
  automatizado='nao'
where automatizado=_utf8'não';

INSERT INTO job
(nome, pilar_id, produto_id, tecnologia_id, torre_id, area_id, automatizado)
select
  atividade as nome,
  (select pilar.id from pilar where base_carga.pilares=pilar.nome) as pilar_id,
  (select produto.id from produto where base_carga.produto=produto.nome) as produto_id,
  (select tecnologia.id from tecnologia where base_carga.tecnologia=tecnologia.nome) as tecnologia_id,
  (select torre.id from torre where base_carga.torre=torre.nome) as torre_id,
  (select torre.area_id from torre where base_carga.torre=torre.nome) as area_id,
  automatizado
from
  base_carga;
  
UPDATE
  base_carga
SET
  tecnologia='empty'
where tecnologia='';
UPDATE
  base_carga
SET
  produto='empty'
where produto='';
UPDATE
  base_carga
SET
  automatizado='empty'
where automatizado='';
UPDATE
  base_carga
SET
  pilares='empty'
where pilares='';
UPDATE
  base_carga
SET
  torre='empty'
where torre='';
UPDATE
  base_carga
SET
  atividade='empty'
where atividade='';

" -v


