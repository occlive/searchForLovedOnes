package com.example.administrator.searchforlovedones;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnTwoLevelListener;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerOne extends Fragment {
    private View viewPageOne;
    private ListView listView;
    private List<PageText> texts = new ArrayList<>();
    private Gson gson;
    private SmartRefreshLayout smartRefreshLayout;
    private TwoLevelHeader header;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewPageOne = inflater.inflate(R.layout.viewpager_one, container, false);

        gson = new Gson();
        findId();
        getValues();
        smartListener();
        return viewPageOne;
    }

    private void smartListener() {
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {


                refreshLayout.finishLoadMore(2000);
            }
        });

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
            }
        });

        header.setOnTwoLevelListener(new OnTwoLevelListener() {
            @Override
            public boolean onTwoLevel(@NonNull RefreshLayout refreshLayout) {


                return false;
            }
        });


    }

    private void findId() {
        listView = viewPageOne.findViewById(R.id.list_one);
        smartRefreshLayout = viewPageOne.findViewById(R.id.smart_one);
        header = viewPageOne.findViewById(R.id.one_header);
    }

    //ListView设置
    private void listSource() {
        PageListAdapter adapter = new PageListAdapter(texts, viewPageOne.getContext(), R.layout.page_listitem);
        listView.setAdapter(adapter);
    }

    //添加数据
    private void getValues() {
        PageTextTask pageTextTask = new PageTextTask();
        pageTextTask.execute("http://10.7.88.184:8080/QinFeng/avoid");
    }

    private class PageTextTask extends AsyncTask {


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            listSource();
            listView.deferNotifyDataSetChanged();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                URL url = new URL((String) objects[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                InputStreamReader is = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(is);
                StringBuffer str = new StringBuffer();
                String line = null;
                while (null!=(line = bufferedReader.readLine()))  {
                    str.append(line);
                }
                is.close();
                String jsonStr =new String(str.toString().getBytes("utf-8"),"UTF-8");
                Log.e("获取到的JSON格式的用户列表", jsonStr);
                texts = gson.fromJson(String.valueOf(jsonStr), new TypeToken<List<PageText>>() {}.getType());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }


}