package restserver.kubeapi;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class KubernetesApi {

    /**
     * This object handles the REST calls of the node object.
     */
    private static CoreV1Api coreApi;

    /**
     * This method is used to call the API from outside the cluster
     * @param kubeConfigPath the path of the config file. Usually at $HOME/.kube/config.
     * @throws FileNotFoundException If the path of the file it not correct
     * @throws IOException
     */
    public static void setUpApi(String kubeConfigPath) throws FileNotFoundException, IOException {
        // loading the out-of-cluster config, a kubeconfig from file-system
        ApiClient client =
                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);

        coreApi = new CoreV1Api();
    }

    /**
     * This method builds the client so that it can call the kubernetes API inside a pod.
     * @throws IOException
     */
    public static void setUpApi() throws IOException{
        // loading the in-cluster config, including:
        //   1. service-account CA
        //   2. service-account bearer-token
        //   3. service-account namespace
        //   4. master endpoints(ip, port) from pre-set environment variables
        ApiClient client = null;
        try{
            client = ClientBuilder.cluster().build();
        }
        catch(Exception e){
            throw new IOException(e.getMessage() + ". Problems while building the client.");
        }
        // set the global default api-client to the in-cluster one from above
        try
        {
            Configuration.setDefaultApiClient(client);
        }
        catch(Exception e){
            throw new IOException(e.getMessage() + ".\n Error while adding the client to the configuration");
        }
        coreApi = new CoreV1Api(client);
    }

    public static List<V1Node> getNodeList() throws ApiException {
        return coreApi.listNode("", Boolean.FALSE, "", "",
                "", 0, "", "",
                0, Boolean.FALSE).getItems();
    }

    public static void updateNode(V1Node node) throws ApiException {
        coreApi.replaceNode(Objects.requireNonNull(node.getMetadata()).getName(), node, "", "", "");
    }
}

