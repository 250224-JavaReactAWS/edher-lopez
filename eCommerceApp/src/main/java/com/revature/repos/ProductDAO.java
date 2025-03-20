package com.revature.repos;

import com.revature.models.Product;

import java.util.List;

public interface ProductDAO extends GeneralDAO<Product> {
    List<Product> getByIdList(List<Integer> idList);
}
