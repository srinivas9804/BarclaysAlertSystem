import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class RealTimeData extends Thread{

    ConcurrentHashMap<StockIds, BigDecimal> realTimePrices;
    public RealTimeData(ConcurrentHashMap<StockIds, BigDecimal> realTimePrices){
        this.realTimePrices = realTimePrices;
    }

    public void run(){
        while (true) {
            for (StockIds id : StockIds.values()) {
                //Math.random gives values between 0 and 1?
                BigDecimal value = new BigDecimal(Math.random() * 100);
                realTimePrices.put(id, value);
            }
            //still need to notify other thread of modified data. Or would need to implement a pub/sub
            synchronized (realTimePrices){
                realTimePrices.notify();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }
    }
}
