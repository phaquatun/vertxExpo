package AdminServer;

import static ConstanceVariable.ConstantsAddres.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class Admin extends AbstractVerticle {

    private Router router;

    public Admin(Router router) {
        this.router = router;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        router.post("/handleInfoHomepage").handler(BodyHandler.create());
        router.post("/handleInfoHomepage").handler(this::handleInfoPage);

    }

    void handleInfoPage(RoutingContext ctx) {
        JsonObject jsonBody = ctx.getBodyAsJson();

        vertx.eventBus().request(adminHandleInfo, jsonBody, (asynMsg) -> {
            ctx.response().end((String) asynMsg.result().body());
        });
    }

}
