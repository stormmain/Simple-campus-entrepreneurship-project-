package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.ProductCategory;

public interface ProductCategoryDao {

	List<ProductCategory> queryProductCategoryList(long shopId);
	
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	int deleteProductCategory(@Param("productCategoryId") long ProductCategoryId,
			@Param("shopId") long shopId);

	List<ProductCategory> queryByShopId(Long shopId);
}
