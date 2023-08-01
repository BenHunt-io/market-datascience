package market.datascience.model;

import java.time.LocalDate;

public class LocalDatePrice implements Comparable {

    private double price;
    private LocalDate localDate;

    public LocalDatePrice(double price, LocalDate localDate) {
        this.price = price;
        this.localDate = localDate;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }
    public void setLocalDate(LocalDate date) {
        this.localDate = date;
    }

    @Override
    public int compareTo(Object otherObj) {
        if(!otherObj.getClass().isAssignableFrom(LocalDatePrice.class)){
            throw new RuntimeException("Comparing invalid object");
        }

        double priceOther = ((LocalDatePrice)otherObj).getPrice();
        return Double.compare(price, priceOther);
    }
}
