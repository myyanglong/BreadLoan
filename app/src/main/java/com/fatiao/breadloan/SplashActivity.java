package com.fatiao.breadloan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.fatiao.breadloan.dummy.LoginActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {

    private static final int GO_HOME = 0;//去主页
    private static final int GO_LOGIN = 1;//去登录页
    /**
     * 跳转判断
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME://去主页
                    Intent intent = new Intent(SplashActivity.this, com.fatiao.breadloan.MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case GO_LOGIN://去登录页

                    Intent intent2 = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
                case 10001:
                    try{
                        List<String> datalist=(List<String>)msg.obj;
                        String data=datalist.get(0);
                        JSONObject jsonObject=new JSONObject(data);
                        String message = jsonObject.optString("message");
                        if (message.equals("登陆成功")) {
                           Thread.sleep(3000);
                            com.fatiao.breadloan.UserManage.getInstance().SetAccessToken(SplashActivity.this,jsonObject.optString("accessToken"));
                            Intent spintent = new Intent(SplashActivity.this, com.fatiao.breadloan.MainActivity.class);
                            startActivity(spintent);
                            finish();
                        }
                        else {
                            Toast.makeText(SplashActivity.this,"自动登录失败请重新登录",Toast.LENGTH_LONG).show();
                            Intent spintent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(spintent);
                            finish();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
            if (com.fatiao.breadloan.UserManage.getInstance().hasUserInfo(this))//自动登录判断，SharePrefences中有数据，则跳转到主页，没数据则跳转到登录页
            {
                com.fatiao.breadloan.UserInfo userInfo = com.fatiao.breadloan.UserManage.getInstance().getUserInfo(SplashActivity.this);
                List<com.fatiao.breadloan.OkHttpClientManager.Param> params = new ArrayList<>();
                params.add(new com.fatiao.breadloan.OkHttpClientManager.Param("userCode", userInfo.getUserName()));
                params.add(new com.fatiao.breadloan.OkHttpClientManager.Param("password", userInfo.getPassword()));
                params.add(new com.fatiao.breadloan.OkHttpClientManager.Param("loginType", "app"));
                try {
                    com.fatiao.breadloan.HttpUtil.sendDataToServer(SplashActivity.this, "http://94.191.7.189/user/login", params, mHandler, 10001);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mHandler.sendEmptyMessageDelayed(GO_LOGIN, 3000);
            }



    }
}
