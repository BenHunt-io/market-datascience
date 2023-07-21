package market.datascience.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class YoYReturn {

    private LocalDate from;
    private LocalDate to;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private BigDecimal yoyReturn;

    public YoYReturn(LocalDate from, LocalDate to, double fromPrice, double toPrice, double yoyReturn) {
        this.from = from;
        this.to = to;
        this.fromPrice = toBigDecimal(fromPrice);
        this.toPrice = toBigDecimal(toPrice);
        this.yoyReturn = toBigDecimal(yoyReturn);
    }

    public BigDecimal getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(BigDecimal fromPrice) {
        this.fromPrice = fromPrice;
    }

    public BigDecimal getToPrice() {
        return toPrice;
    }

    public void setToPrice(BigDecimal toPrice) {
        this.toPrice = toPrice;
    }

    private BigDecimal toBigDecimal(double value){
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_DOWN);
    }

    public LocalDate getFrom() {
        return from;
    }
    public void setFrom(LocalDate fromDate) {
        this.from = fromDate;
    }
    public LocalDate getTo() {
        return to;
    }
    public void setTo(LocalDate toDate) {
        this.to = toDate;
    }
    public BigDecimal getYoyReturn() {
        return yoyReturn;
    }
    public void setYoyReturn(BigDecimal yoyReturn) {
        this.yoyReturn = yoyReturn;
    }
    @Override
    public String toString() {
        return "YoYReturn [from=" + from + ", to=" + to + ", yoyReturn=" + yoyReturn + "]";
    }
}
