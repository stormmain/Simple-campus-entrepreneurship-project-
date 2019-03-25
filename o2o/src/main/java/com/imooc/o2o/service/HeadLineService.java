package com.imooc.o2o.service;

import java.io.IOException;
import java.util.List;

import com.imooc.o2o.entity.HeadLine;

public interface HeadLineService {

	public List<HeadLine> getHeadLineList(HeadLine 
			headLineConditio) throws IOException;
}
