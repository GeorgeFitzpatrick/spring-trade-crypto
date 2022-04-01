package com.georgefitzpatrick.trading.crypto.data.service;

import com.georgefitzpatrick.trading.crypto.data.IdentifiableEntityService;
import com.georgefitzpatrick.trading.crypto.data.entity.ExchangeCredentials;
import com.georgefitzpatrick.trading.crypto.data.repository.ExchangeCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author George Fitzpatrick
 */
@Service
public class ExchangeCredentialsService extends IdentifiableEntityService<ExchangeCredentials> {

    @Autowired
    public ExchangeCredentialsService(ExchangeCredentialsRepository repository) {
        super(repository);
    }

}
