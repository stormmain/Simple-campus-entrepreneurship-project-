package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Product;

public interface ProductDao {

	List<Product> queryProductList(@Param("productCondition") Product productCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	Product queryProductById(long productId);
	
	int insertProduct(Product product);
	
	int updateProduct(Product product);
	
	int updateProductCategoryToNull(long productCategoryId);
	
	int deleteProduct(long productId);

	int queryProductCount(@Param("productCondition") Product product);
}
