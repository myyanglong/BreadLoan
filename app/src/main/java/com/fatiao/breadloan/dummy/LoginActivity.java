package com.fatiao.breadloan.dummy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fatiao.breadloan.EmptyUtils;
import com.fatiao.breadloan.HmacSha1Signature;
import com.fatiao.breadloan.HttpUtil;
import com.fatiao.breadloan.MainActivity;
import com.fatiao.breadloan.OkHttpClientManager;
import com.fatiao.breadloan.R;
import com.fatiao.breadloan.UserManage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

public class LoginActivity extends Activity implements View.OnClickListener {
    private LinearLayout LinSin, LinRegister, sintxtbg, registertxtbg, LinLoginSin, LinRegisterRin,Lelat_Ver;
    private RelativeLayout Relatpasswrod;
    private TextView sintxt, registertxt, gettime,logingettime;
    private EditText RegisterPhone, RegisterVer, RegisterPassword;
    private EditText LoginPhone, LoginPasswrod,LoginVer;
    private String Phone, Password;
    private String ReginPhone, ReginPassword, Reginyzm;
    private String BizId;
    private boolean YzmLogin=false;//false密码登录 ture 验证码登录
    private final int LOGINPOST = 1001;
    private final int REGISTER = 1002;
    private String getVer;//获取的6位数的随机验证码
    private String GetVerPhone;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGINPOST:
                    try{
                        List<String> datalist=(List<String>)msg.obj;
                        String data=datalist.get(0);
                        JSONObject jsonObject=new JSONObject(data);
                        String message = jsonObject.optString("message");
                        if (message.equals("登陆成功")) {
                            UserManage.getInstance().saveUserInfo(LoginActivity.this, Phone, Password);
                            UserManage.getInstance().SetAccessToken(LoginActivity.this,jsonObject.optString("accessToken"));
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    break;
                case REGISTER:
                    try {
                        List<String> datalist = (List<String>) msg.obj;
                        String data = datalist.get(0);
                        JSONObject jsonObject = new JSONObject(data);
                        String message = jsonObject.optString("message");
                        if (message.equals("注册成功")) {
                            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                            sintxt.setTextColor(getResources().getColor(R.color.select));
                            registertxt.setTextColor(getResources().getColor(R.color.defaul));
                            registertxtbg.setVisibility(View.INVISIBLE);
                            sintxtbg.setVisibility(View.VISIBLE);
                            LinLoginSin.setVisibility(View.VISIBLE);
                            LinRegisterRin.setVisibility(View.GONE);
                            Relatpasswrod.setVisibility(View.VISIBLE);
                            Lelat_Ver.setVisibility(View.GONE);
                            YzmLogin=false;
                            LoginPhone.setText(ReginPhone);
                            LoginPasswrod.setText(ReginPassword);
                        }
                        else {
                            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitView();
    }

    private void InitView() {
        findViewById(R.id.login_paswordsin).setOnClickListener(this);
        findViewById(R.id.login_registersin).setOnClickListener(this);
        findViewById(R.id.login_login).setOnClickListener(this);
        findViewById(R.id.qiehuan).setOnClickListener(this);
        findViewById(R.id.register_register).setOnClickListener(this);
        findViewById(R.id.register_loginyzmtd).setOnClickListener(this);
        Relatpasswrod=findViewById(R.id.Relat_passwrod);
        Lelat_Ver=findViewById(R.id.Linear_Ver);
        LoginVer=findViewById(R.id.register_loginyzmtd);
        gettime = findViewById(R.id.register_btn_get);
        logingettime=findViewById(R.id.login_btn_getver);
        logingettime.setOnClickListener(this);
        gettime.setOnClickListener(this);
        sintxt = findViewById(R.id.login_paswordtxt);
        registertxt = findViewById(R.id.login_registertxt);
        sintxtbg = findViewById(R.id.login_passwordtxtbg);
        registertxtbg = findViewById(R.id.login_registertxtbg);
        registertxtbg.setVisibility(View.INVISIBLE);
        LinLoginSin = findViewById(R.id.linlogin_sin);
        LinRegisterRin = findViewById(R.id.linregister_sin);

        LoginPhone = findViewById(R.id.login_uid);
        LoginPasswrod = findViewById(R.id.login_pwd);

        RegisterPhone = findViewById(R.id.register_uid);
        RegisterVer = findViewById(R.id.register_yzmtd);
        RegisterPassword = findViewById(R.id.register_pwd);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.qiehuan://切换到验证码登录
                if (YzmLogin)
                {
                    Relatpasswrod.setVisibility(View.VISIBLE);
                    Lelat_Ver.setVisibility(View.GONE);
                    YzmLogin=false;
                }
                else {
                    Relatpasswrod.setVisibility(View.GONE);
                    Lelat_Ver.setVisibility(View.VISIBLE);
                    YzmLogin=true;
                }

                break;
            case R.id.login_paswordsin:
                sintxt.setTextColor(getResources().getColor(R.color.white));
                registertxt.setTextColor(getResources().getColor(R.color.defaul));
                registertxtbg.setVisibility(View.INVISIBLE);
                sintxtbg.setVisibility(View.VISIBLE);
                LinLoginSin.setVisibility(View.VISIBLE);
                LinRegisterRin.setVisibility(View.GONE);
                break;
            case R.id.login_registersin:
                registertxt.setTextColor(getResources().getColor(R.color.white));
                sintxt.setTextColor(getResources().getColor(R.color.defaul));
                sintxtbg.setVisibility(View.INVISIBLE);
                registertxtbg.setVisibility(View.VISIBLE);
                LinRegisterRin.setVisibility(View.VISIBLE);
                LinLoginSin.setVisibility(View.GONE);
                break;
            case R.id.login_login:
                Phone = LoginPhone.getText().toString().trim();
                if (Empty(Phone)) {
                        if (YzmLogin) {
                            if (GetVerPhone.equals(Phone)) {
                            if (getVer.equals(LoginVer.getText().toString().trim())) {
                                List<OkHttpClientManager.Param> params = new ArrayList<>();
                                params.add(new OkHttpClientManager.Param("userCode", Phone));
                                long time = new Date().getTime();
                                try {
                                    HttpUtil.sendDataToServer(LoginActivity.this, "http://94.191.7.189/user/login", params, handler, LOGINPOST, Phone, time, HmacSha1Signature.signature(Phone, time));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "请输入正确的验证码", Toast.LENGTH_LONG).show();
                            }
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "请输入获取验证码手机号", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Password = LoginPasswrod.getText().toString().trim();
                            if (EmptyUtils.isNotEmpty(Password)) {
                                List<OkHttpClientManager.Param> params = new ArrayList<>();
                                params.add(new OkHttpClientManager.Param("userCode", Phone));
                                params.add(new OkHttpClientManager.Param("password", Password));
                                params.add(new OkHttpClientManager.Param("loginType", "app"));
                                try {
                                    HttpUtil.sendDataToServer(LoginActivity.this, "http://94.191.7.189/user/login", params, handler, LOGINPOST);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                            }
                        }

                }
                break;
            case R.id.register_register://注册

                ReginPhone = RegisterPhone.getText().toString().trim();
                if (GetVerPhone.equals(ReginPhone))
                {
                Reginyzm = RegisterVer.getText().toString().trim();
                ReginPassword = RegisterPassword.getText().toString().trim();
          if (Empty(ReginPhone,Reginyzm,ReginPassword)) {
              if (getVer.equals(Reginyzm)) {
                  long time = new Date().getTime();
                  List<OkHttpClientManager.Param> params = new ArrayList<>();
                  params.add(new OkHttpClientManager.Param("userCode", ReginPhone));
                  params.add(new OkHttpClientManager.Param("password", ReginPassword));
                  params.add(new OkHttpClientManager.Param("userName", ""));
                  params.add(new OkHttpClientManager.Param("sex", ""));
                  params.add(new OkHttpClientManager.Param("mobilePhone", ""));
                  params.add(new OkHttpClientManager.Param("mail", ""));
                  params.add(new OkHttpClientManager.Param("city", ""));
                  params.add(new OkHttpClientManager.Param("district", ""));
                  try {
                      HttpUtil.sendDataToServer(LoginActivity.this, "http://94.191.7.189/user/register", params, handler, REGISTER, ReginPhone, time, HmacSha1Signature.signature(ReginPhone, time));
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              } else {
                  Toast.makeText(LoginActivity.this, "请输入正确的验证码", Toast.LENGTH_LONG).show();
              }
          }
          else {
              Toast.makeText(LoginActivity.this, "请正确输入获取验证码手机号", Toast.LENGTH_LONG).show();
          }
          }
          break;
            case R.id.register_btn_get:
                ReginPhone = RegisterPhone.getText().toString().trim();
                GetVerPhone=ReginPhone;
                if (Empty(ReginPhone)) {
                    getYZM(ReginPhone,"SMS_149145087");
                    CountDownTimer timer = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            gettime.setEnabled(false);
                            gettime.setTextColor(getResources().getColor(R.color.defaul));
                            gettime.setText("已发送(" + millisUntilFinished / 1000 + ")");
                        }

                        @Override
                        public void onFinish() {
                            gettime.setEnabled(true);
                            gettime.setTextColor(getResources().getColor(R.color.select));
                            gettime.setText("重新获取验证码");
                        }
                    }.start();
                }

                break;

            case R.id.login_btn_getver:
                Phone = LoginPhone.getText().toString().trim();
                GetVerPhone=Phone;
                if (Empty(Phone))
                {
                    getYZM(Phone,"SMS_149145089");
                    CountDownTimer timer = new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            logingettime.setEnabled(false);
                            logingettime.setTextColor(getResources().getColor(R.color.defaul));
                            logingettime.setText("已发送(" + millisUntilFinished / 1000 + ")");
                        }

                        @Override
                        public void onFinish() {
                            logingettime.setEnabled(true);
                            logingettime.setTextColor(getResources().getColor(R.color.select));
                            logingettime.setText("重新获取验证码");
                        }
                    }.start();
                }
                break;
        }

    }

    private boolean getYZM(final String Phone, final String SMS) {
        final int getYzmm=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        //
        //cQFSy6bJ7ZYcLEOiEGd3yrjMO5eZEx
        final String accessKeyId = "LTAIlE5vPDTJjBQr";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "cQFSy6bJ7ZYcLEOiEGd3yrjMO5eZEx";//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers(Phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("佛系钱包");
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(SMS);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
                getVer=Random();
                request.setTemplateParam("{\"name\":\"Tom\", \"code\":\""+getVer+"\"}");
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");
        //请求失败这里会抛ClientException异常
        //SendSmsResponse sendSmsResponse;
        try {
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                BizId = sendSmsResponse.getBizId();
                //请求成功
            } else {
                Log.e("错误信息", sendSmsResponse.getMessage());
            }

        } catch (ServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            }
        }).start();
        return false;
    }

    /**
     * 获取随机验证码
     *
     * @return 6位数String验证码
     */
    private String Random() {
        String sources = "0123456789"; // 加上一些字母，就可以生成pc站的验证码了
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < 6; j++) {
            flag.append(sources.charAt(rand.nextInt(9)) + "");
        }
        return flag.toString();
    }

    /**
     * 判断是否是手机号
     *
     * @param mobile
     * @return
     */
    public  boolean isMobile(String mobile) {
        String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    public boolean Empty(String phone ,String password,String Ver)
    {
        if (EmptyUtils.isEmpty(phone))
        {
            Toast.makeText(LoginActivity.this,"请输入手机号",Toast.LENGTH_LONG).show();
            return false;
        }
        if (EmptyUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
            return false;
        }
        if (EmptyUtils.isEmpty(Ver))
        {
            Toast.makeText(LoginActivity.this,"请输入验证码",Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isMobile(phone))
        {
            Toast.makeText(LoginActivity.this,"请输入正确的手机号",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public boolean Empty(String phone )
    {
        if (EmptyUtils.isEmpty(phone))
        {
            Toast.makeText(LoginActivity.this,"请输入手机号",Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isMobile(phone))
        {
            Toast.makeText(LoginActivity.this,"请输入正确的手机号",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}