package com.liujishun.library;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.test.library.manager.OnRetryListener;
import com.test.library.manager.OnShowHideViewListener;
import com.test.library.manager.StatusLayoutManager;


/**
 * Created by chenpengfei on 2017/3/14.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected StatusLayoutManager statusLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initToolBar();

        LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.main_rl);
        statusLayoutManager = StatusLayoutManager.newBuilder(this)
                .contentView(getContentView())
                .emptyDataView(R.layout.activity_emptydata)
                .errorView(R.layout.activity_error)
                .loadingView(R.layout.activity_loading)
                .netWorkErrorView(R.layout.activity_networkerror)
                .retryViewId(R.id.button_try)
                .onShowHideViewListener(new OnShowHideViewListener() {
                    @Override
                    public void onShowView(View view, int id) {
                    }

                    @Override
                    public void onHideView(View view, int id) {
                    }
                }).onRetryListener(new OnRetryListener() {
                    @Override
                    public void onRetry() {
                        statusLayoutManager.showLoading();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        statusLayoutManager.showContent();
                                    }
                                });
                            }
                        }).start();

                    }
                }).build();

        mainLinearLayout.addView(statusLayoutManager.getRootLayout(), 1);

        statusLayoutManager.showLoading();
    }


    protected abstract int getContentView();

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("StatusLayout");
        toolbar.inflateMenu(R.menu.base_toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_content) {
                    statusLayoutManager.showContent();
                }
                if(item.getItemId() == R.id.action_emptyData) {
                    statusLayoutManager.showEmptyData();
                }
                if(item.getItemId() == R.id.action_error) {
                    statusLayoutManager.showError();
                }
                if(item.getItemId() == R.id.action_networkError) {
                    statusLayoutManager.showNetWorkError();
                }
                if(item.getItemId() == R.id.action_loading) {
                    statusLayoutManager.showLoading();
                }
                return true;
            }
        });
    }
}
