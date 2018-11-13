package com.fatiao.breadloan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolsUtils {

	public static ImageLoader imageLoader() {
		return ImageLoader.getInstance();
	}





	/**
	 *  pull解析xml数据
	 * @param xmlData xml数据
	 * @return list 集合
	 */
	public static List<String> stringpull(String xmlData) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(xmlData));
			int eventType = xmlPullParser.getEventType();
			List<String> list = new ArrayList<>();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String data = null;
				String nodeName = xmlPullParser.getName();
//	            Log.e("nodeName", nodeName);
				switch (eventType) {
					case XmlPullParser.START_TAG: {
						if ("accessToken".equals(nodeName)) {
							data= xmlPullParser.nextText();
						} else if ("message".equals(nodeName)) {
							data= xmlPullParser.nextText();
						}
						list.add(data);
						break;
					}
					// 完成解析某个结点
					case XmlPullParser.END_TAG: {
						if (data!=null){
							list.add(data);
						}else {
							list.clear();
						}

						break;
					}
					default:
						break;
				}
				eventType = xmlPullParser.next();
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


	/**
	 *  pull解析xml数据
	 * @param xmlData xml数据
	 * @return list 集合
	 */
	public static List<FramgtData> pullParse(String xmlData) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(xmlData));
			int eventType = xmlPullParser.getEventType();



			List<FramgtData> list = new ArrayList<>();

			while (eventType != XmlPullParser.END_DOCUMENT) {
			    FramgtData data =new FramgtData();
				String nodeName = xmlPullParser.getName();
//	            Log.e("nodeName", nodeName);
				switch (eventType) {
					case XmlPullParser.START_TAG: {
						if ("name".equals(nodeName)) {
                            data.setName( xmlPullParser.nextText());
						} else if ("label".equals(nodeName)) {
							data.setLabel(xmlPullParser.nextText());
						} else if ("minLimit".equals(nodeName)) {
							data.setMinLimit( xmlPullParser.nextText());
						} else if ("maxLimit".equals(nodeName) ){
							data.setMaxLimit(xmlPullParser.nextText());
						}else if ("rate".equals(nodeName)) {
							data.setRate( xmlPullParser.nextText());
						}
                        else if ("url".equals(nodeName)) {
                            data.setUrl(xmlPullParser.nextText());
                        }
						break;
					}
					// 完成解析某个结点
					case XmlPullParser.END_TAG: {
						if (data!=null){
							list.add(data);
						}else {
							list.clear();
						}

						break;
					}
					default:
						break;
				}
				eventType = xmlPullParser.next();
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取圆角位图的方法
	 *
	 * @param bitmap
	 *            需要转化成圆角的位图
	 * @param pixels
	 *            圆角的度数，数值越大，圆角越大
	 * @return 处理后的圆角位图
	 */
	public static Bitmap toRoundCornerImage(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		// 抗锯齿
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	//把文件转换成String
	public static String fileToString(String filePath) {
//	    Bitmap bm = getSmallBitmap(filePath);
//	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

		//1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
		//这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
//	    bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = readFileToByteArray(new File(filePath));
		Log.d("d", "压缩后的大小=" + b.length);
		String strBase64 = new String(Base64.encode(b,0));
		return strBase64;
	}

	/**
	 * 将文件转换成byte类型的数组
	 * @param file
	 * @return
	 */
	public static byte[] readFileToByteArray(File file) {
		FileInputStream fileInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] bt = new byte[1024];
			int len = 0;
			while ((len = fileInputStream.read(bt)) != -1) {
				byteArrayOutputStream.write(bt, 0, len);
			}
			return byteArrayOutputStream.toByteArray();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 传入文件名以及字符串, 将字符串信息保存到文件中
	 *
	 * @param strFilename
	 * @param strBuffer
	 */
	public static void TextToFile(final String strFilename, final String strBuffer)
	{
		try
		{
			// 创建文件对象
			File fileText = new File(strFilename);
			// 向文件写入对象写入信息
			FileWriter fileWriter = new FileWriter(fileText);

			// 写文件
			fileWriter.write(strBuffer);
			// 关闭
			fileWriter.close();
		}
		catch (IOException e)
		{
			//
			e.printStackTrace();
		}
	}

	/**
	 * 将String数据存为文件
	 */
	public static File getFileFromBytes(String name,String path) {
		byte[] b=name.getBytes();
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(path);
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 从文件中读取数据
	 * @param fileName 路径
	 * @return 从文件中读取的数据
	 */


	/**
	 * 判断是否是json结构
	 */
	public static boolean isJson(String value) {
		try {
			new JSONObject(value);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				new JSONArray(value);
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
			return true;
		}
		return true;
	}

	public static Spanned HtmlToTextView(String msg){
		Spanned result;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			result = Html.fromHtml("<u>" + msg + "</u>", Html.FROM_HTML_MODE_LEGACY);
		} else {
			result = Html.fromHtml("<u>" + msg + "</u>");
		}
		return result;
	}

	/**
	 * 判断是否是xml结构
	 */
	public static boolean isXML(String value) {
		try {
			DocumentHelper.parseText(value);
		} catch (DocumentException e) {
			return false;
		}
		return true;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 禁止EditText输入特殊字符
	 * @param editText
	 */
	public static void setEditTextInhibitInputSpeChat(EditText editText){

		InputFilter filter=new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】\"\'\\\\\\‘；：”“’。，、？ ]";
				Pattern pattern = Pattern.compile(speChat);
				Matcher matcher = pattern.matcher(source.toString());
				if(matcher.find())return "";
				else return null;
			}
		};
		editText.setFilters(new InputFilter[]{filter});
	}

	/**
	 * 递归删除文件和文件夹
	 * @param file    要删除的根目录
	 */
	public static void RecursionDeleteFile(File file){
		if(file.isFile()){
			file.delete();
			return;
		}
		if(file.isDirectory()){
			File[] childFile = file.listFiles();
			if(childFile == null || childFile.length == 0){
				file.delete();
				return;
			}
			for(File f : childFile){
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager
				= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//	        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  
		if (gps) {
			return true;
		}

		return false;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取通知栏权限是否开启
	 *
	 */

	public static class NotificationsUtils {
		private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
		private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

		@SuppressLint("NewApi")
		public static boolean isNotificationEnabled(Context context) {

			AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
			ApplicationInfo appInfo = context.getApplicationInfo();
			String pkg = context.getApplicationContext().getPackageName();
			int uid = appInfo.uid;

			Class appOpsClass = null;
      /* Context.APP_OPS_MANAGER */
			try {
				appOpsClass = Class.forName(AppOpsManager.class.getName());
				Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
						String.class);
				Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

				int value = (Integer) opPostNotificationValue.get(Integer.class);
				return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	public static void requestPermission(Context context) {
		// TODO Auto-generated method stub
		// 6.0以上系统才可以判断权限
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
			// 进入设置系统应用权限界面
			Intent intent = new Intent(Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return;
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
			// 进入设置系统应用权限界面
			Intent intent = new Intent(Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return;
		}
		return;
	}

	/**
	 * 显示图片
	 * @param context android的contxt
	 * @param values 圆角值
	 * @param url 图片url
	 * @param imageView 显示图片的控件
	 * @param loadingListener 图片加载监听
	 */
	public static DiskCache imageLoaderDisplayImage(Context context, int values, String url, final ImageView imageView, ImageLoadingListener loadingListener) {
		final DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisk(true)
				.cacheInMemory(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(values))
			//	.showImageOnLoading(R.mipmap.ic_loading_white_01)
				.build();
		if(loadingListener == null){
			ImageLoader.getInstance().displayImage(url, imageView, options, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					//ImageLoader.getInstance().displayImage("drawable://" + R.mipmap.ic_loading_white_01, imageView,options);
				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
				}
			});
		}else{
			ImageLoader.getInstance().displayImage(url, imageView, options, loadingListener);
		}

		return ImageLoader.getInstance().getDiskCache();
	}

	public void dialogShow(final Dialog dialog, Context context){
		if (!dialog.isShowing()) {
			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialog.show();
				}
			});
		}
	}

	public void dialogCancel(final Dialog dialog, Context context){
		if (dialog.isShowing()) {
			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialog.cancel();
				}
			});
		}
	}

}
