package org.woolrim.woolrim.Temp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*
    현재 임시로 만들어 둔거임.
    필요에 따라 수정해야됨
 */
public class GetDataService extends Service {
    Retrofit retrofit;
    DataInterface dataInterface;
    int fromActivity;
    List<Data.Result> mdata;
    Call<Data> request;

    public GetDataService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("time", "Create");
        retrofit = new Retrofit.Builder()
                .baseUrl("http://stou2.cafe24.com/test/") // php가 있는 폴더위치 설정
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())//http 로그 찍어주는 함수
                .build();
        dataInterface = retrofit.create(DataInterface.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("time", "StartCommand");
        if (intent == null) {
            return Service.START_STICKY;
        } else {
            processIntent(intent);
        }

//        requestServer(dataInterface, 결과를 보낼 액티비티);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!request.isCanceled()) {
            Log.d("lat", "cancel");
            request.cancel();
        }
        Log.d("lat", "destroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("time", "onBind");
        throw new UnsupportedOperationException("");
    }

    public void requestServer(DataInterface service, final Class openClass) {  //서버로 부터 데이터 받아서 원하는 액티비티로 보냄
        request = service.getPost("mapTest.php", 0, 40, 100, 150);//바꿔야된다.
        request.enqueue(new Callback<Data>() { // retrofit 비동기식 실행
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data data = response.body();
                mdata = data.result;
                Intent itn = new Intent(getApplicationContext(), openClass);
                itn.putExtra("Data", (Serializable) mdata);
                itn.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(itn);
                stopSelf(); // 해당 서비스 destroy
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
            }
        });
    }

    private void processIntent(Intent intent) { //이전 액티비티에서 넘어온 데이터 처리
        fromActivity = intent.getIntExtra("Activity", 0);
    }

    private OkHttpClient createOkHttpClient() { //http로그 찍는 함수(신경 안써도 됨)
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder.build();
    }
}
