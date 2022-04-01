package com.georgefitzpatrick.trading.crypto.data.service;

import com.georgefitzpatrick.trading.crypto.data.IdentifiableEntityService;
import com.georgefitzpatrick.trading.crypto.data.entity.Condition;
import com.georgefitzpatrick.trading.crypto.data.repository.ConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author George Fitzpatrick
 */
@Service
public class ConditionService extends IdentifiableEntityService<Condition> {

    @Autowired
    public ConditionService(ConditionRepository repository) {
        super(repository);
    }

}
