package com.georgefitzpatrick.trading.crypto.data.repository;

import com.georgefitzpatrick.trading.crypto.data.entity.Condition;
import com.georgefitzpatrick.trading.crypto.strategy.Indicator;
import com.georgefitzpatrick.trading.crypto.strategy.IndicatorRelationship;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author George Fitzpatrick
 */
@SpringBootTest
@SuppressWarnings("ClassCanBeRecord")
public class ConditionRepositoryIntegrationTest {

    /* ----- Fields ----- */

    private static final Condition.Action VALID_ACTION = Condition.Action.BUY;
    private static final int VALID_PERIOD = 2;
    private static final Indicator VALID_INDICATOR = Indicator.PRICE;
    private static final IndicatorRelationship VALID_RELATIONSHIP = IndicatorRelationship.CROSSES_OVER;

    private final ConditionRepository repository;

    /* ----- Constructors ----- */

    @Autowired
    public ConditionRepositoryIntegrationTest(ConditionRepository repository) {
        this.repository = repository;
    }

    /* ----- Methods ----- */

    @Test
    public void saveCondition_nullAction_throwsException() {
        Condition condition = new Condition();
        condition.setAction(null);
        condition.setBasePeriod(VALID_PERIOD);
        condition.setBaseIndicator(VALID_INDICATOR);
        condition.setRelationship(VALID_RELATIONSHIP);
        condition.setCounterPeriod(VALID_PERIOD);
        condition.setCounterIndicator(VALID_INDICATOR);
        Assertions.assertThrows(Exception.class, () -> repository.save(condition));
    }

    @Test
    public void saveCondition_nullBaseIndicator_throwsException() {
        Condition condition = new Condition();
        condition.setAction(VALID_ACTION);
        condition.setBasePeriod(VALID_PERIOD);
        condition.setBaseIndicator(null);
        condition.setRelationship(VALID_RELATIONSHIP);
        condition.setCounterPeriod(VALID_PERIOD);
        condition.setCounterIndicator(VALID_INDICATOR);
        Assertions.assertThrows(Exception.class, () -> repository.save(condition));
    }

    @Test
    public void saveCondition_nullRelationship_throwsException() {
        Condition condition = new Condition();
        condition.setAction(VALID_ACTION);
        condition.setBasePeriod(VALID_PERIOD);
        condition.setBaseIndicator(VALID_INDICATOR);
        condition.setRelationship(null);
        condition.setCounterPeriod(VALID_PERIOD);
        condition.setCounterIndicator(VALID_INDICATOR);
        Assertions.assertThrows(Exception.class, () -> repository.save(condition));
    }

    @Test
    public void saveCondition_nullCounterIndicator_throwsException() {
        Condition condition = new Condition();
        condition.setAction(VALID_ACTION);
        condition.setBasePeriod(VALID_PERIOD);
        condition.setBaseIndicator(VALID_INDICATOR);
        condition.setRelationship(VALID_RELATIONSHIP);
        condition.setCounterPeriod(VALID_PERIOD);
        condition.setCounterIndicator(null);
        Assertions.assertThrows(Exception.class, () -> repository.save(condition));
    }

    @Test
    public void saveCondition_requiredFields_doesNotThrowException() {
        Condition condition = new Condition();
        condition.setAction(VALID_ACTION);
        condition.setBasePeriod(VALID_PERIOD);
        condition.setBaseIndicator(VALID_INDICATOR);
        condition.setRelationship(VALID_RELATIONSHIP);
        condition.setCounterPeriod(VALID_PERIOD);
        condition.setCounterIndicator(VALID_INDICATOR);
        Assertions.assertDoesNotThrow(() -> repository.save(condition));
    }

}
