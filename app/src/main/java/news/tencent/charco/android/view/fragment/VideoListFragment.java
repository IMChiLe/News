package news.tencent.charco.android.view.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import news.tencent.charco.android.New;
import news.tencent.charco.android.R;
import news.tencent.charco.android.base.BaseFragment;
import news.tencent.charco.android.view.adapter.VideoListAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created 18/7/21 17:11
 * Author:charcolee
 * Version:V1.0
 * ----------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------
 */

public class VideoListFragment extends BaseFragment implements RecyclerView.OnChildAttachStateChangeListener {

    private RecyclerView mRecyclerView;

    // 访问的服务器路径
    //private String path="http://10.0.2.2:8080/SelectIndexNews";
    private String path="http://106.14.167.49:8080/news/SelectIndexNews";

    //key 用于传输给服务器的newtype
    private int key=0;

    //用于存放服务器返回的json 解析后数据
    private List<New> list = new ArrayList<>();

    //增加newInstance函数替代Fragment 的构造函数
    public static VideoListFragment newInstance(Bundle args) {
        VideoListFragment f = new VideoListFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_video_list;
    }

    @Override
    public void initView(View rootView) {
        Log.i("kwwl","key=="+key);
        getDataAsync();
        SystemClock.sleep(500);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //mRecyclerView.setAdapter(new VideoListAdapter(Arrays.asList(getResources().getStringArray(R.array.hot))));
        Log.i("kwwl","length=="+list.size());
        mRecyclerView.setAdapter(new VideoListAdapter(list));
        mRecyclerView.addOnChildAttachStateChangeListener(this);


    }

    //将耗时的网路请求操作放在一个子线程中操作
    private void getDataAsync() {
        new Thread(){
            public void run(){
                try {
                    OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
                    RequestBody requestBody = new FormBody.Builder().add("newtype",key+"").build();
                    Request request = new Request.Builder().url(path).post(requestBody).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("kwwl", "onFailure: "+e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            parseJsonWithJsonObject(response);
                            Log.i("kwwl","response.boby=="+response.body());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //用于解析后台的json数据
    private void parseJsonWithJsonObject(Response response) throws IOException {
        final String responseData=response.body().string();
        try{
            JSONArray jsonArray=new JSONArray(responseData);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String account=jsonObject.getString("account");
                String videourl=jsonObject.getString("videourl");
                String author = jsonObject.getString("author");
                String title = jsonObject.getString("title");
                String dateofpublication = jsonObject.getString("dateofpublication");
                String text = jsonObject.getString("text");
                int newtype = jsonObject.getInt("newtype");
                New aNew=new New(account,videourl,author,newtype,title,dateofpublication,text);
                list.add(aNew);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void loadData() {

    }

    @Override
    public void onChildViewAttachedToWindow(View view) {

    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        JZVideoPlayer jzvd = view.findViewById(R.id.video_player);
        if (jzvd!=null){
            Object[] dataSourceObjects = jzvd.dataSourceObjects;
            if (dataSourceObjects!=null&&
                    JZUtils.dataSourceObjectsContainsUri(dataSourceObjects, JZMediaManager.getCurrentDataSource())) {
                JZVideoPlayer currentJzvd = JZVideoPlayerManager.getCurrentJzvd();
                if (currentJzvd != null && currentJzvd.currentScreen != JZVideoPlayer.SCREEN_WINDOW_FULLSCREEN) {
                    JZVideoPlayer.releaseAllVideos();
                }
            }
        }
    }
}
