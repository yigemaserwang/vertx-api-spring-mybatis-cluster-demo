package cn.monkey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;

import cn.monkey.vertx.ArticleDaoVerticle;
import cn.monkey.vertx.RestApiVerticle;
import cn.monkey.vertx.SpringVerticleFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Runner for the vertx-spring sample
 *
 */
@SpringBootApplication
@EnableAsync
public class VertXRunner {
	public static final Logger LOGGER = LoggerFactory.getLogger(VertXRunner.class);
	@Autowired
	SpringVerticleFactory verticleFactory;

	@Autowired
	Vertx vertx;
	
	@Value("${httpPort:#{null}}")
	Integer port;

	public static void main(String[] args) {
		SpringApplication.run(VertXRunner.class);
	}

	// @PostConstruct
	@EventListener
	public void deployServerVerticle(ApplicationReadyEvent event) {
//		VertxOptions options = new VertxOptions(); 
//		options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
//		Vertx vertx = Vertx.vertx(options);

		// Vertx vertx = Vertx.vertx((new VertxOptions()).setWorkerPoolSize(100));
		// Vertx vertx = Vertx.vertx((new VertxOptions()).setWorkerPoolSize(40));
		vertx.registerVerticleFactory(verticleFactory);
		String restApiVerticleName = verticleFactory.prefix() + ":" + RestApiVerticle.class.getName();
		if (port != null) {
			// deploy loop verticle
			JsonObject config = new JsonObject().put("port", port);
			DeploymentOptions loopDeploymentOptions = new DeploymentOptions().setWorker(false).setInstances(1).setConfig(config);
			vertx.deployVerticle(restApiVerticleName, loopDeploymentOptions);
		}
		// deploy worker verticle
		String workerVerticleName = verticleFactory.prefix() + ":" + ArticleDaoVerticle.class.getName();
		// DeploymentOptions workerDeploymentOptions = new
		// DeploymentOptions().setWorker(true);
		DeploymentOptions workerDeploymentOptions = new DeploymentOptions().setWorkerPoolSize(20).setWorker(true)
				.setInstances(16);
		vertx.deployVerticle(workerVerticleName, workerDeploymentOptions);
	}

}
