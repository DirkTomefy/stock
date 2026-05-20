CREATE DATABASE stock;
\c stock;
-- ============================================
-- TABLE : methode_gestion_stock
-- ============================================

CREATE TABLE methode_gestion_stock (
    sigle VARCHAR(10) PRIMARY KEY
);

INSERT INTO methode_gestion_stock(sigle) VALUES
('LIFO'),
('FIFO'),
('CUMP');

-- ============================================
-- TABLE : article
-- ============================================

CREATE TABLE article (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(255) NOT NULL,

    sigle_gestion_stock VARCHAR(10) NOT NULL,

    CONSTRAINT fk_article_methode
        FOREIGN KEY (sigle_gestion_stock)
        REFERENCES methode_gestion_stock(sigle)
);


-- ============================================
-- TABLE : mouvement
-- ============================================

CREATE TABLE mouvement (
    id SERIAL PRIMARY KEY,

    id_article INT NOT NULL,

    type VARCHAR(200) NOT NULL,

    date_mouvement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    qte NUMERIC(15,2) NOT NULL,

    pu NUMERIC(15,2) NOT NULL,

    valeur NUMERIC(15,2) ,

    qte_prise NUMERIC(15,2) NOT NULL DEFAULT 0,

    total_prise_for_entree NUMERIC(15,2) NOT NULL DEFAULT 0,

    qte_stock NUMERIC(15,2),

    money_value_stock NUMERIC(15,2),

    cump NUMERIC(15,2),

    source_id INT, -- nullable pour les mouvements d'entrée

    CONSTRAINT fk_mouvement_article
        FOREIGN KEY (id_article)
        REFERENCES article(id),

    CONSTRAINT fk_mouvement_source
        FOREIGN KEY (source_id)
        REFERENCES mouvement(id)
);

CREATE OR REPLACE VIEW last_mouvement AS
SELECT m.*
FROM mouvement m
WHERE m.id = (
    SELECT MAX(id)
    FROM mouvement
);


CREATE OR REPLACE VIEW mouvement_fifo AS
SELECT *
FROM mouvement
ORDER BY date_mouvement ASC, id ASC;

CREATE OR REPLACE VIEW mouvement_lifo AS
SELECT *
FROM mouvement
ORDER BY date_mouvement DESC, id DESC;

CREATE OR REPLACE VIEW etat_stock_general AS
SELECT
    a.id AS id_article,
    a.libelle AS libelle_article,
    a.sigle_gestion_stock,
    COALESCE(lm.qte_stock, 0)::NUMERIC(15,2) AS qte_stock,
    COALESCE(lm.money_value_stock, 0)::NUMERIC(15,2) AS money_value_stock,
    COALESCE(lm.cump, 0)::NUMERIC(15,2) AS cump,
    lm.date_mouvement AS date_dernier_mouvement
FROM article a
LEFT JOIN (
    SELECT DISTINCT ON (m.id_article)
        m.id_article,
        m.qte_stock,
        m.money_value_stock,
        m.cump,
        m.date_mouvement
    FROM mouvement m
    ORDER BY m.id_article, m.date_mouvement DESC, m.id DESC
) lm ON lm.id_article = a.id
ORDER BY a.id;