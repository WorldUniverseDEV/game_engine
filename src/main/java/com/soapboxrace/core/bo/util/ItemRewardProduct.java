package com.soapboxrace.core.bo.util;

import com.soapboxrace.core.jpa.ProductEntity;

import java.util.Collections;
import java.util.List;

public class ItemRewardProduct extends ItemRewardBase {
    protected final ProductEntity productEntity;

    public ItemRewardProduct(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    @Override
    public List<ProductEntity> getProducts() {
        return Collections.singletonList(productEntity);
    }
}