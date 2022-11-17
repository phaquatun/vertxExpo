package StandServer;

import AdminServer.Admin;
import DataBaseServer.DataAdmin;
import DataBaseServer.DataHomePage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.Arrays;
import java.util.List;

public class SetupServer extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        configRouter()
                .compose(this::startHttpServer)
                .compose(this::deployVerticles)
                .onComplete(this::resultStartServer);
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {

    }

    Future<Router> configRouter() {
        return Future.future((promise) -> {
            Router router = Router.router(vertx);
            
            // cors for test 
            router.route().handler(CorsHandler.create("*")
                    .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                    .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                    .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                    .allowedHeader("Access-Control-Allow-Credentials")
                    .allowedHeader("Access-Control-Allow-Headers")
                    .allowedHeader("Content-Type"));

            promise.complete(router);
        });
    }

    Future<Router> startHttpServer(Router router) {
        return Future.future((promise) -> {
            vertx.createHttpServer().requestHandler(router).listen(8080).result();
            promise.complete(router);
        });
    }

    /*
    * importance deploy all neccessary verticles
     */
    Future<Void> deployVerticles(Router router) {

        var homepage = Future.future((promise) -> promise.complete(vertx.deployVerticle(new HomePage(router)).result()));
        var dataHomePage = Future.future((promise) -> promise.complete(vertx.deployVerticle(new DataHomePage()).result()));
        
        var addmin = Future.future((promise) -> promise.complete(vertx.deployVerticle(new Admin(router)).result()));
        var dataAdmin = Future.future((promise) -> promise.complete(vertx.deployVerticle(new DataAdmin()).result()));
        var detailProduct = Future.future((promise) -> promise.complete(vertx.deployVerticle(new DetailProduct(router)).result()));

        List<Future> listFutre = Arrays.asList(homepage, dataHomePage, addmin, dataAdmin, detailProduct);

        return CompositeFuture.all(listFutre).mapEmpty();
    }

    /*
    *
     */
    void resultStartServer(AsyncResult<Void> asynResult) {
        if (asynResult.succeeded()) {
            System.out.println("create server Success");
        }
        if (asynResult.failed()) {
            System.out.println("create server fail " + asynResult.cause().getMessage());
        }
    }

}
