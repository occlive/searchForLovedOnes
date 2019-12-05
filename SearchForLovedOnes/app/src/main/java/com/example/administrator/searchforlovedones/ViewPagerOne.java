package com.example.administrator.searchforlovedones;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnTwoLevelListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerOne extends Fragment {
    public static View viewPageOne;
    public static ListView listView;
    public static List<PageText> texts = new ArrayList<>();
    public static Gson gson;
    private SmartRefreshLayout smartRefreshLayout;
    private TwoLevelHeader header;
    private List<PageText> addtexts = new ArrayList<>();
    private int position = 0;
    private MZBannerView banner;
    private List<Drawable> banList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewPageOne = inflater.inflate(R.layout.viewpager_one, container, false);

        gson = new Gson();

        //获取ID
        findId();
        //取值
        getValues();
        //设置二层轮播图
        twoLevelBanner();
        //设置监听事件
        smartListener();



        return viewPageOne;
    }

    private void twoLevelBanner() {

        banList.add(getResources().getDrawable(R.drawable.banner1));
        banList.add(getResources().getDrawable(R.drawable.banner2));
        banList.add(getResources().getDrawable(R.drawable.banner5));
        banList.add(getResources().getDrawable(R.drawable.banner6));
        banner.setDelayedTime(3500);
        banner.setDuration(2500);
        banner.setPages(banList, new MZHolderCreator() {
            @Override
            public MZViewHolder createViewHolder() {
                return new BannerHolder();
            }
        });
    }

    private void smartListener() {
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                addValues();
                refreshLayout.finishLoadMore(1000);
            }
        });

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getValues();
                refreshLayout.finishRefresh(1000);
            }
        });

        header.setOnTwoLevelListener(new OnTwoLevelListener() {
            @Override
            public boolean onTwoLevel(@NonNull RefreshLayout refreshLayout) {
                return true;
            }
        });
        banner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                Log.e("BannerPage",String.valueOf(i));
            }
        });
    }

    private void findId() {
        listView = viewPageOne.findViewById(R.id.list_one);
        smartRefreshLayout = viewPageOne.findViewById(R.id.smart_one);
        header = viewPageOne.findViewById(R.id.one_header);
        banner = viewPageOne.findViewById(R.id.banner);
    }

    //ListView设置
    private static void listSource() {
        PageListAdapter adapter = new PageListAdapter(texts, viewPageOne.getContext(), R.layout.page_listitem);
        listView.setAdapter(adapter);
    }

    //添加数据
    public static void getValues() {
        PageTextTask pageTextTask = new PageTextTask();
        pageTextTask.execute("http://10.7.88.184:8080/QinFeng/avoid");
    }

    private void addValues() {
        AddPageTextTask addPageTextTask = new AddPageTextTask();
        addPageTextTask.execute("http://10.7.88.184:8080/QinFeng/avoid");
    }

    private void refreshValues() {
        PageTextTask pageTextTask = new PageTextTask();
        pageTextTask.execute("http://10.7.88.184:8080/QinFeng/avoid");
    }

    private static class PageTextTask extends AsyncTask {


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
                while (null != (line = bufferedReader.readLine())) {
                    str.append(line);
                }
                is.close();
                String jsonStr = new String(str.toString().getBytes("utf-8"), "UTF-8");
                Log.e("获取到的JSON格式的用户列表", jsonStr);
                texts = gson.fromJson(String.valueOf(jsonStr), new TypeToken<List<PageText>>() {
                }.getType());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    private class AddPageTextTask extends AsyncTask {


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            for (PageText pageText : addtexts) {
                texts.add(pageText);
            }
            position = listView.getFirstVisiblePosition();
            listSource();
            listView.setSelection(position);
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
                while (null != (line = bufferedReader.readLine())) {
                    str.append(line);
                }
                is.close();
                String jsonStr = new String(str.toString().getBytes("utf-8"), "UTF-8");
                Log.e("获取到的JSON格式的用户列表", jsonStr);
                addtexts = gson.fromJson(String.valueOf(jsonStr), new TypeToken<List<PageText>>() {
                }.getType());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    private class BannerHolder implements MZViewHolder {
        private ImageView img_banenr;
        private TextView name;
        private TextView place;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.banner_item, null);
            img_banenr = view.findViewById(R.id.banner_iv);
            name = view.findViewById(R.id.banner_name);
            place = view.findViewById(R.id.banner_place);
            return view;
        }

        @Override
        public void onBind(Context context, int i, Object o) {
            RequestOptions options = new RequestOptions().centerCrop();
            Glide.with(context)
                    .load(banList.get(i))
                    .apply(options)
                    .into(img_banenr);
            name.setText("name");
            place.setText("place");
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        banner.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.pause();
    }
}
