package cn.monkey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import cn.monkey.dao.ArticleMapper;
import cn.monkey.model.TblArticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
//import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;


@Service
public class ArticleService {

	@Autowired
	private ArticleMapper articleDAO;
	
	
	public String getCurrentDateTime(){
		return articleDAO.getCurrentDateTime();
	}
	
	public int insertArticle(TblArticle tblArticle){
		return articleDAO.insertArticle(tblArticle);
	}
	
	
	public TblArticle findArticleById(int id){
		return articleDAO.findArticleById(id);
	}

	public List<TblArticle> findAll(){
		return articleDAO.findAll();
	}
}
