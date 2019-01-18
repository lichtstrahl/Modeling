package iv.root.modeling.app;

import android.app.Application;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Single;
import iv.root.modeling.BuildConfig;
import iv.root.modeling.network.AustralianNationUniversityAPI;
import iv.root.modeling.network.RandomAPI;
import iv.root.modeling.network.body.IntegerBody;
import iv.root.modeling.network.body.IntegerParams;
import iv.root.modeling.network.dto.ResponseQRNS;
import iv.root.modeling.network.dto.ResponseRandomDTO;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static RandomAPI randomAPI;
    private static AustralianNationUniversityAPI universityAPI;

    @Override
    public void onCreate() {
        super.onCreate();
        randomAPI = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_randomORG)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(RandomAPI.class);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        universityAPI = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_QRNS_ANU)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(AustralianNationUniversityAPI.class);
    }

    public static Single<ResponseRandomDTO> requestRandomInteger(int count, int min, int max) {
        IntegerBody integerBody = new IntegerBody(new IntegerParams(count, min, max));
        return randomAPI.randomInteger(integerBody);
    }

    public static Single<ResponseQRNS> requestRandomInteger(int count) {
        return universityAPI.randomInteger(count, "uint16");
    }

    public static void logI(String msg) {
        Log.i(BuildConfig.GLOBAL_TAG, msg);
    }

    public static void logE(String msg) {
        Log.e(BuildConfig.GLOBAL_TAG, msg);
    }
}
