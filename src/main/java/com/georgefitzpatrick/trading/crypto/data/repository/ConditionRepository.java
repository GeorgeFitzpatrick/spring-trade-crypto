package com.georgefitzpatrick.trading.crypto.data.repository;

import com.georgefitzpatrick.trading.crypto.data.entity.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author George Fitzpatrick
 */
@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {

}
