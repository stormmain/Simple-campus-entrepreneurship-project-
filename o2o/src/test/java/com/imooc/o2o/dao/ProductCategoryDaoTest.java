package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {

	@Autowired
	private ProductCategoryDao productCategoyrDao;
	
	@Test
	public void testBQueryByShopId() throws Exception{
		long shopId=15;
		List<ProductCategory> productCategoryList=productCategoyrDao.queryProductCategoryList(shopId);
		System.out.println("该店铺自定义类别数为："+productCategoryList.size());
	}
	
	@Test
	public void testABatchInsertProductCategory() {
		ProductCategory productCategory=new ProductCategory();
		productCategory.setProductCategoryName("商品类别");
		productCategory.setPriority(1);
		productCategory.setCreateTime(new Date());
		productCategory.setShopId(1L);
		ProductCategory productCategory2=new ProductCategory();
		productCategory2.setProductCategoryName("商品类别2");
		productCategory2.setPriority(2);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(1L);
		List<ProductCategory> productCategories=new ArrayList<ProductCategory>();
		productCategories.add(productCategory);
		productCategories.add(productCategory2);
		int effectedNum=productCategoyrDao.batchInsertProductCategory(productCategories);
		assertEquals(2, effectedNum);
	}
	
	@Test
	public void testCDeleteProductCategory() throws Exception{
		long shopId=1;
		List<ProductCategory> productCategories=productCategoyrDao.queryProductCategoryList(shopId);
		for(ProductCategory pc:productCategories) {
			if("商品类别1".equals(pc.getProductCategoryName()) || "商品类别2".equals(pc.getProductCategoryName())){
				int effectedNum=productCategoyrDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
				assertEquals(1, effectedNum);
			}
		}
	}
	
	
}
