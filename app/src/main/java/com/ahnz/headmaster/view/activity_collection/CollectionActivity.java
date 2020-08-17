package com.ahnz.headmaster.view.activity_collection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahnz.headmaster.MainActivity;
import com.ahnz.headmaster.R;
import com.ahnz.headmaster.bean.PictureCollection;
import com.ahnz.headmaster.utils.database.RealmOperationHelper;
import com.ahnz.headmaster.view.activity_find_classification_details.FindClassificationDetailsActivity;
import com.ahnz.headmaster.view.activity_header_details.HeaderDetailsActivity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gyf.immersionbar.ImmersionBar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author xzb
 * @description: 我的收藏
 * @date :2020/7/17 15:37
 */
public class CollectionActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collection_rv)
    RecyclerView collection_rv;

    @BindView(R.id.no_data_layout)
    LinearLayout no_data_layout;
    private MyCollectionRecyclerViewAdapter myCollectionRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.white).navigationBarColor(R.color.white).fitsSystemWindows(true).init();
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        toolbar.setTitle("");
        //设置导航图标要在setSupportActionBar方法之后
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_dark_24dp);

        List<PictureCollection> realmResults = (List<PictureCollection>) RealmOperationHelper.getInstance().queryAllByDescending(PictureCollection.class, "collectionTime");
        //Toast.makeText(this, realmResults.size() + "", Toast.LENGTH_SHORT).show();
        //realmResults 经鉴定  就算无数据  也不会是null
        if (0 < realmResults.size()) {
            no_data_layout.setVisibility(View.GONE);
            collection_rv.setVisibility(View.VISIBLE);
            //定义布局管理器为Grid管理器，设置一行放3个
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
            //设置布局管理器为网格布局管理器
            collection_rv.setLayoutManager(layoutManager);
            collection_rv.addItemDecoration(new FindClassificationDetailsActivity.Decoration());
            myCollectionRecyclerViewAdapter = new MyCollectionRecyclerViewAdapter(this, R.layout.item_collection_recyclerview, realmResults);
            //设置适配器
            collection_rv.setAdapter(myCollectionRecyclerViewAdapter);
            //设置Item点击事件
            myCollectionRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(CollectionActivity.this, HeaderDetailsActivity.class);
//                    switch (realmResults.get(position).getPathType()) {
//                        case PictureCollection.NET:
//                            intent.putExtra(HeaderDetailsActivity.PIC_PATH_NET, realmResults.get(position).getPicPath());
//                            startActivity(intent);
//                            break;
//
//                        case PictureCollection.LOCAL:
//                            intent.putExtra(HeaderDetailsActivity.PIC_PATH_LOCAL, "file://" + realmResults.get(position).getPicPath());
//                            startActivity(intent);
//                            break;
//
//                        case PictureCollection.URI:
//                            intent.putExtra(HeaderDetailsActivity.PIC_PATH_LOCAL, "file://" + realmResults.get(position).getPicPath());
//                            startActivity(intent);
//                            break;
//                    }
                    intent.putExtra(HeaderDetailsActivity.PIC_BITMAP_BYTES, realmResults.get(position).getBitmapBytes());
                    startActivity(intent);
                }
            });

            myCollectionRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    LinearLayout delete_layout = (LinearLayout) view.findViewById(R.id.delete_layout);
                    delete_layout.setVisibility(View.VISIBLE);
                    delete_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delete_layout.setVisibility(View.GONE);
                            //删除 某条数据
                            RealmOperationHelper.getInstance().deleteElement(PictureCollection.class, myCollectionRecyclerViewAdapter.getData().size() - 1 - position);
                            myCollectionRecyclerViewAdapter.getData().remove(position);
                            myCollectionRecyclerViewAdapter.notifyDataSetChanged();
                            if (0 == myCollectionRecyclerViewAdapter.getData().size()) {
                                no_data_layout.setVisibility(View.VISIBLE);
                                collection_rv.setVisibility(View.GONE);
                            }
                        }
                    });
                    return true;
                }
            });
        }
    }

    //点击 去首页
    public void clickGoMain(View view) {
        finish();
        MainActivity.noTouchViewPager.setCurrentItem(0);
    }

    //清空所有
    public void clickCleanAll(View view) {

        if (null != myCollectionRecyclerViewAdapter) {
            int size = myCollectionRecyclerViewAdapter.getData().size();
            if (size > 0) {
                new QMUIDialog.MessageDialogBuilder(this)
                        .setTitle("温馨提示")
                        .setMessage("确定删除全部收藏？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                //删除 所有数据
                                RealmOperationHelper.getInstance().deleteAll(PictureCollection.class);
                                myCollectionRecyclerViewAdapter.getData().clear();
                                no_data_layout.setVisibility(View.VISIBLE);
                                collection_rv.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != RealmOperationHelper.mRealm) {
            RealmOperationHelper.mRealm.close();
        }
    }

    class MyCollectionRecyclerViewAdapter extends BaseQuickAdapter<PictureCollection, BaseViewHolder> {
        private FragmentActivity fragmentActivity;

        public MyCollectionRecyclerViewAdapter(FragmentActivity fragmentActivity, int layoutResId, List<PictureCollection> data) {
            super(layoutResId, data);
            this.fragmentActivity = fragmentActivity;
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, PictureCollection dataBean) {
//            switch (dataBean.getPathType()) {
//                case PictureCollection.NET:
//                    Glide.with(fragmentActivity)
//                            .load(dataBean.getPicPath())
//                            .into((ImageView) baseViewHolder.getView(R.id.item_imageview));
//                    break;
//
//                case PictureCollection.LOCAL:
//                    Glide.with(fragmentActivity)
//                            .load(Uri.parse(dataBean.getPicPath()))
//                            .into((ImageView) baseViewHolder.getView(R.id.item_imageview));
//                    break;
//
//                case PictureCollection.URI:
//                    Glide.with(fragmentActivity)
//                            .load(Uri.parse("file://" + dataBean.getPicPath()))
//                            .into((ImageView) baseViewHolder.getView(R.id.item_imageview));
//                    break;
//            }
            byte[] bis = dataBean.getBitmapBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
            Glide.with(fragmentActivity)
                    .load(bitmap)
                    .into((ImageView) baseViewHolder.getView(R.id.item_imageview));
        }
    }
}
