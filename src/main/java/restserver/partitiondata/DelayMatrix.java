package restserver.partitiondata;

import com.google.gson.annotations.SerializedName;

public class DelayMatrix {

    @SerializedName("routes")
    private final float[][] routes;

    public DelayMatrix(float[][] routes) {
        this.routes = routes;
    }

    public float[][] getRoutes() {
        return routes;
    }
}
