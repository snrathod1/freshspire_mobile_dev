package com.freshspire.api.service;

import com.freshspire.api.dao.DiscountDAO;
import com.freshspire.api.model.Discount;
import com.freshspire.api.model.response.DiscountData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional
    public List getDiscountsByLatLong(float latitude, float longitude, String queryParam, float within, String foodType, String chain) {

        List<DiscountData> discountData = new ArrayList<>();
        for (Object discount : discountDAO.getDiscountByLatLong(latitude, longitude, queryParam, within, foodType, chain)) {
            Object[] discountArray = (Object []) discount;
            discountData.add(new DiscountData(discountArray));
        }

        return discountData;
    }
}
