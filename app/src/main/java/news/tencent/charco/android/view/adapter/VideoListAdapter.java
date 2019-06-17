package news.tencent.charco.android.view.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import news.tencent.charco.android.New;
import news.tencent.charco.android.R;
import news.tencent.charco.android.utils.PrictureUtil;
import news.tencent.charco.android.utils.Time;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created 18/7/21 16:58
 * Author:charcolee
 * Version:V1.0
 * ----------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------
 */

public class VideoListAdapter extends BaseQuickAdapter<New,BaseViewHolder> {
    public VideoListAdapter(@Nullable List<New> data) {
        super(R.layout.item_video_list,data);
        //Log.i("kwwl","data=="+data.get(0).getVideourl());
    }

    @Override
    protected void convert(BaseViewHolder helper, New item) {
        Log.i("kwwl","i=="+helper.getLayoutPosition());
        Log.i("kwwl","videourl=="+item.getVideourl());
        Log.i("kwwl","date=="+item.getDateofpublication());
        if(item.getVideourl()!=""||item.getVideourl()!=null){
            //设置视频
            final JZVideoPlayerStandard videoPlayer = helper.getView(R.id.video_player);
            videoPlayer.setAllControlsVisiblity(VISIBLE, GONE, VISIBLE, GONE, VISIBLE, VISIBLE, GONE);
            videoPlayer.setState(JZVideoPlayer.CURRENT_STATE_NORMAL);
            videoPlayer.resetProgressAndTime();
            videoPlayer.setUp(  item.getVideourl(),
                    //videoPlayer.setUp("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
                    JZVideoPlayer.SCREEN_WINDOW_LIST,item.getTitle());
            //设置视频封面
            final  String img = item.getImg();
//                Log.i("kwwl","img=="+datas.get(helper.getLayoutPosition()).getImg());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStream inputStream = null;
                    try {
                        inputStream = PrictureUtil.getImageViewInputStream(img);
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);//从路径加载出图片bitmap
                        Activity activity = (Activity) videoPlayer.getContext();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                videoPlayer.thumbImageView.setImageBitmap(bitmap);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            //videoPlayer.thumbImageView.setImageResource(R.drawable.bg_motherland);
            //videoPlayer.thumbImageView.setImageResource(R.drawable.bg_motherland);

            //设置作者
            TextView author=helper.getView(R.id.tv_publisher);
            author.setText(item.getAuthor());
            //设置日期
            TextView date=helper.getView(R.id.tv_date);
            date.setText(Time.CalculateTime(item.getDateofpublication()));
        }
    }
}
