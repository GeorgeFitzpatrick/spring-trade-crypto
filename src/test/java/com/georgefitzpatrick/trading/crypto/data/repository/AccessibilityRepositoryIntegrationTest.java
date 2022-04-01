package com.georgefitzpatrick.trading.crypto.data.repository;

import com.georgefitzpatrick.trading.crypto.data.entity.Accessibility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

/**
 * @author George Fitzpatrick
 */
@SpringBootTest
@SuppressWarnings("ClassCanBeRecord")
public class    AccessibilityRepositoryIntegrationTest {

    /* ----- Fields ----- */

    private static final Locale VALID_LOCALE = Locale.ENGLISH;

    private final AccessibilityRepository repository;

    /* ----- Constructors ----- */

    @Autowired
    public AccessibilityRepositoryIntegrationTest(AccessibilityRepository repository) {
        this.repository = repository;
    }

    /* ----- Methods ----- */

    @Test
    public void saveAccessibility_nullLocale_throwsException() {
        Accessibility accessibility = new Accessibility();
        accessibility.setLocale(null);
        Assertions.assertThrows(Exception.class, () -> repository.save(accessibility));
    }

    @Test
    public void saveAccessibility_requiredFields_doesNotThrowException() {
        Accessibility accessibility = new Accessibility();
        accessibility.setLocale(VALID_LOCALE);
        Assertions.assertDoesNotThrow(() -> repository.save(accessibility));
    }

}
