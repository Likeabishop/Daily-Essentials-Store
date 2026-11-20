package com.pos.pos_backend.sale.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.pos.pos_backend.product.entity.ProductVariant;

@Entity
@Table(name = "sale_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    private String skuSnapshot;

    private String productNameSnapshot;

    private String variantNameSnapshot;

    private BigDecimal unitPriceSnapshot;

    private Integer quantity;

    private BigDecimal lineTotal;
}
