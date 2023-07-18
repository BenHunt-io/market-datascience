package market.interactive_brokers.client;

import java.time.LocalDateTime;

import com.ib.client.Decimal;

public class Bar {

    private LocalDateTime localDateTime;
    private double open;
    private double high;
    private double low;
    private double close;
    private Decimal volume;
    private int count;
    private Decimal wap;

    public Bar(com.ib.client.Bar bar) {
        this.localDateTime = LocalDateTime.parse(bar.time(), ApiConsumer.LOCAL_DATE_TIME_FORMAT);
        this.open = bar.open();
        this.high = bar.high();
        this.low = bar.low();
        this.close = bar.close();
        this.volume = bar.volume();
        this.count = bar.count();
        this.wap = bar.wap();
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public Decimal getVolume() {
        return volume;
    }

    public void setVolume(Decimal volume) {
        this.volume = volume;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Decimal getWap() {
        return wap;
    }

    public void setWap(Decimal wap) {
        this.wap = wap;
    }
    
}
