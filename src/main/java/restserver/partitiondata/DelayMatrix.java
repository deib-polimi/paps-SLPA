package restserver.partitiondata;

import com.google.gson.annotations.SerializedName;

public class DelayMatrix {

    @SerializedName("routes")
    private final int[][] routes;

    public DelayMatrix(int[][] routes) {
        this.routes = routes;
    }

    public int[][] getRoutes() {
        return routes;
    }
}
