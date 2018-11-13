package com.fatiao.breadloan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.fatiao.breadloan.dummy.DummyContent.DummyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment2 extends Fragment {
    private ListView listview;
    private List<FramgtData> dataList;
    private ImageView imageView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 8000:
                    dataList.clear();
                    try {
                        JSONArray array = new JSONArray((String) msg.obj);
                        for (int i = 0; i < array.length(); i++) {
                            FramgtData data=new FramgtData();
                            JSONObject object =array.getJSONObject(i);
                            data.setId(object.optString("id"));
                            data.setName(object.optString("name"));
                            data.setLabel(object.optString("label"));
                            data.setMinLimit(object.optString("minLimit"));
                            data.setMaxLimit(object.optString("maxLimit"));
                            data.setRate(object.optString("rate"));
                            data.setUrl(object.optString("url"));
                            data.setPicturePath(object.optString("picturePath"));
                            data.setPassRate(object.optString("passRate"));
                            if (data != null) {
                                dataList.add(data);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (dataList.size() > 0) {
                        listview.setAdapter(new MyListAdapter(getContext(), dataList));
                    } else {
                        Toast.makeText(getContext(), "暂未获取到数据", Toast.LENGTH_SHORT).show();
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
        View view = inflater.inflate(R.layout.fragment_item_list2, container, false);
        dataList = new ArrayList<>();
        listview = view.findViewById(R.id.list_framen2);
        getListviewData();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setClass(getContext(), WebViewActivity.class);
                intent.putExtra("url",dataList.get(i).getUrl());
                startActivity(intent);
            }
        });
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
            HttpUtil.getQuery("http://94.191.7.189/online-credit/get-all/2",UserManage.getInstance().GetAccessToken(getContext()), handler, 8000);        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
