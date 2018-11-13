package com.fatiao.breadloan;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private Context context;
    private Button selectBut;

    private RadioButton  radioButton2, radioButton4,radioButton5;
    private RadioButton[] buttons;

    private static final String TAB_KEY = "TAG_KEY";
    private static final String PAYCOST = "PAYCOST";
    private static final String QUERY = "QUERY";

    private static final String ABOUT = "ABOUT";
    private static  final String GZWM="GZWM";
    //private ItemFragment itemFragment;
    private ItemFragment2 itemFragment2;
    private ItemFragment4 itemFragment4;
    private ItemFragment5 itemFragment5;
    private RadioGroup mainreadiogroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=MainActivity.this;
        setContentView(R.layout.activity_main);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        intview(savedInstanceState);

    }

    private void intview(Bundle savedInstanceState) {

        radioButton2 =  findViewById(R.id.center_radio);
        radioButton4 =  findViewById(R.id.right2_radio);
        radioButton5=findViewById(R.id.right6_radio);
        mainreadiogroup=findViewById(R.id.main_readiogroup);

        radioButton2.setOnClickListener(this);
        radioButton4.setOnClickListener(this);
        radioButton5.setOnClickListener(this);
        buttons = new RadioButton[]{ radioButton2, radioButton4,radioButton5};

        int index = 0;

        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(TAB_KEY, 0);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                index = bundle.getInt(TAB_KEY, 0);
            }
        }
        if (index < 0 || index > buttons.length - 1) {
            index = 0;
        }
        button(buttons[index]);
    }

    @Override
    public void onClick(View view) {
        button((Button) view);
    }

    private void button(Button view) {

        if (selectBut != null && selectBut == view) {
            return;
        }
        selectBut = view;
        Bundle bundle = new Bundle();
     if (radioButton2 == view) {
            if (itemFragment2 == null) {
                itemFragment2 = addFragment(ItemFragment2.class, QUERY, bundle);
            }
            changeFragment(itemFragment2);
        } else if (radioButton4 == view) {
            if (itemFragment4 == null) {
                itemFragment4 = addFragment(ItemFragment4.class, ABOUT, bundle);
            }
            changeFragment(itemFragment4);
        }
        else if (radioButton5==view)
        {
            if (itemFragment5 == null) {
                itemFragment5 = addFragment(ItemFragment5.class, GZWM, bundle);
            }
            changeFragment(itemFragment5);
        }

    }
    @SuppressWarnings("unchecked")
    /**
     * 添加管理fragment 并返回
     * @param fragmentClass    传入的Fragment类
     * @param tag     fragment标识
     * @param bundle
     * @return
     */
    private <T> T addFragment(Class<? extends Fragment> fragmentClass, String tag, Bundle bundle) {
        //创建Fragment 管理者
        FragmentManager manager = getSupportFragmentManager();
        //创建Fragment的管理执行者
        /*
         * FragmentTransaction方法对应Fragment生命周期的方法：
         * 1. add() : onAttach -> onCreate -> onCreateView -> onActivityCreated -> onStart -> onResume
         * 2. remove() : onPause -> onStop -> onDestoyView -> onDestory -> onDetach
         * 3. attach() :  onCreateView -> onActivityCreated -> onStart -> onResume
         * 4. detach() : onPause -> onStop -> onDestoryView
         * 5. show() : 没有
         * 6. hide() : 没有
         */
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = manager.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = Fragment.instantiate(context, fragmentClass.getName(), bundle);
            if (bundle != null) {
                bundle.putString("fragment", fragmentClass.getName());
                fragment.setArguments(bundle);
            }
            transaction.add(R.id.fragment, fragment, tag);
            transaction.commit();
        }
        return (T) fragment;
    }

    /**
     * 切换fragment
     *
     * @param Fragment 传入当前切换的fragment
     */
    private void changeFragment(Fragment Fragment) {
        //创建Fragment管理者
        FragmentManager manager = getSupportFragmentManager();
        //创建Fragment的管理执行者
        /*
         * FragmentTransaction方法对应Fragment生命周期的方法：
         * 1. add() : onAttach -> onCreate -> onCreateView -> onActivityCreated -> onStart -> onResume
         * 2. remove() : onPause -> onStop -> onDestoyView -> onDestory -> onDetach
         * 3. attach() :  onCreateView -> onActivityCreated -> onStart -> onResume
         * 4. detach() : onPause -> onStop -> onDestoryView
         * 5. show() : 没有
         * 6. hide() : 没有
         */
        FragmentTransaction transaction = manager.beginTransaction();



        Fragment paycostfragment = manager.findFragmentByTag(QUERY);
        if (paycostfragment != null && paycostfragment != Fragment) {

            transaction.detach(paycostfragment);
            paycostfragment.setUserVisibleHint(false);
        }
        Fragment aboutfragment = manager.findFragmentByTag(ABOUT);
        if (aboutfragment != null && aboutfragment != Fragment) {

            transaction.detach(aboutfragment);
            aboutfragment.setUserVisibleHint(false);
        }
        Fragment gzwmfragment = manager.findFragmentByTag(GZWM);
        if (gzwmfragment != null && gzwmfragment != Fragment) {

            transaction.detach(gzwmfragment);
            gzwmfragment.setUserVisibleHint(false);
        }
        if (Fragment != null) {
            if (Fragment != paycostfragment && Fragment.isDetached()) {
                transaction.attach(Fragment);
            } else if (Fragment == paycostfragment && Fragment.isDetached()) {
                transaction.attach(Fragment);
            }
            Fragment.setUserVisibleHint(true);
        }
        transaction.commit();
    }


    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 3000) {
                Toast.makeText(context, "再按一次退出APP", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
//                ToolsUtils.RecursionDeleteFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.wecan.waterbusiness/devce/"));
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
