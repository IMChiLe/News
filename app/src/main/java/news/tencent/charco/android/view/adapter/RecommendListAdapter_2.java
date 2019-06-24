package news.tencent.charco.android.view.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import news.tencent.charco.android.New;
import news.tencent.charco.android.R;
import news.tencent.charco.android.entity.Recommend;
import news.tencent.charco.android.utils.ToastUtil;
import news.tencent.charco.android.utils.UIUtils;
import news.tencent.charco.android.widget.HotRefrechHead;
import news.tencent.charco.android.widget.MyHorizontalRefreshLayout;
import xiao.free.horizontalrefreshlayout.HorizontalRefreshLayout;
import xiao.free.horizontalrefreshlayout.RefreshCallBack;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @CreateDate: 2019/6/17 21:06
 * @ClassName: RecommendListAdapter
 * @Author: Lunatic Princess
 * @Descriptions: $description
 */
public class RecommendListAdapter_2 extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> implements View.OnClickListener {

    /**
     * 专题
     */
    public static final int RECOMMENDS_SUBJECT = 100;
    /**
     * 热点列表,可横向滚动
     */
    public static final int RECOMMENDS_HOT_LIST = 200;
    /**
     * 热点列表,纯文字滚动
     */
    public static final int RECOMMENDS_HOT_TEXT = 201;
    /**
     * 单图右侧小图布局(1.小图新闻；2.视频类型，右下角显示视频时长)
     */
    public static final int  RECOMMENDS_SIMPLE_PHOTO = 300;
    /**
     * 三张图片布局(文章、广告)
     */
    public static final int RECOMMENDS_THREE_PHOTO = 400;
    /**
     * 图集
     */
    public static final int RECOMMENDS_ALBUM_PHOTO = 500;
    /**
     * 视频
     */
    public static final int RECOMMENDS_VIDEO = 600;
    /**
     * 用于存储后台返回的热门推送数据
     */
    private ArrayList<Recommend> hotlist = new ArrayList<Recommend>();
    /**
     * 用于存储后台返回的数据
     */
    private ArrayList<Recommend> datas = new ArrayList<Recommend>();

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public RecommendListAdapter_2(List<MultiItemEntity> data, ArrayList<Recommend> recommendArrayList, ArrayList<Recommend> datas) {
        super(data);
        addItemType(RECOMMENDS_SUBJECT, R.layout.item_recommend_subject);
        addItemType(RECOMMENDS_SIMPLE_PHOTO, R.layout.item_news_simple_photo);
        addItemType(RECOMMENDS_THREE_PHOTO, R.layout.item_news_three_photo);
        addItemType(RECOMMENDS_ALBUM_PHOTO, R.layout.item_news_album_photo);
        addItemType(RECOMMENDS_VIDEO, R.layout.item_news_video);
        addItemType(RECOMMENDS_HOT_LIST, R.layout.item_news_hot_list);
        addItemType(RECOMMENDS_HOT_TEXT, R.layout.item_hot_text_srcoll);
    }

//    public RecommendListAdapter_2(List<Recommend> recommendArrayList) {
//        super(recommendArrayList);
//    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()){
            case RECOMMENDS_HOT_LIST:
                setHotList(helper);
                break;
            case RECOMMENDS_VIDEO:
                JZVideoPlayerStandard videoPlayer = helper.getView(R.id.video_player);
                videoPlayer.setAllControlsVisiblity(GONE, GONE, VISIBLE, GONE, VISIBLE, VISIBLE, GONE);
                videoPlayer.setState(JZVideoPlayer.CURRENT_STATE_NORMAL);
                videoPlayer.resetProgressAndTime();
                videoPlayer.setUp
                        ("https://gss3.baidu.com/6LZ0ej3k1Qd3ote6lo7D0j9wehsv/tieba-smallvideo-spider/7042402_eb0c2ab3dc960772188c3ff0f35324b0.mp4",
                        JZVideoPlayer.SCREEN_WINDOW_LIST,"视频标题");
                videoPlayer.thumbImageView.setImageResource(R.drawable.bg_huawei);
                break;
//            case RECOMMENDS_HOT_TEXT:
//                setScrollText(helper);
//                break;
        }
        helper.addOnClickListener(R.id.iv_delete);
    }

    //推荐
    private void setScrollText(BaseViewHolder helper){
        ViewFlipper viewFlipper = helper.getView(R.id.viewFlipper);
        if (viewFlipper != null){

            if (viewFlipper.getChildCount() > 0){
                viewFlipper.removeAllViews();
            }
            String[] array = UIUtils.getContext().getResources().getStringArray(R.array.hot);
            for (String string : array){
                TextView textView = (TextView) View.inflate(mContext, R.layout.item_hot_recommend,null);
                textView.setText(string);
                textView.setTag(string);
                textView.setOnClickListener(this);
                viewFlipper.addView(textView);
            }
            viewFlipper.startFlipping();

        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position % 7){
            case 0:
                return RECOMMENDS_VIDEO;
//            case 1:
//                return RECOMMENDS_VIDEO;
//            case 2:
//                return RECOMMENDS_VIDEO;
//            case 3:
//                return RECOMMENDS_VIDEO;
//            case 4:
//                return RECOMMENDS_VIDEO;
//            case 1:
//                return RECOMMENDS_HOT_LIST;
//            case 2:
//                return RECOMMENDS_HOT_TEXT;
//            case 3:
//                return RECOMMENDS_SIMPLE_PHOTO;
//            case 4:
//                return RECOMMENDS_THREE_PHOTO;
//            case 5:
//                return RECOMMENDS_ALBUM_PHOTO;
//            case 6:
//                return RECOMMENDS_VIDEO;
        }
        return super.getItemViewType(position);
    }

    private void setHotList(BaseViewHolder helper){

        RecyclerView recyclerView = helper.getView(R.id.recyclerView);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null){
            new PagerSnapHelper().attachToRecyclerView(recyclerView);
            final MyHorizontalRefreshLayout refreshLayout = helper.getView(R.id.refreshLayout);
            refreshLayout.setRefreshHeader(new HotRefrechHead(UIUtils.getContext()), HorizontalRefreshLayout.RIGHT);
            refreshLayout.setRefreshCallback(new RefreshCallBack() {
                @Override
                public void onLeftRefreshing() {

                }

                @Override
                public void onRightRefreshing() {
                    refreshLayout.onRefreshComplete();
                    ToastUtil.showToast("查看更多");
                }
            });

            final NewsHotHorizontalListAdapter hotAdapter = new NewsHotHorizontalListAdapter(null);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setAdapter(hotAdapter);
            refreshLayout.setLayoutManager(linearLayoutManager);
            recyclerView.setLayoutManager(linearLayoutManager);
            refreshLayout.setItemSize(4);
            hotAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                }
            });
            helper.addOnClickListener(R.id.tv_more_hot);
        }

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_hot_item:
                ToastUtil.showToast((String) view.getTag());
                break;
        }
    }
}
