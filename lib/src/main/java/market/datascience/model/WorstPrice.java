package market.datascience.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class WorstPrice implements Comparable {

    private double price;
    private LocalDate purchaseDate;
    private LocalDate breakEvenDate;

    public long getDaysToBreakEven(){
        return ChronoUnit.DAYS.between(purchaseDate, breakEvenDate);
    }

    @Override
    public int compareTo(Object o) {
        if(!o.getClass().isAssignableFrom(WorstPrice.class)){
            throw new RuntimeException("Comparing invalid object");
        }

        WorstPrice worstPriceOther = ((WorstPrice)o);

        return Long.compare(worstPriceOther.getDaysToBreakEven(), getDaysToBreakEven());
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getBreakEvenDate() {
        return breakEvenDate;
    }

    public void setBreakEvenDate(LocalDate breakEvenDate) {
        this.breakEvenDate = breakEvenDate;
    }

    @Override
    public String toString() {
        return "WorstPrice [price=" + price + ", purchaseDate=" + purchaseDate + ", breakEvenDate=" + breakEvenDate
                + "]";
    }

    
}
