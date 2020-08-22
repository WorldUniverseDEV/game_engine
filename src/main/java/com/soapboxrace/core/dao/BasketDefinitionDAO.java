/*
 * This file is part of the Soapbox Race World core source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Copyright (c) 2020.
 */

package com.soapboxrace.core.dao;

import com.soapboxrace.core.dao.util.BaseDAO;
import com.soapboxrace.core.jpa.BasketDefinitionEntity;

import javax.ejb.Stateless;

@Stateless
public class BasketDefinitionDAO extends BaseDAO<BasketDefinitionEntity> {

    public BasketDefinitionEntity findById(String productId) {
        return entityManager.find(BasketDefinitionEntity.class, productId);
    }

}
