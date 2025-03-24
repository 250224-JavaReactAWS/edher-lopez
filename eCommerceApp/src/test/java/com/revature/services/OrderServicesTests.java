package com.revature.services;

import com.revature.models.CartItem;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.Status;
import com.revature.repos.CartDAO;
import com.revature.repos.OrderDAO;
import com.revature.repos.ProductDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Or;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class OrderServicesTests {
    private OrderServices orderServices;
    private OrderDAO mockedOrderDao;
    private CartDAO mockedCartDAO;
    private ProductDAO mockedProductDAO;
    private List<CartItem> mockedCart;
    private List<Product> mockedProductList;
    private final int testUserId = 1;
    @Before
    public void setUp() throws SQLException {
        mockedOrderDao = Mockito.mock(OrderDAO.class);
        mockedCartDAO = Mockito.mock(CartDAO.class);
        mockedProductDAO = Mockito.mock(ProductDAO.class);
        orderServices = new OrderServices(mockedCartDAO, mockedOrderDao, mockedProductDAO);
        mockedCart = new ArrayList<>();
        mockedCart.add(new CartItem(1,1, testUserId, 10));
        mockedCart.add(new CartItem(2,2, testUserId, 8));
        mockedCart.add(new CartItem(3,3, testUserId, 6));
        mockedProductList = new ArrayList<>();
        mockedProductList.add(new Product(1, "Samsung TV", "A TV", 15.5,10));
        mockedProductList.add(new Product(2, "LG TV", "A TV", 10.8,15));
        mockedProductList.add(new Product(3, "Sony TV", "A TV", 12.7,12));
        mockedProductList.add(new Product(4, "Samsung Fridge", "A TV", 11.4,8));
        mockedProductList.add(new Product(5, "Motorola Smartphone", "A smartphone", 12.5,1));
        when(mockedCartDAO.getByUserId(1)).thenReturn(mockedCart);
        when(mockedProductDAO.getByIdList(anyList())).thenAnswer(invocation -> {
            List<Integer> ids = invocation.getArgument(0);
            return mockedProductList.stream().filter(product -> ids.contains(product.getProductId())).toList();
        });
        when(mockedOrderDao.createWithOrderItems(any(Order.class), anyList())).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });
    }

    @Test
    public void placeOrderShouldReturnOrderWithGivenValues() throws SQLException{
        double expectedTotalPrice = 155 + 86.4 + 76.2;
        Order returnedOrder = orderServices.placeOrder(testUserId);
        Assert.assertEquals(expectedTotalPrice, returnedOrder.getTotalPrice(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void placeOrderShouldThrowAnExceptionWhenQuantityIsHigherThanStockedProducts() throws SQLException{
        mockedCart.add(new CartItem(4, 5, testUserId, 2));
        Order returnedOrder = orderServices.placeOrder(testUserId);
    }

    @Test
    public void updateOrderStatusShouldReturnOrderWithNewStatus(){
        Status newStatus = Status.SHIPPED;
        int orderId = 1;
        when(mockedOrderDao.getById(anyInt())).thenReturn(new Order(testUserId, 24.3));
        when(mockedOrderDao.update(any(Order.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });
        Order returnedOrder = orderServices.updateOrderStatus(orderId, newStatus);
        Assert.assertEquals(newStatus, returnedOrder.getStatus());
    }


}
