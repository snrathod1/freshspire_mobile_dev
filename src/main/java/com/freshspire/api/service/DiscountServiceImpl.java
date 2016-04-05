package com.freshspire.api.service;

import com.freshspire.api.dao.DiscountDAO;
import com.freshspire.api.model.Discount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {

    private DiscountDAO discountDAO;

    public void setDiscountDAO(DiscountDAO discountDAO) {
        this.discountDAO = discountDAO;
    }

    @Transactional
    @Override
    public Discount getDiscountById(int discountId) {
        return discountDAO.getDiscountById(discountId);
    }

    public List<Discount> getDiscountsByLatLong(float latitude, float longitude, String queryParam, float within, String foodType, String chain) {
        return discountDAO.getDiscountByLatLong(latitude, longitude, queryParam, within, foodType, chain);
    }
}
