package practice2;

import java.math.BigDecimal;
import java.util.List;

public class Receipt {

    private BigDecimal tax;

    public Receipt() {
        tax = new BigDecimal(0.1);
        tax = tax.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public double CalculateGrandTotal(List<Product> products, List<OrderItem> items) {
        BigDecimal subTotal = calculateSubtotal(products, items);

        subTotal = SubtractDiscounts(products, items, subTotal);

        BigDecimal grandTotal = AddTax(subTotal);

        return convertTotalToDoubleType(grandTotal);
    }

    private double convertTotalToDoubleType(BigDecimal grandTotal) {
        return grandTotal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private BigDecimal AddTax(BigDecimal subTotal) {
        BigDecimal taxTotal = subTotal.multiply(tax);

        return subTotal.add(taxTotal);
    }

    private BigDecimal SubtractDiscounts(List<Product> products, List<OrderItem> items, BigDecimal subTotal) {
        for (Product product : products) {
            OrderItem curItem = findOrderItemByProduct(items, product);
            BigDecimal reducedPrice = caculateProductDiscounts(product, curItem);
            subTotal = subTotal.subtract(reducedPrice);
        }
        return subTotal;
    }

    private OrderItem findOrderItemByProduct(List<OrderItem> items, Product product) {
        OrderItem curItem = null;
        for (OrderItem item : items) {
            if (item.getCode() == product.getCode()) {
                curItem = item;
                break;
            }
        }
        return curItem;
    }

    private BigDecimal caculateProductDiscounts(Product product, OrderItem item) {
        return product.getPrice()
                .multiply(product.getDiscountRate())
                .multiply(new BigDecimal(item.getCount()));
    }

    private BigDecimal calculateSubtotal(List<Product> products, List<OrderItem> items) {
        BigDecimal subTotal = new BigDecimal(0);
        for (Product product : products) {
            OrderItem item = findOrderItemByProduct(items, product);
            BigDecimal itemTotal = caculateItemTotal(product, item);
            subTotal = subTotal.add(itemTotal);
        }
        return subTotal;
    }

    private BigDecimal caculateItemTotal(Product product, OrderItem item) {
        return product.getPrice().multiply(new BigDecimal(item.getCount()));
    }
}
