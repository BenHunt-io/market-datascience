package market.datascience.model;

import java.time.LocalDate;

import com.ib.client.Decimal;

public class Bar {

    private LocalDate localDate;
    private double open;
    private double high;
    private double low;
    private double close;
    private Decimal volume;
    private int count;
    private Decimal wap;

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

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
    
    
}
