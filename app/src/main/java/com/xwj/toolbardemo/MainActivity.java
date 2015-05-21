package com.xwj.toolbardemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;

import com.xwj.toolbardemo.widget.PagerSlidingTabStrip;

import java.lang.reflect.Field;


public class MainActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ShareActionProvider mShareActionProvider;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * DrawerLayout不覆盖Toolbar
         */
        setContentView(R.layout.activity_main);
        /**
         * DrawerLayout覆盖Toolbar
         */
        //setContentView(R.layout.activity_main_toggleover);
        initViews();
        setOverflowShowingAlways();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.ic_launcher);
        // 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitle("ToolbarDemo");
        // toolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
        /* 这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了 */
        // getSupportActionBar().setTitle("标题");
        // getSupportActionBar().setSubtitle("副标题");
        // getSupportActionBar().setLogo(R.drawable.ic_launcher);

		/* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_settings:
//                        Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.action_share:
//                        Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
        //       mToolbar.setOnCreateContextMenuListener(this);
        getSupportActionBar().setHomeButtonEnabled(true);  // 设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* findView */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //当drawer页面打开的时候，京东的那个RunningMan动画就是在此时关闭和打开的
                Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //当drawer页面关闭的时候
                Toast.makeText(MainActivity.this, "关闭", Toast.LENGTH_SHORT).show();
            }

        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                colorChange(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        initTabsValue();
    }

    /**
     * mPagerSlidingTabStrip默认值配置
     */
    private void initTabsValue() {
        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.BLUE);
        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(Color.parseColor("#4876FF"));
        // tab底线高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1, getResources().getDisplayMetrics()));
        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics()));
        // 选中的文字颜色
        mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(Color.BLACK);
    }

    /**
     * 界面颜色的更改
     */
    @SuppressLint("NewApi")
    private void colorChange(int position) {
        // 用来提取颜色的Bitmap
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                BaseCardFragment.getBackgroundBitmapPosition(position));
        // Palette的部分
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            /**
             * 提取完之后的回调方法
             */
            @Override
            public void onGenerated(Palette palette) {
                if (palette != null) {
                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                /* 界面颜色UI统一性处理,看起来更Material一些 */
                    mPagerSlidingTabStrip.setBackgroundColor(vibrant.getRgb());
                    mPagerSlidingTabStrip.setTextColor(vibrant.getTitleTextColor());
                    // 其中状态栏、游标、底部导航栏的颜色需要加深一下，也可以不加，具体情况在代码之后说明
                    mPagerSlidingTabStrip.setIndicatorColor(colorBurn(vibrant.getRgb()));

                    mToolbar.setBackgroundColor(vibrant.getRgb());
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        Window window = getWindow();
                        // API>=21以上才能实现此效果，当然也可以用开源的状态栏SystemBarTintManager实现（支持4.4以上）
                        window.setStatusBarColor(colorBurn(vibrant.getRgb()));
                        window.setNavigationBarColor(colorBurn(vibrant.getRgb()));
                    }
                    //  释放掉，避免卡顿
                    bitmap.recycle();
                }

            }
        });

    }

    /**
     * 颜色加深处理
     *
     * @param RGBValues RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *                  Android中我们一般使用它的16进制，
     *                  例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *                  red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *                  所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    private int colorBurn(int RGBValues) {
        Log.e("-----",RGBValues+"");
        int alpha = RGBValues >> 24;

        int red = RGBValues >> 16 & 0xFF;
        Log.e("--red---",red+"");

        int green = RGBValues >> 8 & 0xFF;
        Log.e("--green---",green+"");

        int blue = RGBValues & 0xFF;
        Log.e("--blue---",blue+"");
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu
                .findItem(R.id.action_share));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        mShareActionProvider.setShareIntent(intent);

        MenuItem searchItem = menu.findItem(R.id.ab_search);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ab_search:
                Toast.makeText(MainActivity.this, "ab_search", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 处理溢出菜单问题
     */
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {"推荐", "分类", "本月热榜", "热门推荐", "专栏", "热门收藏", "随缘"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            return BaseCardFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}