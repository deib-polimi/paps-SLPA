package restserver;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restserver.partition.Community;
import restserver.partition.SLPA;
import restserver.partition.SLPA_Node;
import restserver.partitiondata.Host;
import restserver.partitiondata.PartitionData;
import restserver.partitiondata.PartitionParameters;
import restserver.partitiondata.PartitionResult;

import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.post;

public class PartitionRoute {

    private static final Logger logger = LoggerFactory.getLogger(PartitionRoute.class);

    static  void configureRoutes() {
        post("/communities", (request, response) -> {

            logger.info("new POST request to partitions");

            response.header("Access-Control-Allow-Credentials", "true");
            response.type("application/json");

            PartitionData data = new Gson().fromJson(request.body(), PartitionData.class);

            logger.info("Partition data:");
            logger.info(data.toString());
            SLPA slpa = new SLPA(data);

            PartitionParameters parameters = data.getParameters();
            List<Community> communities = slpa.computeCommunities(parameters.getIterations(), parameters.getProbabilityThreshold());
            return new Gson().toJson(new PartitionResult(communities), PartitionResult.class);
        });
    }
}