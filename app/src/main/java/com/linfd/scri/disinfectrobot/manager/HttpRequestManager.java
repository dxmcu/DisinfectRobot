package com.linfd.scri.disinfectrobot.manager;

import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linfd.scri.disinfectrobot.BaseApplication;
import com.linfd.scri.disinfectrobot.Contanst;
import com.linfd.scri.disinfectrobot.entity.BaseEntity;
import com.linfd.scri.disinfectrobot.entity.BitoLoginEntity;
import com.linfd.scri.disinfectrobot.entity.CancelTaskEntity;
import com.linfd.scri.disinfectrobot.entity.ChangePwbEntity;
import com.linfd.scri.disinfectrobot.entity.GetAllTasksEntity;
import com.linfd.scri.disinfectrobot.entity.GetErrorCodeEntity;
import com.linfd.scri.disinfectrobot.entity.GetHanxinStatusEntity;
import com.linfd.scri.disinfectrobot.entity.RobotRegisterEntity;
import com.linfd.scri.disinfectrobot.entity.RobotUnregisterEntity;
import com.linfd.scri.disinfectrobot.entity.TaskStatusEntity;
import com.linfd.scri.disinfectrobot.listener.HttpCallbackEntity;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpRequestManager {

    public static final String TAG = HttpRequestManager.class.getSimpleName();

    private static volatile HttpRequestManager ourInstance;
    private  MyOkHttp mMyOkHttp;
    private  Gson gson;

    /*
    * 构造函数
    * */
    public HttpRequestManager() {
         gson = new Gson();
    }

    public static HttpRequestManager getInstance(){
        if (ourInstance == null){
            synchronized (HttpRequestManager.class){
                if (ourInstance == null){
                    ourInstance = new HttpRequestManager();
                }
            }
        }
        return ourInstance;
    }

    public  void init(){
//持久化存储cookie
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApplication.getApplication()));

        //log拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //自定义OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)       //设置开启cookie
                .addInterceptor(logging)            //设置开启log
                .build();
        mMyOkHttp = new MyOkHttp(okHttpClient);
    }

    public void cancel(){
        mMyOkHttp.cancel(this);     //tag 即之前请求时传入的tag 建议直接将页面作为object传入
    }
    /*
    * 查询任务运⾏状态 - 根据任务id
    * */
    public <T> void  get_task_status(int id, final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_get_task_status + id;


        mMyOkHttp.get()
                .url(url)
//                        .addParam("name", "tsy")
//                        .addParam("id", "5")
                .tag(this)
                .enqueue(new GsonResponseHandler<TaskStatusEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, TaskStatusEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });


    }
    /*
    * 启动韩信
    * */
    public <T> void hanxin_start(final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_hanxin_start;
        mMyOkHttp.get()
                .url(url)
                .tag(this)
                .enqueue(new GsonResponseHandler<BaseEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, BaseEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }

    /*
    * 关闭韩信
    * */
    public <T >void hanxin_stop(final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_hanxin_stop;
        mMyOkHttp.get()
                .url(url)
                .tag(this)
                .enqueue(new GsonResponseHandler<BaseEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, BaseEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }

    /*
     * 查询韩信状态
     * */
    public <T >void get_hanxin_status(final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_get_hanxin_status;
        mMyOkHttp.get()
                .url(url)
                .tag(this)
                .enqueue(new GsonResponseHandler<GetHanxinStatusEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, GetHanxinStatusEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }


    /*
    * 注册机器人
    * */
    public <T> void robot_register(final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_robot_register + Contanst.ROBOT_SERIAL;
        mMyOkHttp.get()
                .url(url)
                .tag(this)
                .enqueue(new GsonResponseHandler<RobotRegisterEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, RobotRegisterEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }

    /*
    *查询所有任务信息
    * */
    public <T> void get_all_tasks(final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_get_all_tasks;
        mMyOkHttp.get()
                .url(url)
                .tag(this)
                .enqueue(new GsonResponseHandler<GetAllTasksEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, GetAllTasksEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }

    /*
    * 重复任务
    * */
    public <T> void repeat_tasks(int taskId ,  final HttpCallbackEntity<T> httpCallbackEntity){

        String url = Contanst.api_repeat_tasks;
        Map<String, Object> map = new HashMap<>();
        List<Integer> id_list = new ArrayList<>();
        id_list.add(taskId);

        map.put("repeat_num", 1);
        map.put("id_list", id_list);

        mMyOkHttp.post()
                .url(url)
                .tag(this).jsonParams(gson.toJson(map))
                .enqueue(new GsonResponseHandler<BaseEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, BaseEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }

    /*
    * 充电模式  1:纯手动，2:半自动，3:纯自动，4:混动
    * */
    public <T> void switch_charging_mode(int mode,final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_switch_charging_mode + mode;
        mMyOkHttp.get()
                .url(url)
                .tag(this)
                .enqueue(new GsonResponseHandler<CancelTaskEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, CancelTaskEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }

    /*
    * 取消任务
    * */
    public <T> void cancel_task(int taskId,final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_cancel_task + taskId;
        mMyOkHttp.get()
                .url(url)
                .tag(this)
                .enqueue(new GsonResponseHandler<CancelTaskEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, CancelTaskEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }

    /*
    *查询所有故障信息
    * */

    public <T> void get_error_code(final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_get_error_code;
        mMyOkHttp.get()
                .url(url)
                .tag(this)
                .enqueue(new GsonResponseHandler<GetErrorCodeEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, GetErrorCodeEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }

    /*
    * 登录
    * */
    public <T> void login(String username,String password,final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_login;
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        mMyOkHttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<BitoLoginEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, BitoLoginEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }

    /*
    * 修改密码
    * */

    public <T> void changePwb(String username,String password,String oldPassword,String passwordConfirm,final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_changePwb;
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("oldPassword", oldPassword);
        params.put("passwordConfirm", passwordConfirm);
        mMyOkHttp.post()
                .url(url)
                .params(params)
                .tag(this)
                .enqueue(new GsonResponseHandler<ChangePwbEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, ChangePwbEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }
    /*
    * 重置机器人
    * */
    public <T> void reset_agents(final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_reset_agents;
        mMyOkHttp.get()
                .url(url)
                .tag(this)
                .enqueue(new GsonResponseHandler<BaseEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, BaseEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }

    /*
    * 注销机器人
    * */
    public <T> void robot_unregister(String serial,final HttpCallbackEntity<T> httpCallbackEntity){
        String url = Contanst.api_robot_unregister + serial;
        mMyOkHttp.get()
                .url(url)
                .tag(this)
                .enqueue(new GsonResponseHandler<RobotUnregisterEntity>() {

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        httpCallbackEntity.onFailure();
                    }

                    @Override
                    public void onSuccess(int statusCode, RobotUnregisterEntity response) {

                        httpCallbackEntity.onSuccess((T) response);

                    }
                });
    }


}