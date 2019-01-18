package iv.root.modeling.network;

import io.reactivex.Single;
import iv.root.modeling.network.dto.ResponseQRNS;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AustralianNationUniversityAPI {
    @GET("/API/jsonI.php")
    Single<ResponseQRNS> randomInteger(@Query("length") int count, @Query("type") String type);
}
