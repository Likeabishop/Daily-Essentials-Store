CREATE TABLE sale
(
    id BIGSERIAL PRIMARY KEY,

    sale_reference VARCHAR(50) NOT NULL UNIQUE,

    business_id BIGINT NOT NULL,

    processed_by BIGINT NOT NULL,

    payment_method VARCHAR(20) NOT NULL,

    payment_status VARCHAR(20) NOT NULL,

    sale_status VARCHAR(20) NOT NULL,

    subtotal NUMERIC(19,2) NOT NULL,

    total NUMERIC(19,2) NOT NULL,

    idempotency_key VARCHAR(255),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_sale_business
        FOREIGN KEY (business_id)
        REFERENCES business(id),

    CONSTRAINT fk_sale_user
        FOREIGN KEY (processed_by)
        REFERENCES users(id)
);

CREATE TABLE sale_item
(
    id BIGSERIAL PRIMARY KEY,

    sale_id BIGINT NOT NULL,

    variant_id BIGINT NOT NULL,

    sku_snapshot VARCHAR(100) NOT NULL,

    product_name_snapshot VARCHAR(255) NOT NULL,

    variant_name_snapshot VARCHAR(100) NOT NULL,

    unit_price_snapshot NUMERIC(19,2) NOT NULL,

    quantity INTEGER NOT NULL,

    line_total NUMERIC(19,2) NOT NULL,

    CONSTRAINT fk_sale_item_sale
        FOREIGN KEY (sale_id)
        REFERENCES sale(id),

    CONSTRAINT fk_sale_item_variant
        FOREIGN KEY (variant_id)
        REFERENCES product_variant(id)
);

CREATE TABLE payment
(
    id BIGSERIAL PRIMARY KEY,

    sale_id BIGINT NOT NULL UNIQUE,

    payment_method VARCHAR(20) NOT NULL,

    payment_status VARCHAR(20) NOT NULL,

    proof_reference VARCHAR(255),

    proof_received BOOLEAN NOT NULL DEFAULT FALSE,

    confirmed_by BIGINT,

    confirmed_at TIMESTAMP,

    notes VARCHAR(500),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_payment_sale
        FOREIGN KEY (sale_id)
        REFERENCES sale(id),

    CONSTRAINT fk_payment_confirmer
        FOREIGN KEY (confirmed_by)
        REFERENCES users(id)
);

