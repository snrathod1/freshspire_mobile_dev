package com.freshspire.api.dao;

import com.freshspire.api.model.Chain;
import com.freshspire.api.model.Discount;
import com.freshspire.api.model.Product;
import com.freshspire.api.model.Store;
import com.freshspire.api.model.response.DiscountData;
import org.hibernate.transform.ResultTransformer;

import java.math.BigDecimal;
import java.util.List;

public class DiscountSearchResultTransformer implements ResultTransformer {
    @Override
    public List transformList(List list) {
        return list;
    }

    /**
     * This is very hacky. Rewrite the SQL in DiscountDAOImpl as a Hibernate query or rename the cols or something
     * so this hacky function can go away.
     * @param row
     * @param aliases
     * @return
     */
    @Override
    public Object transformTuple(Object[] row, String[] aliases) {
        Discount d = new Discount();
        d.setDiscountId((int) row[0]);
        d.setStoreId((int) row[1]);
        d.setProductId((int) row[2]);
        d.setPosted(Long.valueOf((int)row[3]));
        d.setExpirationDate(Long.valueOf((int) row[4]));
        d.setOriginalPrice(((BigDecimal) row[5]).floatValue());
        d.setDiscountedPrice(((BigDecimal) row[6]).floatValue());
        d.setChainId((int) row[20]);
        d.setQuantity((int) row[7]);
        d.setUnit((String) row[8]);

        Store s = new Store();
        s.setStoreId(d.getStoreId());
        s.setChainId(d.getChainId());
        s.setDisplayName((String) row[9]);
        s.setStreet((String) row[10]);
        s.setCity((String) row[11]);
        s.setState((String) row[12]);
        s.setZipCode((String) row[13]);
        s.setLatitude((double) row[14]);
        s.setLongitude((double) row[15]);

        Product p = new Product();
        p.setProductId(d.getProductId());
        p.setDisplayName((String) row[16]);
        p.setFoodType((String) row[17]);
        p.setThumbnail((String) row[18]);

        Chain c = new Chain();
        c.setDisplayName((String) row[19]);
        c.setChainId(d.getChainId());

        Object[] data = new Object[4];
        data[0] = d;
        data[1] = s;
        data[2] = p;
        data[3] = c;

        return data;
    }
}
