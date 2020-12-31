package chapter2.concurrentvssequential;

import java.util.Map;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SequentialNAV extends AbstractNAV {
    public double computeNetAssetValue(final Map<String, Integer> stocks) throws IOException {
        double netAssetValue = 0.0;
        for(String ticker : stocks.keySet()) {
            netAssetValue += stocks.get(ticker) * YahooFinanceService.getPrice(ticker);
        }
        return netAssetValue;
    }

    public static void main(final String[] args) throws ExecutionException, IOException, InterruptedException {
        new SequentialNAV().timeAndComputeValue();
    }
}