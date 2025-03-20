package com.revature.services;

import com.revature.models.*;
import com.revature.repos.CartDAO;
import com.revature.repos.OrderDAO;
import com.revature.repos.ProductDAO;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OrderServices {
    private OrderDAO orderRepository;
    private CartDAO cartRepository;
    private ProductDAO productRepository;

    public OrderServices(CartDAO cartRepository, OrderDAO orderRepository, ProductDAO productRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order placeOrder(int userId) {
        List<CartItem> cart = cartRepository.getByUserId(userId);
        List<OrderItem> orderItems = createOrderItemsList(cart);
        double totalPrice = calculateTotalPrice(orderItems);
        Order order = new Order(userId, totalPrice);
        Order savedOrder = orderRepository.createWithOrderItems(order, orderItems);
        if(savedOrder != null){
            cartRepository.deleteByUserId(userId);
            return savedOrder;
        }
        return null;
    }

    public List<Order> viewAllOrders(){
        return  orderRepository.getAll();
    }

    public List<Order> viewAllOrders(Status status){
        return  orderRepository.getByStatus(status);
    }

    public Order updateOrderStatus(int orderId, Status newStatus){
        Order storedOrder = orderRepository.getById(orderId);
        if(storedOrder != null){
            storedOrder.setStatus(newStatus);
            return orderRepository.update(storedOrder);
        }
        return null;
    }

    public List<Order> viewOrderHistory(int userId){
        return orderRepository.getByUserId(userId);
    }

    public Order viewOrder(int orderId, int userId){
        Order storedOrder = orderRepository.getById(orderId);
        if(storedOrder != null && storedOrder.getUserId() == userId){
            return storedOrder;
        }
        return null;
    }


    private List<OrderItem> createOrderItemsList(List<CartItem> cart){
        List<Product> products = getProductsFoundInCart(cart);
        List<OrderItem> orderItems = new LinkedList<>();
        for(CartItem c : cart){
            Product product = findProduct(c, products);
            if(c.getQuantity() > product.getStock()) {
                throw new IllegalArgumentException("You are trying to buy "
                        + c.getQuantity() + " products named " + product.getName()
                        + "but there are only " + product.getStock());
            }
            orderItems.add(new OrderItem(product.getProductId(), c.getQuantity(), product.getPrice()));
        }
        return orderItems;
    }

    private List<Product> getProductsFoundInCart(List<CartItem> cart){
        List<Integer> productIds = cart.stream().map(CartItem::getProductId).toList();
        return  productRepository.getByIdList(productIds);
    }

    private Product findProduct(CartItem cartItem, List<Product> products){
        Optional<Product> p = products.stream()
                .filter(product -> product.getProductId() == cartItem.getProductId())
                .findFirst();
        if(p.isEmpty()){
            throw new IllegalArgumentException("Product with id " + cartItem.getProductId() + " was not found");
        }
        return p.get();
    }

    private double calculateTotalPrice(List<OrderItem> orderItems){
        return orderItems.stream()
                .mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                .sum();
    }
}
