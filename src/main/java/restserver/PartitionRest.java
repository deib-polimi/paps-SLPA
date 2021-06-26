package restserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class PartitionRest {

    private static final Logger logger = LoggerFactory.getLogger(PartitionRest.class);

    public static void main(String[] args) {
        path("/api", () -> {
            before("/*", (q, a) -> {
                logger.info("Received api call");
                a.header("Access-Control-Allow-Origin", "null");
                a.header("Access-Control-Allow-Credentials", "true");
                a.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
            });
            PartitionRoute.configureRoutes();
        });
    }
}
// porco dio e la madonna