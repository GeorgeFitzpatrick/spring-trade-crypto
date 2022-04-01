package com.georgefitzpatrick.trading.crypto.data.service;

import com.georgefitzpatrick.trading.crypto.data.IdentifiableEntityService;
import com.georgefitzpatrick.trading.crypto.data.entity.Strategy;
import com.georgefitzpatrick.trading.crypto.data.repository.StrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author George Fitzpatrick
 */
@Service
public class StrategyService extends IdentifiableEntityService<Strategy> {

    @Autowired
    public StrategyService(StrategyRepository repository) {
        super(repository);
    }

}
