package news.tencent.charco.android.view.fragment;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import news.tencent.charco.android.New;
import news.tencent.charco.android.R;
import news.tencent.charco.android.base.BaseFragment;
import news.tencent.charco.android.utils.AnimationUtil;
import news.tencent.charco.android.utils.ToastUtil;
import news.tencent.charco.android.view.activity.AlbumActivity;
import news.tencent.charco.android.view.activity.WebViewActivity;
import news.tencent.charco.android.view.adapter.NewsListAdapter;
import news.tencent.charco.android.widget.popup.LoseInterestPopup;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created 18/7/19 16:39
 * Author:charcolee
 * Version:V1.0
 * ----------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------
 */

public class NewsListFragment extends BaseFragment implements RecyclerView.OnChildAttachStateChangeListener, OnRefreshListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, LoseInterestPopup.OnLoseInterestListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;
    private TextView mTvTip;
    private NewsListAdapter mAdapter;
    private int key;
    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_news_list;
    }

    @Override
    public void initView(View rootView) {
        ArrayList<New> hotTitle=getHotTitle();
        ArrayList<New> list = getDataAsync();
        list.add(0,new New());
        SystemClock.sleep(2000);
//        Log.i("kwwl","LIST.SIZE=="+list.size());
//        for (New n: list) {
//            Log.i("kwwl","n=="+n.getTitle());
//        }
        mSmartRefreshLayout = findViewById(R.id.refreshLayout);
        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        mSmartRefreshLayout.setEnableOverScrollDrag(false);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mAdapter = new NewsListAdapter(null,hotTitle,list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnChildAttachStateChangeListener(this);
        mTvTip = findViewById(R.id.tv_tip);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void initListener() {
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onChildViewAttachedToWindow(View view) {

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

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public ArrayList<New> getHotTitle(){
        final ArrayList<New> rlist = new ArrayList<New>();
        //向服务器请求数据
        //final String path="http://10.0.2.2:8080/SelectNewTypeZero";
        final String path="http://106.14.167.49:8080/news/SelectNewTypeZero";
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
    public void parseJsonWithJsonObjectWithReMen(Response response,ArrayList<New> rlist) throws IOException {
        String responseData=response.body().string();
        try{
            JSONArray jsonArray=new JSONArray(responseData);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                New aNew=new New();
                aNew.setTitle(title);
                rlist.add(aNew);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //将耗时的网路请求操作放在一个子线程中操作
    private ArrayList<New> getDataAsync() {
        final String url="http://106.14.167.49:8080/news/SelectNews";
        final ArrayList<New> list = new ArrayList<New>();
        new Thread(){
            public void run(){
                try {
                    OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
                    RequestBody requestBody = new FormBody.Builder().add("newtype",key+"").build();
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("kwwl", "onFailure: "+e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            parseJsonWithJsonObject(response,list);
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
    private void parseJsonWithJsonObject(Response response,ArrayList<New> list) throws IOException {
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
                String img = jsonObject.getString("img");
                New aNew=new New(account,videourl,author,newtype,title,dateofpublication,text);
                aNew.setImg(img);
                list.add(aNew);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
