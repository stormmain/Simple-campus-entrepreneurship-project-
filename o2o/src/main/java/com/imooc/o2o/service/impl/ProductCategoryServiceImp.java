package com.imooc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImp implements ProductCategoryService {

	@Autowired
	private ProductCategoryDao productCategoryDao;
	

	@Autowired
	private ProductDao productDao;
	
	@Override
	public List<ProductCategory> getProductCategoryList(Long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if(productCategoryList!=null && productCategoryList.size()>0) {
			try {
				int effectedNum=productCategoryDao.batchInsertProductCategory(productCategoryList);
				if(effectedNum<=0) {
					throw new ProductCategoryOperationException("店铺类别创建失败");
				}else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			}catch(Exception e) {
				throw new ProductCategoryOperationException("batchAddProductCategory err="+e.getMessage());
			}
		}else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		
		try {
			int effectNum=productDao.updateProductCategoryToNull(shopId);
			if(effectNum<=0) {
				throw new ProductCategoryOperationException("商品更新失败");
			}
		}catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
		}
		
		
		try {
			int effectNum=productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectNum<=0) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			}else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
		}
	}

	@Override
	@Transactional
	public List<ProductCategory> getByShopId(Long shopId) {
		// TODO Auto-generated method stub
		return productCategoryDao.queryByShopId(shopId);
	}
}
