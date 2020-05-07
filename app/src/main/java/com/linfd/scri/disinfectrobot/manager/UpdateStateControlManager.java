package com.linfd.scri.disinfectrobot.manager;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.linfd.scri.disinfectrobot.Contanst;
import com.linfd.scri.disinfectrobot.GsonUtil;
import com.linfd.scri.disinfectrobot.Tools;
import com.linfd.scri.disinfectrobot.entity.DataEntity;
import com.linfd.scri.disinfectrobot.entity.RobotStatusCallbackEntity;
import com.linfd.scri.disinfectrobot.observer.DataChanger;
import com.linfd.scri.disinfectrobot.observer.DataWatcher;


/*
* 状态管理类  输入地图箭头bitmap
* */
public class UpdateStateControlManager {
    public static final String TAG = UpdateStateControlManager.class.getSimpleName();
    private static  UpdateStateControlManager ourInstance;

    private Rect rect = new Rect();//覆盖的位置  运行的位置
    private BitmapCallback bitmapCallback;
    private double map_update = -1;

    public static UpdateStateControlManager getInstance() {
        if (ourInstance == null){
            synchronized (UpdateStateControlManager.class){
                if (ourInstance == null){
                    ourInstance = new UpdateStateControlManager();
                }
            }
        }
        return ourInstance;
    }

    private UpdateStateControlManager() {
        DataChanger.getInstance().addObserver(watcher);
    }

    public void setBitmapCallback(BitmapCallback bitmapCallback) {
        this.bitmapCallback = bitmapCallback;
    }

    /*
    * 必须先初始化
    * */
    private void init(){
       // DataChanger.getInstance().addObserver(watcher);
    }
    private DataWatcher watcher = new DataWatcher() {

        @Override
        public void notifyUpdata(Object data) {
            if (data instanceof DataEntity) {
                DataEntity dataEntity = (DataEntity) data;
                if (dataEntity.getType().equalsIgnoreCase(Contanst.robot_status)) {

                    try {
                        Log.e("linfd", "机器人状态");
                        Log.e("linfd", dataEntity.getMessage());
                        final RobotStatusCallbackEntity satusEntity = GsonUtil.GsonToBean(dataEntity.getMessage(), RobotStatusCallbackEntity.class);
//
                        updateLocation(satusEntity);
                        Log.e("linfd", Tools.getDateToString((long) satusEntity.get_hand_map_update()));
                        double map_update_now = satusEntity.get_hand_map_update();
                        //大于5分钟
                                if (map_update_now != map_update && (((System.currentTimeMillis()-map_update_now) > Contanst.GETMAPFREQUENCY) || (System.currentTimeMillis()-map_update_now)<0)){
                                    map_update = map_update_now;
                                    MapDataObtainManager.getInstance().start();
                                    Log.e(TAG,"重新获取地图");
                                }else if (map_update_now != map_update){//要删掉 测试
                                    double d= System.currentTimeMillis()- map_update_now;
                                    Log.e(TAG,"时间间隔是（测试）"+d);
                                }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("linfd", e.getMessage());
                    }
                }
            }
        }
    };


    private void updateLocation(final RobotStatusCallbackEntity satusEntity) {
        if (satusEntity == null || Contanst.MAPPARAMENTITY == null) {
            return;
        }
        Tools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Contanst.MAPPARAMENTITY == null) {
                    return;
                }
/*
* 注意：satusEntity.getRobot_pose().get(0)，satusEntity.getRobot_pose().get(1)可能顺序有误
*
* */
                int width = BGSelectorManager.getInstance().getMapWH().get(0);
                int height = BGSelectorManager.getInstance().getMapWH().get(1);
                double left = width - (-(Contanst.MAPPARAMENTITY.getOrigin().get(1) - satusEntity.getRobot_pose().get(1)) / Contanst.MAPPARAMENTITY.getResolution());
                double top = height - (-(Contanst.MAPPARAMENTITY.getOrigin().get(0) - satusEntity.getRobot_pose().get(0)) / Contanst.MAPPARAMENTITY.getResolution());

                //弧度转角度
                float angle = (float) (360 * satusEntity.getRobot_pose().get(3) / (2 * Math.PI));
                rect.left = (int) left;
                rect.top = (int) top;

                Log.e("linfd",rect.left+"=======left");
                Log.e("linfd",rect.top+"=======ltop");
                /*
                 * 解析出定位点位置和角度，画上去
                 * */
                ComBitmapManager.getInstance().startComposite(rect, angle, new ComBitmapManager.CompositeMapListener() {
                    @Override
                    public void compositeMapCallBack(Bitmap mapComposite) {
                        if (bitmapCallback != null) {
                            bitmapCallback.bitmapFinish(mapComposite);
                        }

                    }
                });
            }
        });
    }

    public interface  BitmapCallback {
       void  bitmapFinish(Bitmap bitmap);
    }
}