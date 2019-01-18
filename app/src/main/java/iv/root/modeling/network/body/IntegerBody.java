package iv.root.modeling.network.body;

import com.google.gson.annotations.SerializedName;

public class IntegerBody {
    @SerializedName("jsonrpc")
    String verJSON;
    @SerializedName("method")
    String method;
    @SerializedName("id")
    int id;
    @SerializedName("params")
    IntegerParams params;

    public IntegerBody(IntegerParams params) {
        this.verJSON = "2.0";
        this.method = "generateIntegers";
        this.id = 1;
        this.params = params;
    }

    public String getVerJSON() {
        return verJSON;
    }

    public String getMethod() {
        return method;
    }

    public int getId() {
        return id;
    }

    public IntegerParams getParams() {
        return params;
    }
}




/*
{
  "jsonrpc" : "1.0",
  "method": "generateIntegers",
  "id" : 1,
  "params" : {
    ...
  }
}
 */
