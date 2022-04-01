package com.georgefitzpatrick.trading.crypto.data.service;

import com.georgefitzpatrick.trading.crypto.data.SingletonIdentifiableEntityService;
import com.georgefitzpatrick.trading.crypto.data.entity.Accessibility;
import com.georgefitzpatrick.trading.crypto.data.repository.AccessibilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author George Fitzpatrick
 */
@Service
public class AccessibilityService extends SingletonIdentifiableEntityService<Accessibility> {

    @Autowired
    public AccessibilityService(AccessibilityRepository repository) {
        super(repository);
    }

}
