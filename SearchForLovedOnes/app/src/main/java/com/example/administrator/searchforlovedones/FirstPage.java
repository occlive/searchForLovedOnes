package com.example.administrator.searchforlovedones;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FirstPage extends Fragment {
    public MySpinner spinner;
    private View firstpage;
    private Button btn_reg;
    public ScrollView lvContent;
    public Button btn_back;
    private List<String> list;
    public static TabLayout tabLayout;
    private ViewPager viewPager;
    public static ImageView load;
    private TextView name;
    private Gson gson;
    private User user = new User();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //避免重复创建Fragment
        if (firstpage == null) {
            firstpage = inflater.inflate(R.layout.fragment_page, container, false);
            gson = new Gson();
            findId();
            //获取name
            if(MainActivity.userId!=-1){
                getUser();
            }else{
                name.setText("未登录");
            }
            //给TabLayout添加Tab
            addTab();
            //相关连Tablayout和ViewPager
            tabAndView();
            //点击事件设置
            click();
            //下拉按钮改变
            itemCahnge();
            load.setVisibility(View.INVISIBLE);
        }

        ViewGroup parent = (ViewGroup) firstpage.getParent();
        if (parent != null) {
            parent.removeView(firstpage);
        }

        return firstpage;
    }

    private void tabAndView() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), list);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addTab() {
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        list = new ArrayList<>();
        list.add("防拐防骗");
        list.add("寻人方法");
        list.add("政策法规");
        tabLayout.addTab(tabLayout.newTab().setText(list.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(list.get(2)));
    }

    private void click() {


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Load.class);
                startActivity(intent);
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.fragmentTabHost.setCurrentTab(4);
            }
        });
    }

    //获取ID
    public void findId() {
        spinner = firstpage.findViewById(R.id.first_span);
        btn_reg = firstpage.findViewById(R.id.btn_reg);
        tabLayout = firstpage.findViewById(R.id.tab_essence);
        viewPager = firstpage.findViewById(R.id.vp_essence);
        load = firstpage.findViewById(R.id.load);
        lvContent = firstpage.findViewById(R.id.lvContent);
        name = firstpage.findViewById(R.id.name);
    }

    //设置下拉按钮方法
    private void itemCahnge() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceType")
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && view != null) {
                    view.setVisibility(View.INVISIBLE);
                    Log.e("select", position + "");
                } else if (position == 1) {
                    Log.e("select", position + "");
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.fragmentTabHost.setCurrentTab(2);
                    Intent intent = new Intent(mainActivity.getApplicationContext(), SearchPeople.class);
                    intent.putExtra("userId",""+MainActivity.userId);
                    Log.e("现在的id",MainActivity.userId+"");
                    startActivity(intent);
                } else if (position == 2) {
                    Log.e("select", position + "");
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.fragmentTabHost.setCurrentTab(2);
                    Intent intent = new Intent(mainActivity.getApplicationContext(), SearchFamily.class);
                    intent.putExtra("userId",""+MainActivity.userId);
                    Log.e("现在的id",MainActivity.userId+"");
                    startActivity(intent);
                } else if (position == 3) {
                    Log.e("select", position + "");
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.fragmentTabHost.setCurrentTab(2);
                    Intent intent = new Intent(mainActivity.getApplicationContext(), VagrantHelp.class);
                    intent.putExtra("userId",""+MainActivity.userId);
                    Log.e("现在的id",MainActivity.userId+"");
                    startActivity(intent);
                } else if (position == 4) {
                    Log.e("select", position + "");
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.fragmentTabHost.setCurrentTab(2);
                    Intent intent = new Intent(mainActivity.getApplicationContext(), OtherSearch.class);
                    intent.putExtra("userId",""+MainActivity.userId);
                    Log.e("现在的id",MainActivity.userId+"");
                    startActivity(intent);
                }
                spinner.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void getUser() {
        NameTask nameTask = new NameTask();
        nameTask.execute("http://" + Constant.IP + ":8080/searchfor_prj/IdvInitialize");
    }

    private class NameTask extends AsyncTask {

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            name.setText(user.getUserName());

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String json = gson.toJson(MainActivity.userId);
            try {
                URL url = new URL((String) objects[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                OutputStream os = connection.getOutputStream();
                os.write(json.toString().getBytes());
                InputStreamReader is = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(is);
                StringBuffer str = new StringBuffer();
                String line = null;
                while (null != (line = bufferedReader.readLine())) {
                    str.append(line);
                }
                is.close();

                String jsonStr = new String(str.toString().getBytes("utf-8"), "UTF-8");
                user = gson.fromJson(jsonStr, User.class);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

}
