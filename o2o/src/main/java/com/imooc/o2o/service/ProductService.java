package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exceptions.ProductOperationException;

public interface ProductService {

	Product getProductById(long productId);

	ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgs)
			throws ProductOperationException;

	ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgs) throws RuntimeException;
	
	ProductExecution getProductList(Product productId, int pageIndex, int pageSize);
}
