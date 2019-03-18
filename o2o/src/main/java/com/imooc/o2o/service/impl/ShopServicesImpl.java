package com.imooc.o2o.service.impl;

import java.io.InputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;

@Service
public class ShopServicesImpl implements ShopService{

	@Autowired
	private ShopDao shopDao;
	
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) {
		if(shop==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		
		try {
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			System.out.println("effectedNum出现错误");
			int effectedNum = shopDao.insertShop(shop);
			System.out.println("effectedNum其实没有出现错误啦!");
			
			if(effectedNum<=0) {
				throw new ShopOperationException("店铺创建失败");
			}else {
				if(shopImgInputStream!=null) {
					try {
						addShopImg(shop,shopImgInputStream,fileName);
					}catch(Exception e) {
						throw new ShopOperationException("addShopImg error:"+e.getMessage());
					}
					
					effectedNum=shopDao.updateShop(shop);
					if(effectedNum<=0) {
						throw new ShopOperationException("更新图片地址失败");
					}
				}else {
					System.out.println("shopImgInputStream==null");
				}
			}
		}catch(Exception e) {
//			throw new RuntimeException("addShop error:"+e.getMessage());
			e.printStackTrace();
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}

	private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
		// TODO Auto-generated method stub
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream, fileName, dest);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) {
		// TODO Auto-generated method stub
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName)
			throws ShopOperationException {
		// TODO Auto-generated method stub
		if(shop==null || shop.getShopId()==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else {
			try {
				if(shopImgInputStream!=null&&fileName!=null&&!"".equals(fileName)) {
					Shop tempShop=shopDao.queryByShopId(shop.getShopId());
					if(tempShop.getShopImg()!=null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					addShopImg(shop, shopImgInputStream, fileName);
				}
				
				shop.setLastEditTime(new Date());
				int effectedNum=shopDao.updateShop(shop);
				if(effectedNum<=0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				}else {
					shop=shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS,shop);
				}
			}catch(Exception e) {
				throw new ShopOperationException(e.getMessage());
			}
		}
	}
}
