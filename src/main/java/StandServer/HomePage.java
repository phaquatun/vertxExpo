package StandServer;

import ConstanceVariable.ConstantsAddres;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;


/*
homepage incluse products sell , info contact  .
 */
public class HomePage extends AbstractVerticle {

    private Router router;

    public HomePage(Router router) {
        this.router = router;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        router.get("/*").handler(StaticHandler.create().setWebRoot("fontend\\homepage\\").setIndexPage("index.html").setDefaultContentEncoding("UTF-8"));
        router.get("/detail/product/:name").handler(this::handleDetailProduct);

    }

    void handleDetailProduct(RoutingContext ctx) {
        String nameProduct = ctx.pathParam("name");

        vertx.eventBus().request(ConstantsAddres.detailProduct, "name", (msg) -> {
            ctx.response().end((String) msg.result().body());
        });
    }

   
}
