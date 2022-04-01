package com.georgefitzpatrick.trading.crypto.data.repository;

import com.georgefitzpatrick.trading.crypto.data.entity.Accessibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author George Fitzpatrick
 */
@Repository
public interface AccessibilityRepository extends JpaRepository<Accessibility, Long> {

}
