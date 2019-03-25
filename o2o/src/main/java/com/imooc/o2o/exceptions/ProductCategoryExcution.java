package com.imooc.o2o.exceptions;

import com.imooc.o2o.enums.ProductCategoryStateEnum;

public class ProductCategoryExcution extends RuntimeException {

	private static final long serialVersionUID = -1933854879083021046L;

	public ProductCategoryExcution(ProductCategoryStateEnum success) {
		super();
	}
}
