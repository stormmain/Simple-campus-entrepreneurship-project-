package com.imooc.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import com.imooc.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	
//	public static void main(String[] args) throws IOException {
//		String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
//		System.out.println(basePath+"fengjing.jpg");
//		Thumbnails.of(new File("/Users/lenovo/Desktop/a.jpg")).size(200, 200)
//		.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/fengjing.jpg")),0.25f)
//		.outputQuality(0.8f).toFile("/Users/lenovo/Desktop/anew.jpg");
//		
//	}
	
	
	
	
	private static String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random random=new Random();
	
	public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
		String realFileName = getRandomFileName();
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		
		
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200)
			.watermark(Positions.BOTTOM_LEFT,ImageIO.read(new File(basePath+"/fengjing.jpg")),0.25f)
			.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

	public static String getRandomFileName() {
		int rannum=random.nextInt(89999)+10000;
		String nowTimeStr=simpleDateFormat.format(new Date());
		return nowTimeStr+rannum;
	}
	

	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
	
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	} 
	
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath=new File(PathUtil.getImgBasePath()+storePath);
		if(fileOrPath.exists()) {
			if(fileOrPath.isDirectory()) {
				File files[]=fileOrPath.listFiles();
				for(int i=0; i<files.length; i++) {
					files[i].delete();
				}
			}
			
			fileOrPath.delete();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
