package com.freshspire.api.service;

import com.freshspire.api.dao.DiscountDAO;
import com.freshspire.api.model.Discount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiscountServiceImpl implements DiscountService {

    private DiscountDAO discountDAO;

    public void setDiscountDAO(DiscountDAO discountDAO) {
        this.discountDAO = discountDAO;
    }

    @Transactional
    public Discount getDiscountById(String discountId) {
        return discountDAO.getDiscountById(discountId);
    }
}
