package com.pos.pos_backend.config;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pos.pos_backend.business.entity.Business;
import com.pos.pos_backend.business.repository.BusinessRepository;
import com.pos.pos_backend.category.Category;
import com.pos.pos_backend.category.repository.CategoryRepository;
import com.pos.pos_backend.inventory.entity.Inventory;
import com.pos.pos_backend.inventory.repository.InventoryRepository;
import com.pos.pos_backend.product.entity.Product;
import com.pos.pos_backend.product.entity.ProductVariant;
import com.pos.pos_backend.product.repository.ProductRepository;
import com.pos.pos_backend.product.repository.ProductVariantRepository;
import com.pos.pos_backend.user.entity.Role;
import com.pos.pos_backend.user.entity.User;
import com.pos.pos_backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestDataFactory {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private ProductVariantRepository variantRepository;
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private InventoryRepository inventoryRepository;

    public User createOwner(
            String email,
            Long businessId
    ) {

        Business business =
                businessRepository.findById(businessId)
                        .orElseThrow();

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(
                passwordEncoder.encode("Password123")
        );
        user.setRole(Role.OWNER);
        user.setBusiness(business);

        return userRepository.save(user);
    }

    public User createStaff(
        String email,
        Long businessId
    ) {

        Business business =
                businessRepository.findById(businessId)
                        .orElseThrow();

        User user = new User();

        user.setEmail(email);
        user.setPasswordHash(
                passwordEncoder.encode("Password123")
        );
        user.setRole(Role.STAFF);
        user.setBusiness(business);

        return userRepository.save(user);
    }

    public ProductVariant createInventoryWithStock(
        int quantityOnHand
    ) {

        Business business =
                createBusiness(
                        "Test Business"
                );

        Category category =
                createCategory(
                        business,
                        "Beverages"
                );

        Product product =
                createProduct(
                        business,
                        category,
                        "Coke"
                );

        ProductVariant variant =
                createVariant(product);

        createInventory(
                variant,
                quantityOnHand
        );

        return variant;
    }

    public Business createBusiness(
        String name
    ) {

        Business business = new Business();

        business.setName(name);

        return businessRepository.save(business);
    }

    public Category createCategory(
        Business business,
        String name
    ) {

        Category category = Category.builder()
                .name(name)
                .business(business)
                .build();

        return categoryRepository.save(category);
    }

    public Product createProduct(
            Business business,
            Category category,
            String name
    ) {

        Product product = Product.builder()
                .name(name)
                .business(business)
                .category(category)
                .build();

        return productRepository.save(product);
    }

    public ProductVariant createVariant(
            Product product
    ) {

        ProductVariant variant =
                ProductVariant.builder()
                        .product(product)
                        .sku(
                                "SKU-" +
                                System.nanoTime()
                        )
                        .variantName("Default")
                        .sellingPrice(
                                new BigDecimal("10.00")
                        )
                        .build();

        return variantRepository.save(variant);
    }

    public Inventory createInventory(
            ProductVariant variant,
            int quantityOnHand
    ) {

        Inventory inventory =
                Inventory.builder()
                        .variant(variant)
                        .quantityOnHand(
                                quantityOnHand
                        )
                        .reservedQuantity(0)
                        .build();

        return inventoryRepository.save(
                inventory
        );
    }


}