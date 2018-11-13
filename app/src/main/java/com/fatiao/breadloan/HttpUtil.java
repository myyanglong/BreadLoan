package com.fatiao.breadloan;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Request;
import com.fatiao.breadloan.OkHttpClientManager.Param;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络请求，与服务器交互
 * @author home
 *
 */
public class HttpUtil {

	
	private static int HTTP_ERROR = 4001,PARAM_ERROR = 4002;;
	private static String http_error="系统异常：网络连接超时或出错";

	/**
	 * HTTP POST请求方式，请求服务器
	 * @param context
	 * @param serverMethod
	 * @param paramsList
	 * @param handler
	 * @param Back_Code
	 * @throws Exception
	 */
	public static void sendDataToServer(Context context, final String serverMethod, List<Param> paramsList, final Handler handler, final int Back_Code) throws Exception{
		final String url = serverMethod;
		Log.e("请求的接口", url);
		Param[] params = new Param[paramsList.size()];
		for(int i=0;i<paramsList.size();i++){
			params[i] = paramsList.get(i);
			Log.d("请求的参数", paramsList.get(i).value + "");
		}

		OkHttpClientManager.getInstance().getPostDelegate().postAsyn(url, params, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				e.printStackTrace();
				System.out.println("出错的URL：" + url);
				//网络请求错误
				Message msg = handler.obtainMessage();
				msg.what = HTTP_ERROR;
				msg.obj = http_error;

				handler.sendMessage(msg);
			}

			@Override
			public void onResponse(String response) {
				Log.e("数据请求", response);
				Message msg = handler.obtainMessage();
				List<String> list = new ArrayList<String>();
				list.add(response);
				msg.what = Back_Code;
				msg.obj = list;
				handler.sendMessage(msg);
			}
		});
	}


	/**
	 * HTTP POST请求方式，请求服务器
	 * @param context
	 * @param serverMethod
	 * @param paramsList
	 * @param handler
	 * @param Back_Code
	 * @throws Exception
	 */
	public static void sendDataToServer(Context context, final String serverMethod, List<Param> paramsList, final Handler handler, final int Back_Code,String userCode,long time,String key) throws Exception{
		final String url = serverMethod;
			Log.e("请求的接口", url);
		Param[] params = new Param[paramsList.size()];
		for(int i=0;i<paramsList.size();i++){
			params[i] = paramsList.get(i);
			Log.d("请求的参数", paramsList.get(i).value + "");
		}

		OkHttpClientManager.getInstance().getPostDelegate().postAsyn(url, params, userCode, time, key, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				e.printStackTrace();
				System.out.println("出错的URL：" + url);
				//网络请求错误
				Message msg = handler.obtainMessage();
				msg.what = HTTP_ERROR;
				msg.obj = http_error;

				handler.sendMessage(msg);
			}

			@Override
			public void onResponse(String response) {
					Log.e("数据请求", response);
				Message msg = handler.obtainMessage();
				List<String> list = new ArrayList<String>();
				list.add(response);
				msg.what = Back_Code;
				msg.obj = list;
				handler.sendMessage(msg);
			}
		});
	}



	/**
	 * 上传文件操作
	 * @param serverMethod
	 * @param paramsList_file
	 * @param paramsList_data
	 * @param handler
	 * @param Back_Code
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static void onlyUploadFile(final String serverMethod,List<Param> paramsList_file,List<Param> paramsList_data,final Handler handler, final int Back_Code) throws Exception{
//		String url = SystemConstant.HTTP_HEAD + serverMethod;
		String url = serverMethod;
		String[] fileKeys = new String[paramsList_file.size()];
		File[] files = new File[paramsList_file.size()];
		Param[] params = new Param[paramsList_data.size()];
		for(int i=0;i<paramsList_data.size();i++){
			params[i] = paramsList_data.get(i);
			Log.d("请求的参数", paramsList_data.get(i).key + ":" + paramsList_data.get(i).value + "");
		}
		for(int i=0;i<paramsList_file.size();i++){
			fileKeys[i] = paramsList_file.get(i).key;
			files[i] = new File(paramsList_file.get(i).value);
		}
		Log.d("文件", fileKeys.length + "");
		Log.d("文件", files.length + "");
		OkHttpClientManager.getInstance().getUploadDelegate().postAsyn(url, fileKeys, files, params, new OkHttpClientManager.ResultCallback<String>(){
			@Override
			public void onError(Request request, Exception e) {
				e.printStackTrace();
				//网络请求错误
				Message msg = handler.obtainMessage();
				msg.what = HTTP_ERROR;
				msg.obj = http_error;
				handler.sendMessage(msg);
			}

			@Override
			public void onResponse(String response) {
				Log.e("数据请求", response);
				Message msg = handler.obtainMessage();
				List<String> list = new ArrayList<String>();
				list.add(response);
				msg.obj = list;
				handler.sendMessage(msg);
			}

		}, null);
	}

	/**
	 * HTTP GET请求方式，请求服务器
	 * @param url
	 * @param handler
	 * @param Back_Code
	 * @throws Exception
	 */
	public static void getQuery(String url,String handers,final Handler handler,final int Back_Code) throws Exception{
		OkHttpClientManager.getInstance().getGetDelegate().getAsyn(url, new OkHttpClientManager.ResultCallback<String>() {

			@Override
			public void onError(Request request, Exception e) {
				Message msg = handler.obtainMessage();
				msg.what = HTTP_ERROR;
				msg.obj = http_error;
				handler.sendMessage(msg);

			}

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
			//	Log.e("sad", response);
				Message msg = handler.obtainMessage();
				msg.what = Back_Code;
				msg.obj = response;
				handler.sendMessage(msg);
			}
		},handers);
	}
}