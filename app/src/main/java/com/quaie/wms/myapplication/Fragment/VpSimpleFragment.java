package com.quaie.wms.myapplication.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quaie.wms.myapplication.R;

/**
 * Created by yue on 2017/1/17.
 * 　　　　　　　  ┏┓　 ┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　     ┃
 * 　　　　　　　┃　　　━　    ┃ ++ + + +
 * 　　　　　　 ████━████     ┃++  ++
 * 　　　　　　　┃　　　　　　 ┃ +
 * 　　　　　　　┃　　　┻　　　┃  +  +
 * 　　　　　　　┃　　　　　　 ┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * <p>
 * 用于显示viewpager内容区域的fragment
 */

public class VpSimpleFragment extends Fragment {

    private String mTitle;
    //Fragment里面的参数是有bundle传进来的，bundle是键值对，所以定义一个键值对
    private static final String BUNDLE_TITLE = "title";

    private TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(BUNDLE_TITLE);
        }

        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTextView = (TextView) view.findViewById(R.id.id_tv_fragment_page);
        mTextView.setText(mTitle);
    }

    //在向fragment传入参数的时候，一般情况下我们会用newInstance去创建一个fragment并传入值，而不是直接new出来
    //然后在onCreatView中取出来title
    public static VpSimpleFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);

        VpSimpleFragment fragment = new VpSimpleFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
}
