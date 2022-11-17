
package StandServer;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;


public class DetailProduct extends AbstractVerticle{

     private Router router;

    public DetailProduct(Router router) {
        this.router = router;
    }

    
    @Override
    public void start() throws Exception {
        
    }
}
