package com.accenture.strategy.impl;

import com.accenture.model.Order;
import com.accenture.model.OrderLine;
import com.accenture.model.enums.OrderStatus;
import com.accenture.strategy.OrderStatusStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InvalidOrderAmountStrategy implements OrderStatusStrategy {

    @Override
    public boolean isApplicable(Order order) {
        if (order.getOrderLines() == null || order.getOrderLines().isEmpty()) {
            return false; // Si no hay líneas, esta validación no se aplica.
        }

        double calculatedAmount = 0.0;

        for (OrderLine line : order.getOrderLines()) {
            // Validar que la línea tenga cantidad y precio válidos
            if (line.getQuantity() <= 0) {
                return false; // Línea inválida
            }

            // Validar que el producto y su SKU existan
            if (line.getProduct() == null || line.getProduct().getSku() == null) {
                return false; // Producto inválido
            }

            // Acumular el monto total usando el subtotal de la línea
            calculatedAmount += line.getSubtotal();
        }

        // Comparar el monto calculado con el monto de la orden
        log.info("{} {}", order.getOrderAmount(), calculatedAmount);
        return Double.compare(order.getOrderAmount(), calculatedAmount) != 0;
    }


    @Override
    public OrderStatus getStatus() {
        return OrderStatus.INVALID_AMOUNT; // La orden falla si el monto no coincide con las líneas.
    }
}
