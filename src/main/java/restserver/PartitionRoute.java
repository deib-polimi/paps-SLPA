package restserver;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restserver.ordering.Utils;
import restserver.partition.Community;
import restserver.partition.SLPA;
import restserver.partitiondata.PartitionData;
import restserver.partitiondata.PartitionParameters;
import restserver.partitiondata.PartitionResult;

import java.util.List;

import static spark.Spark.post;

public class PartitionRoute {

    private static final Logger logger = LoggerFactory.getLogger(PartitionRoute.class);

    static void configureRoutes() {
        post("/communities", (request, response) -> {

            logger.info("new POST request to partitions");

            response.type("application/json");

            PartitionData data = new Gson().fromJson(request.body(), PartitionData.class);

            logger.info("Partition data:");
            logger.info(data.toString());
            SLPA slpa = new SLPA(data);

            PartitionParameters parameters = data.getParameters();
            List<Community> communities = slpa.computeCommunities(parameters.getIterations(), parameters.getProbabilityThreshold());

            if (communities.size() > 1) communities = Utils.orderCommunities(communities);

            return new Gson().toJson(new PartitionResult(communities), PartitionResult.class);
        });
    }
}
