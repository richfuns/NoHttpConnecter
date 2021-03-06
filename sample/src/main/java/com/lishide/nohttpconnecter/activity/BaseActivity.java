package com.lishide.nohttpconnecter.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lishide.nohttpconnecter.R;
import com.lishide.nohttpconnecter.view.dialog.ImageDialog;
import com.lishide.nohttputils.nohttp.CallServer;
import com.lishide.nohttputils.nohttp.HttpListener;
import com.yanzhenjie.nohttp.rest.Request;

public abstract class BaseActivity extends AppCompatActivity {
    protected BaseActivity context;
    protected Toolbar mToolbar;
    protected TextView mTvCenterTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        initContentView(savedInstanceState);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            initTitle();
        }

        initView();
        initLogic();
    }

    //-------------- NoHttp -----------//

    /**
     * 用来标记取消
     */
    private Object object = new Object();

    /**
     * 发起请求
     *
     * @param what      what.
     * @param request   请求对象。
     * @param callback  回调函数。
     * @param canCancel 是否能被用户取消。
     * @param isLoading 实现显示加载框。
     * @param <T>       想请求到的数据类型。
     */
    public <T> void startRequest(int what, Request<T> request, HttpListener<T> callback,
                            boolean canCancel, boolean isLoading) {
        // 这里设置一个sign给这个请求
        request.setCancelSign(object);
        CallServer.getInstance().add(context, what, request, callback, canCancel, isLoading);
    }

    @Override
    protected void onDestroy() {
        // 和声明周期绑定，退出时取消这个队列中的所有请求，当然可以在你想取消的时候取消也可以，不一定和声明周期绑定。
        CallServer.getInstance().cancelBySign(object);
        super.onDestroy();
    }

    // -------------------- BaseActivity的辅助封装 --------------------- //

    protected abstract void initContentView(Bundle bundle);

    protected abstract void initView();

    protected abstract void initLogic();

    protected void initTitle() {
        //  设置Toolbar title文字颜色
        mToolbar.setTitleTextColor(Color.WHITE);
        //设置NavigationIcon
        mToolbar.setNavigationIcon(R.mipmap.navigation_back_white);
        // 设置navigation button 点击事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvCenterTitle = (TextView) findViewById(R.id.tv_centerTitle);
    }

    public void setCenterTitle(String string) {
        mTvCenterTitle.setText(string);
    }

    public void setCenterTitle(int id) {
        mTvCenterTitle.setText(id);
    }

    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(int title, int message) {
        showMessageDialog(getText(title), getText(message));
    }

    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(int title, CharSequence message) {
        showMessageDialog(getText(title), message);
    }

    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(CharSequence title, int message) {
        showMessageDialog(title, getText(message));
    }

    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(CharSequence title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.txt_confirm, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /**
     * 显示图片dialog。
     *
     * @param title  标题。
     * @param bitmap 图片。
     */
    public void showImageDialog(CharSequence title, Bitmap bitmap) {
        ImageDialog imageDialog = new ImageDialog(this);
        imageDialog.setTitle(title);
        imageDialog.setImage(bitmap);
        imageDialog.show();
    }
}
