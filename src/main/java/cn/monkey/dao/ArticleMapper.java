package cn.monkey.dao;

import java.util.List;

import cn.monkey.model.TblArticle;

public interface ArticleMapper {
	String getCurrentDateTime();
	Integer insertArticle(TblArticle tblArticle);
	TblArticle findArticleById(int id);
	List<TblArticle> findAll();
}
