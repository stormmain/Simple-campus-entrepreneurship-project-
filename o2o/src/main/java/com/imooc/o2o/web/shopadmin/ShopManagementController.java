package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodelUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap=new HashMap<>();
		Long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId>-1) {
			try {
				Shop shop=shopService.getByShopId(shopId);
				List<Area> areaList=areaService.getAreaList();
				modelMap.put("shop",shop);
				modelMap.put("areaList",areaList);
				modelMap.put("success", true);
			}catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "enpty shopId");
		}
		
		return modelMap;
	}
	
	
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){
		Map<String,Object> modelMap=new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList=new ArrayList<ShopCategory>();
		
		List<Area> areaList=new ArrayList<Area>();
		try {
			shopCategoryList=shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList=areaService.getAreaList();
			
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		
		return modelMap;
	}
	
	@RequestMapping(value="/registershop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request){
		 Map<String,Object> modelMap=new HashMap<String, Object>();
		 
		 if(!CodelUtil.checkVerigyCode(request)) {
			 modelMap.put("success", false);
			 modelMap.put("errMsg", "输入错误的验证码");
			 return modelMap;
		 }
		 
		 String shopStr=HttpServletRequestUtil.getString(request, "shopStr");
		 ObjectMapper mapper=new ObjectMapper();
		 Shop shop=null;
		 
		 try {
			 System.out.println("2");
			 shop=mapper.readValue(shopStr, Shop.class);
		 }catch(Exception e) {
			 modelMap.put("success", false);
			 modelMap.put("errMsg", e.getMessage());
			 e.printStackTrace();
			 return modelMap;
		 }

		 System.out.println("3");
		 CommonsMultipartFile shopImg=null;
		 CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(
				 request.getSession().getServletContext());
		 
		 if(commonsMultipartResolver.isMultipart(request)) {
			 MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
			 shopImg=(CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		 }else {
			 System.out.println("4");
			 modelMap.put("success", false);
			 modelMap.put("errMsg", "上传图片不能为空");
			 return modelMap;
		 }
		 
//		 注册店铺
		 System.out.println("5");
		 if(shop!=null && shopImg!=null) {
			 PersonInfo owner=(PersonInfo) request.getSession().getAttribute("user");
			 shop.setOwner(owner);
			 System.out.println("6");
//			 File shopImgFile=new File(PathUtil.getImgBasePath()+ImageUtil.getRandomFileName());
//			 
//			try {
//				 System.out.println("6");
//				shopImgFile.createNewFile();
//			} catch (IOException e) {
//				modelMap.put("success", false);
//				modelMap.put("errMsg", e.getMessage());
//				e.printStackTrace();
//			}
			 
//			try {
//				System.out.println("7");
//				inputStreamToFile(shopImg.getInputStream(),shopImgFile);
//			} catch (IOException e) {
//				modelMap.put("success", false);
//				modelMap.put("errMsg", e.getMessage());
//				return modelMap;
//			}
			 ShopExecution se;
			try {
				System.out.println("7");
				se = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
				if(se.getState()==ShopStateEnum.CHECK.getState()) {
					 modelMap.put("success", true);
					 @SuppressWarnings("unchecked")
					List<Shop> shopList=(List<Shop>) request.getSession().getAttribute("shopList");
					 if(shopList==null||shopList.size()==0) {
						 shopList=new ArrayList<Shop>();
					 }
					 
					 shopList.add(se.getShop());
					 request.getSession().setAttribute("shopList", shopList);
					 
				}else {
					 System.out.println("8");
					 modelMap.put("success", false);
					 modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			 
			 
			 return modelMap; 
		 }else {
			 System.out.println("9");
			 modelMap.put("success", false);
			 modelMap.put("errMsg", "请输入店铺信息");
			 return modelMap;
		 }
	}
	 
	@RequestMapping(value="/modifyshop",method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request){
		 Map<String,Object> modelMap=new HashMap<String, Object>();
		 
		 if(!CodelUtil.checkVerigyCode(request)) {
			 modelMap.put("success", false);
			 modelMap.put("errMsg", "输入错误的验证码");
			 return modelMap;
		 }
		 
		 String shopStr=HttpServletRequestUtil.getString(request, "shopStr");
		 ObjectMapper mapper=new ObjectMapper();
		 Shop shop=null;
		 
		 try {
			 System.out.println("2");
			 shop=mapper.readValue(shopStr, Shop.class);
		 }catch(Exception e) {
			 modelMap.put("success", false);
			 modelMap.put("errMsg", e.getMessage());
			 e.printStackTrace();
			 return modelMap;
		 }

		 System.out.println("3");
		 CommonsMultipartFile shopImg=null;
		 CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(
				 request.getSession().getServletContext());
		 
		 if(commonsMultipartResolver.isMultipart(request)) {
			 MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
			 shopImg=(CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		 }
		 
//		 修改店铺
		 System.out.println("5");
		 if(shop!=null && shop.getShopId()!=null) {
			 ShopExecution se;
			try {
				System.out.println("7");
				if(shopImg==null) {
					se = shopService.modifyShop(shop, null, null);
				}else {
					se = shopService.modifyShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
				}
				
				if(se.getState()==ShopStateEnum.SUCCESS.getState()) {
					 modelMap.put("success", true);
				}else {
					 System.out.println("8");
					 modelMap.put("success", false);
					 modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			 
			 
			 return modelMap; 
		 }else {
			 System.out.println("9");
			 modelMap.put("success", false);
			 modelMap.put("errMsg", "请输入店铺信息id");
			 return modelMap;
		 }
	}
	
//	 private static void inputStreamToFile(InputStream ins, File file) {
//		 FileOutputStream os=null;
//		 try {
//			 os=new FileOutputStream(file);
//			 int bytesRead=0;
//			 byte[] buffer=new byte[1024];
//			 while((bytesRead=ins.read(buffer))!=-1) {
//				 os.write(buffer,0,bytesRead);
//			 }
//		 }catch(Exception e) {
//			 throw new RuntimeException("调用inputStreamToFile产生异常"+e.getMessage());
//		 }finally {
//			 try {
//				 if(os!=null) {
//					 os.close();
//				 }
//				 
//				 if(ins!=null) {
//					 ins.close();
//				 }
//			 }catch(IOException e) {
//				 throw new RuntimeException("inputStreamToFile关闭close产生异常"+e.getMessage());
//			 }
//		 }
//	 }
}
