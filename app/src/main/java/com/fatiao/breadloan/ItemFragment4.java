package com.fatiao.breadloan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.fatiao.breadloan.dummy.DummyContent.DummyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment4 extends Fragment {

    private WebView webView;

    private Handler handler=new Handler() {
        @SuppressLint("JavascriptInterface")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 8000:
//                    "id":"1","name":"欣欣钱包","label":"欣欣钱包真皮甩卖假一赔十","minLimit":1000,"maxLimit":10000,"rate":0.01,
//                        "url":"http://baidu.com","picturePath":null,"remark":null,"createTime":null,"createUser":null,"updateTime":null,
//                        "updateUser":null,"isDelete":"0"},

                    try {
                        JSONArray array = new JSONArray((String) msg.obj);
                        for (int i = 0; i < array.length(); i++) {
                            FramgtData data=new FramgtData();
                            JSONObject object =array.getJSONObject(i);
                            data.setName(object.optString("name"));
                            data.setLabel(object.optString("label"));
                            data.setMinLimit(object.optString("minLimit"));
                            data.setMaxLimit(object.optString("maxLimit"));
                            data.setRate(object.optString("rate"));
                            data.setUrl(object.optString("url"));
                            if (i==0&&data.getUrl()!=null&&!data.getUrl().equals(""))
                            {

                                webView.loadUrl(data.getUrl());//加载url
                                webView.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端

                                WebSettings webSettings=webView.getSettings();


//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String  request) {
//                view.loadUrl(request);
//                return true;
//            }
//        });
                                webSettings.setJavaScriptEnabled(true);//允许使用js
                                /**
                                 * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
                                 * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
                                 * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
                                 * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
                                 */
                                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
                                //支持屏幕缩放
                                webSettings.setSupportZoom(true);
                                webSettings.setBuiltInZoomControls(true);
//
//                                Intent intent=new Intent();
//                                intent.setClass(getContext(), WebViewActivity.class);
//                                intent.putExtra("url",data.getUrl());
//                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getContext(),"暂未获取到数据",Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                    break;
            }
        }


    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list4, container, false);
        webView=view.findViewById(R.id.fragment4_webview);
        getListviewData();
        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    public void getListviewData() {
        try {
            HttpUtil.getQuery("http://94.191.7.189/online-credit/get-all/4",UserManage.getInstance().GetAccessToken(getContext()), handler, 8000);        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
