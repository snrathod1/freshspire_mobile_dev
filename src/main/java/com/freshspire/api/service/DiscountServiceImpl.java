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
    @Override
    public List<DiscountData> getDiscountsByLatLong(float latitude, float longitude, String queryParam, float within, List<String> foodTypes, List<String> chains) {

        List<DiscountData> discountData = new ArrayList<>();
        for (Object discount : discountDAO.getDiscountByLatLong(latitude, longitude, queryParam, within, foodTypes, chains)) {
            Object[] discountArray = (Object []) discount;
            discountData.add(new DiscountData(discountArray));
        }
        return discountData;
    }

    @Transactional
    @Override
    public void addDiscount(Discount discount) {
        discountDAO.addDiscount(discount);
    }

    @Transactional
    @Override
    public boolean isUp() {
        return discountDAO.connectionIsEstablished();
    }
}
