/*
 * This file is part of the Soapbox Race World core source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Copyright (c) 2019.
 */

package com.soapboxrace.core.bo;

import com.soapboxrace.core.dao.CarSlotDAO;
import com.soapboxrace.core.jpa.CarSlotEntity;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import java.time.LocalDateTime;

@Stateless
public class CarSlotBO {

    @EJB
    private CarSlotDAO carSlotDAO;

    @EJB
    private BasketBO basketBO;

    @Schedule(minute = "*", hour = "*", persistent = false)
    public void scheduledRemoval() {
        System.out.println("hi");
        for (CarSlotEntity carSlotEntity : carSlotDAO.findAllWithExpirationDate()) {
            System.out.println(carSlotEntity);
            if (carSlotEntity.getOwnedCar().getExpirationDate().isBefore(LocalDateTime.now())) {
                System.out.println(basketBO.removeCar(carSlotEntity.getPersona(), carSlotEntity.getOwnedCar().getId()));
            }
        }
    }
}
