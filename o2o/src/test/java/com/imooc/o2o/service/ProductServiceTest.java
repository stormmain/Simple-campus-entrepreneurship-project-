package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;

public class ProductServiceTest extends BaseTest{

	@Autowired
	private ProductService productService;
	
	@Test
	public void testModifyProduct() throws Exception{
		Product product=new Product();
		Shop shop=new Shop();
		shop.setShopId(1L);
		ProductCategory pc=new ProductCategory();
		pc.setProductCategoryId(1L);
		product.setProductId(1L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("正式的商品");
		product.setProductDesc("正式的商品");
		File thumbnailFile=new File("/Users/lenovo/Desktop/a.jpg");
		InputStream is=new FileInputStream(thumbnailFile);
		ImageHolder thumbnail=new ImageHolder(thumbnailFile.getName(), is);
		
		File productImg1=new File("/Users/lenovo/Desktop/a.jpg");
		InputStream is1=new FileInputStream(productImg1);
		File productImg2=new File("/Users/lenovo/Desktop/a.jpg");
		InputStream is2=new FileInputStream(productImg2);
		
		List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(),is1));
		productImgList.add(new ImageHolder(productImg2.getName(),is2));
		
		ProductExecution pe=productService.modifyProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
		
		
		
	}
	
	@Test
	@Ignore
	public void testAddProduct() throws Exception{
		try {
			Product product = new Product();
			Shop shop = new Shop();
			shop.setShopId(1L);
			ProductCategory pc = new ProductCategory();
			pc.setProductCategoryId(1L);
			product.setShop(shop);
			product.setProductCategory(pc);
			product.setProductName("测试商品1");
			product.setProductDesc("测试商品");
			product.setPriority(20);
			product.setCreateTime(new Date());
			product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
			File thumbnailFile = new File("/Users/lenovo/Desktop/a.jpg");
			FileInputStream is = new FileInputStream(thumbnailFile);
			ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
			File productImg1 = new File("/Users/lenovo/Desktop/a.jpg");
			InputStream is1 = new FileInputStream(productImg1);
			File productImg2 = new File("/Users/lenovo/Desktop/a.jpg");
			InputStream is2 = new FileInputStream(productImg2);
			List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
			productImgList.add(new ImageHolder(productImg1.getName(), is1));
			productImgList.add(new ImageHolder(productImg2.getName(), is2));
			
			//这里之后出错是因为没有 extends BaseTest，这个错误没有多少提示，仅能凭着感觉
			ProductExecution pe=productService.addProduct(product, thumbnail, productImgList);
			
			System.out.println("有错误吗？");
			assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());

			System.out.println("没有错误");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("确认有错！");
			e.printStackTrace();
		}
	}
}
