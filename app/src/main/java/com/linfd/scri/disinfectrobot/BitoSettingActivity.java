package com.linfd.scri.disinfectrobot;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.linfd.scri.disinfectrobot.entity.GetRobotPerformTaskEntity;
import com.linfd.scri.disinfectrobot.manager.BitoActionStateManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BitoSettingActivity extends BaseActivity {
    private EditText et_repeat,et_ssid,et_password;
    private Button bt_sure;

    @Override
    public void initView() {
        setContentView(R.layout.activity_bito_setting);
        et_repeat = findViewById(R.id.et_repeat);
        et_ssid = findViewById(R.id.et_ssid);
        et_password = findViewById(R.id.et_password);
        bt_sure = findViewById(R.id.bt_sure);
        super.initView();
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    /*
              正在执行的任务状态
        * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(GetRobotPerformTaskEntity entity) {


    }

    @Override
    protected void initListener() {
        super.initListener();
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_repeat.getText().toString())){
                    Contanst.repeat_num =  Integer.valueOf(et_repeat.getText().toString());
                }
                if (!TextUtils.isEmpty(et_ssid.getText().toString())){
                    Contanst.ssid = et_ssid.getText().toString();
                }
                if (!TextUtils.isEmpty(et_password.getText().toString())){
                    Contanst.passwd =  et_password.getText().toString();
                }
                finish();
            }
        });
    }
}