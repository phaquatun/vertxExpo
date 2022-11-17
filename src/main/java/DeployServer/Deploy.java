package DeployServer;

import StandServer.SetupServer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Deploy extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        deployServer();
    }

    Future<String> deployServer() {
        DeploymentOptions options = new DeploymentOptions().setHa(true).setWorker(true)
                .setInstances(5)
                .setWorkerPoolSize(500);

        return Future.future((promise) -> vertx.deployVerticle("StandServer.SetupServer", options).result());
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle("DeployServer.Deploy");
    }

}
