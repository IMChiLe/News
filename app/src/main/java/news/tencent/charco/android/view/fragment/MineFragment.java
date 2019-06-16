package news.tencent.charco.android.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goka.blurredgridmenu.GridMenuFragment;

import news.tencent.charco.android.R;
import news.tencent.charco.android.base.BaseFragment;

import static android.app.Activity.RESULT_OK;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    if(data.getStringExtra("name") != null){
                        String name = data.getStringExtra("name");
                        RelativeLayout relativeLayout = findViewById(R.id.loginLayout);
                        RelativeLayout successLoginLayout = findViewById(R.id.successLoginLayout);
                        TextView textView = findViewById(R.id.successName);
                        if(name != null){
                            relativeLayout.setVisibility(View.GONE);
                            successLoginLayout.setVisibility(View.VISIBLE);
                            textView.setText(name);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button login = getActivity().findViewById(R.id.logins);
        /*mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.back);*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CodeLoginActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }
}
