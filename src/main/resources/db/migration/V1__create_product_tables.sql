CREATE TABLE product
(
    id                BIGINT         NOT NULL,
    status            VARCHAR(20)    NOT NULL,
    created_at        DATETIME(6),
    updated_at        DATETIME(6),
    name              VARCHAR(255)   NOT NULL,
    thumbnail_url     VARCHAR(255)   NOT NULL,
    description       TEXT,
    short_description VARCHAR(255),
    cost_price        DECIMAL(19, 2) NOT NULL,
    sales_price       DECIMAL(19, 2) NOT NULL,
    discounted_price  DECIMAL(19, 2) NOT NULL,
    product_status    VARCHAR(20)    NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE product_option
(
    id               BIGINT         NOT NULL,
    status           VARCHAR(20)    NOT NULL,
    created_at       DATETIME(6),
    updated_at       DATETIME(6),
    product_id       BIGINT         NOT NULL,
    name             VARCHAR(255)   NOT NULL,
    description      TEXT,
    cost_price       DECIMAL(19, 2) NOT NULL,
    sales_price      DECIMAL(19, 2) NOT NULL,
    discounted_price DECIMAL(19, 2) NOT NULL,
    stock_quantity   INT            NOT NULL,
    sort_order       INT            NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE product_section
(
    id         BIGINT      NOT NULL,
    status     VARCHAR(20) NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    product_id BIGINT      NOT NULL,
    type       VARCHAR(20) NOT NULL,
    content    LONGTEXT    NOT NULL,
    sort_order INT         NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE INDEX idx_product_option_lookup ON product_option (product_id, status, sort_order);
CREATE INDEX idx_product_section_lookup ON product_section (product_id, status, sort_order);

CREATE TABLE product_category
(
    id          BIGINT      NOT NULL,
    status      VARCHAR(20) NOT NULL,
    created_at  DATETIME(6),
    updated_at  DATETIME(6),
    product_id  BIGINT      NOT NULL,
    category_id BIGINT      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_product_category UNIQUE (product_id, category_id)
) ENGINE = InnoDB;
