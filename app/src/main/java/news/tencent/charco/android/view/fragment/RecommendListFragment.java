package news.tencent.charco.android.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import news.tencent.charco.android.R;
import news.tencent.charco.android.base.BaseFragment;
import news.tencent.charco.android.utils.AnimationUtil;
import news.tencent.charco.android.utils.ToastUtil;
import news.tencent.charco.android.view.activity.AlbumActivity;
import news.tencent.charco.android.view.activity.WebViewActivity;
import news.tencent.charco.android.view.adapter.RecommendListAdapter;
import news.tencent.charco.android.widget.popup.LoseInterestPopup;

/**
 * @CreateDate: 2019/6/14 17:08
 * @ClassName: RecommendListFragment
 * @Author: Lunatic Princess
 * @Descriptions: 碎片链表，在RecommendFragment中实例化进行界面填充
 */
public class RecommendListFragment extends BaseFragment implements RecyclerView.OnChildAttachStateChangeListener,
        OnRefreshListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener,
        LoseInterestPopup.OnLoseInterestListener {

    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mSmartRefreshLayout;
    private TextView mTvTip;
    private RecommendListAdapter mAdapter_1;



    private int key=0;

    public static RecommendListFragment newInstance(Bundle args) {
        RecommendListFragment f = new RecommendListFragment();
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
        return R.layout.fragment_news_list;
    }

    public void initView(View rootView) {
        mSmartRefreshLayout = findViewById(R.id.refreshLayout);
        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        mSmartRefreshLayout.setEnableOverScrollDrag(false);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        mAdapter_1 = new RecommendListAdapter(null);
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
}
