package com.softdesign.devintensive.ui.activities;

import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX+"Main Activity";
    protected ImageView mPhoneBtn;
    protected CoordinatorLayout mCoordinatorLayout;
    protected Toolbar mToolbar;
    protected DrawerLayout mNavigationDrawer;
    protected FloatingActionButton mFab;
    protected EditText mPhone, mEmail, mProfileVk, mGitHub, mAbout;
    protected ArrayList<EditText> mUserInfo;
    protected int mCurrentEditMode;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhoneBtn = (ImageView) findViewById(R.id.phone_btn);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mPhone = (EditText) findViewById(R.id.phone_et);
        mEmail = (EditText) findViewById(R.id.email_et);
        mProfileVk = (EditText) findViewById(R.id.account_vk_et);
        mGitHub = (EditText) findViewById(R.id.github_et);
        mAbout = (EditText) findViewById(R.id.about_et);
        mUserInfo = new ArrayList<>();
        mUserInfo.add(mPhone);
        mUserInfo.add(mEmail);
        mUserInfo.add(mProfileVk);
        mUserInfo.add(mGitHub);
        mUserInfo.add(mAbout);
        mDataManager = DataManager.getInstance();

        mFab.setOnClickListener(this);
        setupToolbar();
        setupDrawer();
        loadUserInfoValue();

        if (savedInstanceState == null) {
            // приложение запускается в 1 раз

        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_CODE_MODE, 0);
            changeEditMode(mCurrentEditMode);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveUserInfoValue();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if (mCurrentEditMode == 1) {
                    mCurrentEditMode = 0;
                    changeEditMode(0);
                } else {
                    mCurrentEditMode = 1;
                    changeEditMode(1);
                }
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_CODE_MODE, mCurrentEditMode);
    }

    private void showSnackBar(String messsage) {
        Snackbar.make(mCoordinatorLayout, messsage, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                showSnackBar(menuItem.getTitle().toString());
                menuItem.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    /**
     * переключает режим редактирования
     * @param mode если 1 режим редактирования, усли 0 режим просмотра
     */
    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_check_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
            }
            saveUserInfoValue();
        }
    }

    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfo.get(i).setText(userData.get(i));
        }

    }

    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText  userInfoField: mUserInfo) {
            userData.add(userInfoField.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }
}