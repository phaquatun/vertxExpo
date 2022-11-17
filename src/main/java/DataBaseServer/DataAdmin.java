package DataBaseServer;

import ConstanceVariable.ConstantsData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataAdmin extends AbstractVerticle {

    MongoClient mongoClient;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        createMongoDB()
                .compose(this::saveDataAdmin)
                .result();
    }

    Future<Void> createMongoDB() {
        return Future.future((promise) -> {
            JsonObject query = new JsonObject().put("db_name", ConstantsData.Db_Name).put("connection_string", ConstantsData.Db_Uri);
            mongoClient = MongoClient.createShared(vertx, query);
            promise.complete();
        });
    }

    Future<Void> saveDataAdmin(Void unused) {
        return Future.future((promise) -> {
            var admin1 = new JsonObject().put("_id", 1).put("user", "tungpham92v").put("pass", "testerTung92");
            var admin2 = new JsonObject().put("_id", 2).put("user", "Checker01").put("pass", "Checker01v62");
            var admin3 = new JsonObject().put("_id", 3).put("user", "Checker01").put("pass", "Checker02v62");

            List<JsonObject> listAdmin = Arrays.asList(admin1, admin2, admin3);
            listAdmin.forEach((jsonAdmin) -> mongoClient.save(ConstantsData.Collections_Admin, jsonAdmin));

            promise.complete();
        });
    }

    void resultHandleData(AsyncResult<Void> asyResult) {
        if (asyResult.succeeded()) {

        }
        if (asyResult.failed()) {
            System.out.println("dataAdmin fail ...");
        }
    }

}
