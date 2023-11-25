package com.v2p.swp391.config;

import com.v2p.swp391.application.repository.BookingRepository;
import com.v2p.swp391.application.repository.OrderRepository;
import com.v2p.swp391.shipment.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StrategyConfig {
    @Bean
    public CreateOrder createOrderStrategy(OrderRepository orderRepository) {
        return new CreateOrder(orderRepository);
    }

    @Bean
    public CreateBooking createBookingStrategy(BookingRepository bookingRepository) {
        return new CreateBooking(bookingRepository);
    }

    @Bean
    public Map<CreateStrategyType, CreateOrderStrategy> createOrderStrategyMap(
            CreateOrder createOrderStrategy,
            CreateBooking createBookingStrategy
    ) {
        Map<CreateStrategyType, CreateOrderStrategy> strategyMap = new HashMap<>();
        strategyMap.put(CreateStrategyType.ORDER, createOrderStrategy);
        strategyMap.put(CreateStrategyType.BOOKING, createBookingStrategy);
        return strategyMap;
    }

    @Bean
    public ShipmentService shipmentService(Map<CreateStrategyType, CreateOrderStrategy> strategies) {
        return new ShipmentImpl(strategies);
    }
}
