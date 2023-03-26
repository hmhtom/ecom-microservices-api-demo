package com.aheproject.order_service.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.aheproject.order_service.dto.InventoryResponse;
import com.aheproject.order_service.dto.OrderLineItemsDto;
import com.aheproject.order_service.dto.OrderRequest;
import com.aheproject.order_service.model.Order;
import com.aheproject.order_service.model.OrderLineItems;
import com.aheproject.order_service.repostories.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    // Inject order repository
    private final OrderRepository orderRepository;
    // Inject web client
    private final WebClient.Builder webClientBuilder;

    // Service - Create Order
    public String createOrder(OrderRequest orderRequest) {

        // Creating Order object from OrderRequest
        // ==========================================================================
        Order order = new Order();
        // Set order number to a random UUID string
        order.setOrderNumber(UUID.randomUUID().toString());

        // Create a list of OrderLineItems objects mapped from OrderLineItemsDto objects
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        // Set the list of OrderLineItems objects in the Order object
        order.setOrderLineItemsList(orderLineItems);
        // ==========================================================================

        // Call Inventory Service, and place order if product is in stock
        // ==========================================================================

        // Get a list of sku codes from the order
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        // Get InventoryResponse from Inventory Service
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        // Check if all elements in inventoryResponseArray are true
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            // If all product is in stock, place order
            orderRepository.save(order);
            return "Order created successfully";
        } else {
            // If product is not in stock, throw an exception
            throw new IllegalArgumentException("Product is not in stock, please try again later.");
        }
        // ==========================================================================

    }

    // Helper Method - Convert OrderLineItemsDto to OrderLineItems
    public OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());

        return orderLineItems;

    }

}
