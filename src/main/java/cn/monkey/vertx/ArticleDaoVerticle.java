package cn.monkey.vertx;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

import cn.monkey.model.TblArticle;
import cn.monkey.service.AsyncArticleService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArticleDaoVerticle extends AbstractVerticle {
	public static final Logger LOGGER = LoggerFactory.getLogger(ArticleDaoVerticle.class);
	
	private static final String ID = UUID.randomUUID().toString();
	
	@Autowired
	private AsyncArticleService service;
	
	private final  HazelcastInstance hazelcastInstance;
	
	public ArticleDaoVerticle(HazelcastInstance hazelcastInstance) {
		//暂时没啥用，主要是用来自定义内存交换数据的
		this.hazelcastInstance = hazelcastInstance;
	}

	static AtomicInteger DataNoOp = new AtomicInteger(0);

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		vertx.eventBus().consumer("dao://article/find", this::find);
		vertx.eventBus().consumer("dao://article/add", this::add);
		vertx.eventBus().consumer("dao://article/findAll", this::findAll);
		
		//IQueue<String> names = hazelcastInstance.getQueue("name");
	}

	public void add(Message<JsonObject> msg) {
		// LOGGER.info("执行 dao add方法");
		LOGGER.info("DataNoOp add " + DataNoOp.incrementAndGet());
		String title = msg.body().getString("fld_title", "标题");
		String content = msg.body().getString("fld_content", "内容");
		TblArticle tblArticle = new TblArticle();
		tblArticle.setFld_title(title);
		tblArticle.setFld_content(content);
		service.insertArticle(tblArticle, aHandler -> {
			if (aHandler.succeeded()) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.put("id", aHandler.result());
				msg.reply(jsonObject);
			} else {
				msg.reply(aHandler.cause());
			}
		});
	}

	public void find(Message<JsonObject> msg) {
		LOGGER.info("DataNoOp find " + DataNoOp.incrementAndGet());
		String id = msg.body().getString("id");
		service.findArticleById(Integer.parseInt(id), ar -> {
			if (ar.succeeded()) {
				TblArticle tblArticle = ar.result();
				msg.reply(JsonObject.mapFrom(tblArticle));
			} else {
				msg.reply("get from db error");
			}
		});

	}

	public void findAll(Message msg) {
		LOGGER.info("DataNoOp findAll " + DataNoOp.incrementAndGet());
		service.findAll(ar -> {
			if (ar.succeeded()) {
				//List<TblArticle> result = ar.result();
				//msg.reply(new JsonArray(result));
				msg.reply(new JsonArray(ar.result().stream().map(r -> r == null ? null : r.toJson()).collect(Collectors.toList())));
			} else {
				msg.reply("findall from db error");
			}
		}

		);
	}
}