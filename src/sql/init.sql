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
-- TYPE ENUM POUR LE MOUVEMENT
-- ============================================

CREATE TYPE type_mouvement AS ENUM ('ENTREE', 'SORTIE');

-- ============================================
-- TABLE : mouvement
-- ============================================

CREATE TABLE mouvement (
    id SERIAL PRIMARY KEY,

    id_article INT NOT NULL,

    type type_mouvement NOT NULL,

    date_mouvement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    qte NUMERIC(15,2) NOT NULL,

    pu NUMERIC(15,2) NOT NULL,

    valeur NUMERIC(15,2) GENERATED ALWAYS AS (qte * pu) STORED,

    qte_stock NUMERIC(15,2),

    money_value_stock NUMERIC(15,2),

    cump NUMERIC(15,2),

    CONSTRAINT fk_mouvement_article
        FOREIGN KEY (id_article)
        REFERENCES article(id)
);