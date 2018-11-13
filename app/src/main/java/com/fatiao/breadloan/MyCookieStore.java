package com.fatiao.breadloan;

import android.annotation.SuppressLint;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SuppressLint("NewApi")
public class MyCookieStore implements CookieStore {
	private Map<URI, List<HttpCookie>> map = new HashMap<URI, List<HttpCookie>>();
	@Override
	public void add(URI uri, HttpCookie cookie) {
		System.out.println( uri + "检测到添加了一条cookie：" + cookie.getName() + "==" + cookie.getValue());
		List<HttpCookie> cookies = map.get(uri);
	    if (cookies == null) {
	      cookies = new ArrayList<HttpCookie>();
	      //第一次得到cookie的时候，就保存到当前cookiestore里面，供后面所有请求使用
	      cookies.add(cookie);
	      map.put(uri, cookies);
	    }
	}

	@Override
	public List<HttpCookie> get(URI uri) {
	    //只要map中有一个cookie，则其它网络请求都用这一个cookie
		List<HttpCookie> cookies = new ArrayList<HttpCookie>();
	    Iterator<URI> uriIt = map.keySet().iterator();
	    while(uriIt.hasNext()){
	    	URI onlyUri = uriIt.next();
	    	cookies = map.get(onlyUri);
	    	break;
	    }
	    System.out.println("----------------------------------------");
	   // System.out.println("我的uri:" + uri);
	   // System.out.println("我的cookie:" + cookies);
	    return cookies;
	}

	@Override
	public List<HttpCookie> getCookies() {
		Collection<List<HttpCookie>> values = map.values();
	    List<HttpCookie> result = new ArrayList<HttpCookie>();
	    for (List<HttpCookie> value : values) {
	      result.addAll(value);
	    }
	    return result;
	}

	@Override
	public List<URI> getURIs() {
		Set<URI> keys = map.keySet();
		return new ArrayList<URI>(keys);
	}

	@Override
	public boolean remove(URI uri, HttpCookie cookie) {
		 List<HttpCookie> cookies = map.get(uri);
	    if (cookies == null) {
	      return false;
	    }
	    return cookies.remove(cookie);
	}

	@Override
	public boolean removeAll() {
		map.clear();
	    return true;
	}

	

}
