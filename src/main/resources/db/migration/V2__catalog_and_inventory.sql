CREATE TABLE category
(
    id BIGSERIAL PRIMARY KEY,

    business_id BIGINT NOT NULL,

    name VARCHAR(100) NOT NULL,

    description VARCHAR(500),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_category_business
        FOREIGN KEY (business_id)
        REFERENCES business(id)
);

CREATE TABLE product
(
    id BIGSERIAL PRIMARY KEY,

    business_id BIGINT NOT NULL,

    category_id BIGINT NOT NULL,

    name VARCHAR(255) NOT NULL,

    description TEXT,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_product_business
        FOREIGN KEY (business_id)
        REFERENCES business(id),

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES category(id)
);

CREATE TABLE product_variant
(
    id BIGSERIAL PRIMARY KEY,

    product_id BIGINT NOT NULL,

    sku VARCHAR(100) NOT NULL,

    variant_name VARCHAR(100) NOT NULL,

    selling_price NUMERIC(19,2) NOT NULL,

    cost_price NUMERIC(19,2),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_variant_product
        FOREIGN KEY (product_id)
        REFERENCES product(id),

    CONSTRAINT uk_variant_sku
        UNIQUE (sku)
);

CREATE TABLE inventory
(
    id BIGSERIAL PRIMARY KEY,

    variant_id BIGINT NOT NULL,

    quantity_on_hand INTEGER NOT NULL DEFAULT 0,

    reserved_quantity INTEGER NOT NULL DEFAULT 0,

    reorder_level INTEGER NOT NULL DEFAULT 5,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_inventory_variant
        FOREIGN KEY (variant_id)
        REFERENCES product_variant(id),

    CONSTRAINT uk_inventory_variant
        UNIQUE (variant_id)
);

CREATE TABLE inventory_movement
(
    id BIGSERIAL PRIMARY KEY,

    variant_id BIGINT NOT NULL,

    performed_by BIGINT NOT NULL,

    movement_type VARCHAR(50) NOT NULL,

    quantity_change INTEGER NOT NULL,

    reason VARCHAR(500),

    reference_number VARCHAR(255),

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_movement_variant
        FOREIGN KEY (variant_id)
        REFERENCES product_variant(id),

    CONSTRAINT fk_movement_user
        FOREIGN KEY (performed_by)
        REFERENCES users(id)
);

CREATE INDEX idx_product_business
ON product(business_id);

CREATE INDEX idx_category_business
ON category(business_id);

CREATE INDEX idx_variant_sku
ON product_variant(sku);

CREATE INDEX idx_inventory_variant
ON inventory(variant_id);

CREATE INDEX idx_inventory_movement_variant
ON inventory_movement(variant_id);

