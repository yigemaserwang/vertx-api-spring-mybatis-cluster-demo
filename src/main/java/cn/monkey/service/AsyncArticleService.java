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
public class AsyncArticleService {

	@Autowired
	private ArticleService articleService;

	public void insertArticle(TblArticle tblArticle,Handler<AsyncResult<Integer>> resultHandle) {
		Integer back = articleService.insertArticle(tblArticle);
		Future.succeededFuture(back).setHandler(resultHandle);
	}

	public void findArticleById(int id, Handler<AsyncResult<TblArticle>> resultHandle) {
		TblArticle art = articleService.findArticleById(id);
		Future.succeededFuture(art).setHandler(resultHandle);
	}

	public void findAll(Handler<AsyncResult<List<TblArticle>>> resultHandle) {
		List<TblArticle> all = articleService.findAll();
		Future.succeededFuture(all).setHandler(resultHandle);
	}
}
