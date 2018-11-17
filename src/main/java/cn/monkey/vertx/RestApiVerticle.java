/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package cn.monkey.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.net.HttpURLConnection.*;

/**
 * A standard verticle, 
 *
 */
@Component
public class RestApiVerticle extends AbstractVerticle {
	private static final Logger LOG = LoggerFactory.getLogger(RestApiVerticle.class);

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		router.post("/article/find").handler(this::find);
		router.post("/article/add").handler(this::add);
		router.get("/article/findAll").handler(this::findAll);

		StaticHandler staticHandler = StaticHandler.create();
		router.route().handler(staticHandler);

		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("port"), listen -> {
			if (listen.succeeded()) {
				LOG.info("ApiVerticle started");
				startFuture.complete();
			} else {
				startFuture.fail(listen.cause());
			}
		});
	}

	public void find(RoutingContext context) {
		JsonObject body = new JsonObject();
		body.put("id", context.request().getParam("id"));
		context.vertx().eventBus().<JsonObject>send("dao://article/find", body, reply -> {
			if (reply.succeeded()) {
				context.response().putHeader("content-type", "text/plain;charset=utf-8");
				context.response().end(reply.result().body().toBuffer());
			} else {
				LOG.info(reply.cause().getMessage());
				// 异常处理
			}

		});

	}

	public void add(RoutingContext context) {
		JsonObject body = new JsonObject();
		body.put("fld_title", context.request().getParam("fld_title"));
		body.put("fld_content", context.request().getParam("fld_content"));
		context.vertx().eventBus().<JsonObject>send("dao://article/add", body, reply -> {

			if (reply.succeeded()) {

				context.response().putHeader("content-type", "text/plain;charset=utf-8");
				context.response().end(reply.result().body().toBuffer());
			} else {
				context.response().putHeader("Content-Length", "-1");
				context.response().setStatusCode(500).write(reply.cause().toString()).end();
				LOG.info(reply.cause().getMessage());
				// 异常处理
			}
		});

	}

	public void findAll(RoutingContext context) {
		JsonObject body = new JsonObject();

		context.vertx().eventBus().<JsonObject>send("dao://article/findAll", body, reply -> {
			if (reply.succeeded()) {
				context.response().putHeader("content-type", "text/plain;charset=utf-8");
				String haha = "" + reply.result().body();
				// System.out.println("ss="+reply.result().body().toString());
				// routingContext.response().end(reply.result().body().toString());
				context.response().end(haha);
			} else {
				// System.out.println("failed="+reply.failed());
				LOG.info(reply.cause().getMessage());
				// 异常处理
			}
		});

	}
}
