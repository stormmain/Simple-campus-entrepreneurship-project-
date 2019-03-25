package com.imooc.o2o.web.shopadmin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.Result;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;

@Controller
@RequestMapping(value="shopadmin",method= {RequestMethod.GET})
public class ProductCategoryManagementController {

	@Autowired
	private ProductCategoryService productCategoryService; 
	
	@RequestMapping(value="/getproductcategorylist",method=RequestMethod.GET)
	@ResponseBody
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){
		Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> list=null;
		if(currentShop!=null && currentShop.getShopId()>0) {
			list=productCategoryService.getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true,list);
		}else {
			ProductCategoryStateEnum ps=ProductCategoryStateEnum.INNER_ERROR;
			return new Result<List<ProductCategory>>(false,ps.getState(),ps.getStateInfo());
		}
	}
	
	@RequestMapping(value="/addproductcategorys",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProductCateogrys(@RequestBody List<ProductCategory> 
		productCategoryList,HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String, Object>();
		Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
		for(ProductCategory pc:productCategoryList) {
			pc.setShopId(currentShop.getShopId());
		}
		
		if(productCategoryList!=null && productCategoryList.size()!=0) {
			try {
				ProductCategoryExecution pe=productCategoryService.batchAddProductCategory(productCategoryList);
				if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch(RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个商品类别");
		}
		
		return modelMap;
	}
	
	@RequestMapping(value="/removeproductcategory",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> removeProductCategory(Long productCategoryId,HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<String, Object>();
		if(productCategoryId!=null && productCategoryId>0) {
			try {
				Shop currentShop=(Shop) request.getSession().getAttribute("ShopId");
				ProductCategoryExecution pe=productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
				if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			}catch(ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少选择一个商品类别");
		}
		
		return modelMap;
	}
}
