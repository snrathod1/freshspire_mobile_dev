package com.freshspire.api.service;

import com.freshspire.api.dao.ChainDAO;
import com.freshspire.api.model.Chain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChainServiceImpl implements ChainService {

    private ChainDAO chainDAO;

    public void setChainDAO(ChainDAO chainDAO) {
        this.chainDAO = chainDAO;
    }

    @Transactional
    @Override
    public Chain getChainById(int chainId) {
        return chainDAO.getChainById(chainId);
    }
}
