package TestCaseData;

import DataBaseServer.DataHomePage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class TestCaseAdmin extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new DataHomePage());
    }

}
