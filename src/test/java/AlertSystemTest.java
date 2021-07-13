import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class AlertSystemTest {

    @Test
    @DisplayName("Check if current price greater than closing price")
    public void priceDifferenceTest(){
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal hundred = new BigDecimal("100");

        String errorMessage = "Price difference not working as expected";

        assertTrue(AlertSystem.priceDifference(hundred,zero), errorMessage);
        assertFalse(AlertSystem.priceDifference(zero,hundred), errorMessage);
    }

    @Test
    @DisplayName("Checks for output Message")
    public void checkPricesTest(){
        HashMap<StockIds, BigDecimal> closingPrices = new HashMap<>();
        closingPrices.put(StockIds.CITI,BigDecimal.ZERO);
        ConcurrentHashMap<StockIds, BigDecimal> realTimePrices = new ConcurrentHashMap<>();
        realTimePrices.put(StockIds.CITI,BigDecimal.TEN);

        String expectedOut = "Stock id = " + StockIds.CITI + "\tClosing Price = 0.00\tRealTime Price = 10.00\n";
        String out = AlertSystem.checkPrices(closingPrices, realTimePrices);

        assertEquals(out,expectedOut, "Not working when realTime price is larger than closing price");

        closingPrices.put(StockIds.CITI, new BigDecimal("100"));
        out = AlertSystem.checkPrices(closingPrices, realTimePrices);
        assertEquals(out,"", "Not working when realTime price is smaller than closing price");
    }
}
