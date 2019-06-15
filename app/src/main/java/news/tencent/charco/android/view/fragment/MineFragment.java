package news.tencent.charco.android.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.goka.blurredgridmenu.GridMenuFragment;

import news.tencent.charco.android.R;
import news.tencent.charco.android.base.BaseFragment;
import news.tencent.charco.android.utils.HttpUtil;
import news.tencent.charco.android.view.activity.MainActivity;
import news.tencent.charco.android.view.activity.SMSActivity;

/**
 * Created 18/7/5 11:09
 * Author:charcolee
 * Version:V1.0
 * ----------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------
 */

public class MineFragment extends BaseFragment {
    private GridMenuFragment mGridMenuFragment;

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button login = getActivity().findViewById(R.id.logins);
        /*mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.back);*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PsdLoginActivity.class);
                startActivity(intent);
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    SMSActivity.getCode("13609014704");
                    Looper.prepare();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"发送成功,请注意接收",Toast.LENGTH_LONG).show();
                        }
                    });
                    Looper.loop();
                }
            }).start();*/
            }
        });
    }
}
