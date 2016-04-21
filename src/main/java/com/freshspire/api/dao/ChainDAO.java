package com.freshspire.api.dao;

import com.freshspire.api.model.Chain;

public interface ChainDAO {
    Chain getChainById(int chainId);
}
