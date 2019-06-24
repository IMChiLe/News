package news.tencent.charco.android.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
import news.tencent.charco.android.entity.Recommend;
import news.tencent.charco.android.utils.AnimationUtil;
import news.tencent.charco.android.utils.ToastUtil;
import news.tencent.charco.android.view.activity.AlbumActivity;
import news.tencent.charco.android.view.activity.WebViewActivity;
import news.tencent.charco.android.view.adapter.RecommendListAdapter_2;
import news.tencent.charco.android.widget.popup.LoseInterestPopup;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @CreateDate: 2019/6/14 17:08
 * @ClassName: RecommendListFragment
 * @Author: Lunatic Princess
 * @Descriptions: 碎片链表，在RecommendFragment中实例化进行界面填充
 */
public class RecommendListFragment_2 extends BaseFragment implements RecyclerView.OnChildAttachStateChangeListener,
        OnRefreshListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener,
        LoseInterestPopup.OnLoseInterestListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;
    private TextView mTvTip;
    private RecommendListAdapter_2 mAdapter_1;
    private TextView textView;



    private int key=0;

    public static RecommendListFragment_2 newInstance(Bundle args) {
        RecommendListFragment_2 f = new RecommendListFragment_2();
        f.setArguments(args);
        return f;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    protected int provideContentViewId() {
        return R.layout.fragment_recommend_list;
    }

    public void initView(View rootView) {
        ArrayList<Recommend> recommendArrayList = getHotTitle();
        ArrayList<Recommend> list = getDataAsync();
        list.add(0,new Recommend());


        mSmartRefreshLayout = findViewById(R.id.refreshLayout);
        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        mSmartRefreshLayout.setEnableOverScrollDrag(false);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        mAdapter_1 = new RecommendListAdapter_2(null, recommendArrayList, list);
        mRecyclerView.setAdapter(mAdapter_1);
        mRecyclerView.addOnChildAttachStateChangeListener(this);
        mTvTip = findViewById(R.id.tv_tip);
        mAdapter_1.setOnItemClickListener(this);
        mAdapter_1.setOnItemChildClickListener(this);
    }

    public void initListener() {
    }

    protected void loadData() {
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {

    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        JZVideoPlayer jzvd = view.findViewById(R.id.video_player_recommend);
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

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mSmartRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSmartRefreshLayout.finishRefresh(0);
                AnimationUtil.showTipView(mTvTip,mRecyclerView);
            }
        },2000);

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position % 7){
            case 0:
                startActivity(new Intent(getActivity(), WebViewActivity.class));
                break;
            case 1:
//                return NEWS_HOT_LIST;
            case 2:
//                return NEWS_HOT_TEXT;
            case 3:
//                return NEWS_SIMPLE_PHOTO;
            case 4:
//                return NEWS_THREE_PHOTO;
            case 5:
                startActivity(new Intent(getActivity(), AlbumActivity.class));
                break;
            case 6:
//                return NEWS_VIDEO;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.iv_delete:
                new LoseInterestPopup(getActivity())
                        .setSource("腾讯新闻")
                        .setPosition(position)
                        .setOnLoseInterestListener(this)
                        .showPopup(view);
                break;
        }
    }


    @Override
    public void onLoseInterestListener(int poor_quality, int repeat, String source, int position) {
        ToastUtil.showToast("position = "+position);
    }

    public ArrayList<Recommend> getHotTitle(){
        final ArrayList<Recommend> rlist = new ArrayList<Recommend>();
        //向服务器请求数据
        //final String path="http://10.0.2.2:8080/SelectNewTypeZero";
        final String path="http://10.0.2.2:8080/recommends";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(path).build();
                    Response response=client.newCall(request).execute();
                    parseJsonWithJsonObjectWithReMen(response,rlist);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return rlist;
    }

    public void parseJsonWithJsonObjectWithReMen(Response response,ArrayList<Recommend> rlist) throws IOException {
        String responseData=response.body().string();
        try{
            JSONArray jsonArray=new JSONArray(responseData);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String url = jsonObject.getString("url");
                Recommend recommend = new Recommend();
                recommend.setUrl(url);
                rlist.add(recommend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //将耗时的网路请求操作放在一个子线程中操作
    private ArrayList<Recommend> getDataAsync() {
        final String url="http://10.0.2.2:8080/recommends";
        final ArrayList<Recommend> list = new ArrayList<Recommend>();
        new Thread(){
            public void run(){
                try {
                    OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
                    RequestBody requestBody = new FormBody.Builder().add("id",key+"").build();
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("kwwl", "onFailure: "+e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
//                            parseJsonWithJsonObject(response,list);
                            parseJsonWithJsonObject(response, list);
                            Log.i("kwwl","response.boby=="+response.body());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return list;
    }

    //用于解析后台的json数据
    private void parseJsonWithJsonObject(Response response,ArrayList<Recommend> list) throws IOException {
        final String responseData=response.body().string();
        try{
            JSONArray jsonArray=new JSONArray(responseData);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String id=jsonObject.getString("id");
                String url=jsonObject.getString("url");
                Recommend recommend = new Recommend(id, url);
                list.add(recommend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
