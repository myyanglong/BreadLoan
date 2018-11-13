package com.fatiao.breadloan;


import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyListAdapter extends BaseAdapter {
    private Context context;
    private List<FramgtData> list;
    private LayoutInflater inflater;
    public MyListAdapter(Context context, List<FramgtData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_item, viewGroup, false);
            holder=new MyViewHolder();
            holder.imageView = convertView.findViewById(R.id.item_imgview);
            holder.item1 = convertView.findViewById(R.id.list_item1);
            TextPaint tp = holder.item1.getPaint();

            tp.setFakeBoldText(true);
            holder.item2 = convertView.findViewById(R.id.list_item2);
            holder.item3 = convertView.findViewById(R.id.list_item3);
        //    holder.item4 = convertView.findViewById(R.id.list_item4);
            holder.item5 = convertView.findViewById(R.id.list_item5);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        FramgtData data = (FramgtData) getItem(position);
        if (data.getPicturePath()!=null&&!data.getPicturePath().equals("")) {
            String imgviewurl="http://94.191.7.189/online-credit/get-icon/"+data.getId();
//            String accesstoken=UserManage.getInstance().GetAccessToken(context);
//            Log.e("-------ACCESS_TOKEN:",accesstoken);
//            GlideUrl glideUrl = new GlideUrl(imgviewurl, new LazyHeaders.Builder()
//                    .addHeader("ACCESS_TOKEN",accesstoken )
//                    .build());
            Glide.with(context).load(imgviewurl).into(holder.imageView);
        }
     //   holder.imageView.setImageResource(Integer.parseInt(data.getImageviewUrl()));
        holder.item1.setText(data.getName());
        holder.item2.setText(data.getLabel());
        holder.item3.setText(data.getMinLimit()+"-"+data.getMaxLimit());
       // holder.item4.setText(data.getPassRate()+"%");
        holder.item5.setText(data.getRate()+"%");

        return convertView;
    }

    public static class MyViewHolder {
        TextView item1, item2, item3, item5;
        ImageView imageView;
    }
}
