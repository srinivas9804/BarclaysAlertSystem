import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AlertSystem {

    public static void main(String args[]){
        HashMap<StockIds, BigDecimal> closingPrices = new HashMap<>();
        closingPrices.put(StockIds.HSBC,new BigDecimal("25"));
        closingPrices.put(StockIds.CITI,new BigDecimal("50"));
        closingPrices.put(StockIds.VODAFONE,new BigDecimal("75"));
        ConcurrentHashMap<StockIds, BigDecimal> realTimePrices = new ConcurrentHashMap<>();

        RealTimeData realTimeData = new RealTimeData(realTimePrices);
        realTimeData.start();

        while(true){
            System.out.println(checkPrices(closingPrices, realTimePrices));
            synchronized (realTimePrices) {
                try {
                    realTimePrices.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String checkPrices(HashMap<StockIds, BigDecimal> closingPrices, ConcurrentHashMap<StockIds, BigDecimal> realTimePrices){
        StringBuilder out = new StringBuilder();
        synchronized (realTimePrices) {
            for (Map.Entry<StockIds, BigDecimal> entry : realTimePrices.entrySet()) {
                StockIds id = entry.getKey();
                BigDecimal currentPrice = entry.getValue();
                BigDecimal closingPrice = closingPrices.get(id);
                if(priceDifference(currentPrice, closingPrice)){
                    out.append(String.format("Stock id = %s\tClosing Price = %.2f\tRealTime Price = %.2f\n", id.toString(), closingPrice, currentPrice));
                }
            }
        }
        return out.toString();
    }

    public static boolean priceDifference(BigDecimal currentPrice, BigDecimal closingPrice){
        if(currentPrice != null && closingPrice != null && currentPrice.compareTo(closingPrice) > 0)
            return true;
        return false;
    }

}
