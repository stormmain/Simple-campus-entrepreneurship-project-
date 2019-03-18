package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest{

	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	@Test
	public void testQueryShopCategory() {
		List<ShopCategory> ShopCategoryList=shopCategoryDao.queryShopCategory(new ShopCategory());
		assertEquals(13,ShopCategoryList.size());//18为数据存储的行数
		ShopCategory testCategory=new ShopCategory();
		ShopCategory parentCategory=new ShopCategory();
		parentCategory.setShopCategoryId(1L);
		testCategory.setParent(parentCategory);
		ShopCategoryList=shopCategoryDao.queryShopCategory(testCategory);
		assertEquals(1,ShopCategoryList.size());
		System.out.println(ShopCategoryList.get(0).getShopCategoryName());
	}
}
