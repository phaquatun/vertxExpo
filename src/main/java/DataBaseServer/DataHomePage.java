package DataBaseServer;

import static ConstanceVariable.ConstantsAddres.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import static ConstanceVariable.ConstantsData.*;

public class DataHomePage extends AbstractVerticle {

    private MongoClient mongoClient;

    @Override
    public void start() throws Exception {
        createMongoDB()
                .compose(this::configEventBus)
                .result();
        //                .onComplete(this::resultHandleData);

    }

    Future<Void> createMongoDB() {
        return Future.future((promise) -> {
            JsonObject query = new JsonObject().put("db_name", Db_Name).put("connection_string", Db_Uri);
            mongoClient = MongoClient.createShared(vertx, query);
            promise.complete();
        });
    }

    Future<Void> configEventBus(Void unused) {
        return Future.future((promise) -> {
            vertx.eventBus().consumer(adminHandleInfo, this::handleInfoHomepage);
            vertx.eventBus().consumer(detailProduct, this::detailProduct);

            promise.complete();
        });
    }

    /*
    jsonbody input format : {"token":"xxx","products":{"nameProduct":"xxx" , "descrips":"Lorem xxx... ","discount":"xxx","bestSeller":true , "linkYT":"xxx"}, "type":"add"}
    jsonbody input format : {"token":"xxx","contacts":{"nameContact":"xxx" , "descrips":"Lorem xxx... ","rank":"xxx-yyy","linkZalo":true , "linkFB":"xxx"}, "type":"add"}
    
    jsonbody as product :  if jsonbody findOne_infoToken == info of data_admin ? add product : cannot add product
    jsonbody as contact : if jsonbody findOne_infoToken == info of data_admin ? add contact : cannot add contactF
    
     */
    void handleInfoHomepage(Message<Object> msg) {
        if (msg.body() instanceof JsonObject) {
            var infoAdmin = (JsonObject) msg.body();

            String token = infoAdmin.getString("token");
            String localData = infoAdmin.containsKey("products") ? Collections_Products : Collections_Contacts;

            if (infoAdmin.getString("type").equals("add")) {
                var jsonRes = findInfoToken(infoAdmin, token).compose((resultFind) -> this.addInfo(infoAdmin, localData)).result();
                msg.reply(jsonRes);
            }
            if (infoAdmin.getString("type").equals("update")) {
//                updateInfo(infoAdmin, infoAdmin);
            }

        } else {
            msg.fail(400, "Bad Request : wrong your format");
        }

    }

    Future<JsonObject> findInfoToken(JsonObject json, String token) {
        return mongoClient.findOne(Collections_Admin, new JsonObject().put("user", token), null);
    }

    Future<JsonObject> addInfo(JsonObject json, String localData) {
        JsonObject response = new JsonObject();

        return Future.future((promise) -> {
            if (json == null) {
                promise.complete(response.put("success", false).put("valErr", "Err permission").put("statusCode", 401));
            } else {
                promise.complete(response.put("success", true).put("valErr", mongoClient.save(Collections_Products, json).result()).put("time", System.currentTimeMillis()));
            }
        });
    }

    Future<JsonObject> updateInfo(JsonObject query, JsonObject update, String localData) {
        return mongoClient.findOneAndUpdate(localData, query, update);
    }

    /*
    * detail product
     */
    void detailProduct(Message<Object> msg) {

    }

    /*
    * handle resultData deploy
     */
    void resultHandleData(AsyncResult<JsonObject> asyResult) {
        if (asyResult.succeeded()) {
            System.out.println("success " + asyResult.toString());
        }
        if (asyResult.failed()) {
            System.out.println("dataAdmin fail ..." + asyResult.cause().getMessage());
        }
    }
}
