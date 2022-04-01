package com.georgefitzpatrick.trading.crypto.data.repository;

import com.georgefitzpatrick.trading.crypto.data.entity.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author George Fitzpatrick
 */
@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

}
