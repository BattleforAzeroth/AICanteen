// Tencent is pleased to support the open source community by making ncnn available.
//
// Copyright (C) 2021 THL A29 Limited, a Tencent company. All rights reserved.
//
// Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
// in compliance with the License. You may obtain a copy of the License at
//
// https://opensource.org/licenses/BSD-3-Clause
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.

package com.tencent.ncnnyolov7;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.tencent.ncnnyolov7.bean.ElemeGroupedItem;
import com.tencent.ncnnyolov7.databinding.MenuBinding;

import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private MenuBinding mBinding;
    private static final int SPAN_COUNT_FOR_GRID_MODE = 2;
    private static final int MARQUEE_REPEAT_LOOP_MODE = -1;
    private static final int MARQUEE_REPEAT_NONE_MODE = 0;
    private int[] f;
    public static TextView textView;
    public static TextView textCheckout;
    public static ImageView checkout;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = MenuBinding.inflate(LayoutInflater.from(this));
        View view = mBinding.getRoot();
        setContentView(view);

        mBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class); //切换窗口
                startActivity(intent);
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        int[] labels = bundle.getIntArray("labels");

        f = new int[]{1, 2, 3, 15, 20, 21, 4, 22, 16, 5, 6, 7, 8, 9, 10, 17, 11, 12, 23, 18, 13};
        initLinkageData(mBinding.linkage, labels);
    }

    @SuppressLint("SetTextI18n")
    private void initLinkageData(LinkageRecyclerView linkage, int[] labels) {
        Gson gson = new Gson();
        List<ElemeGroupedItem> items = gson.fromJson(getString(R.string.eleme_json),
                new TypeToken<List<ElemeGroupedItem>>() {
                }.getType());

        double sum = 0;

        if (labels[0] != 0) {
            for (int i = 1; i <= labels[0]; i++) {
                items.get(f[labels[i]]).Add();
                String cost = items.get(f[labels[i]]).info.getCost();
                sum += Double.parseDouble(cost.replace("$", ""));
            }
        }

        textView = (TextView) findViewById(R.id.textView);
        textCheckout = (TextView) findViewById(R.id.textCheckout);
        checkout = (ImageView) findViewById(R.id.checkout);
        @SuppressLint("DefaultLocale") String total = "Total:    $ " + String.format("%.2f", sum);
        textView.setText(total);
        if (sum == 0) {
            textCheckout.setVisibility(View.INVISIBLE);
            checkout.setVisibility(View.INVISIBLE);
        }
        linkage.init(items, new ElemePrimaryAdapterConfig(), new ElemeSecondaryAdapterConfig());
    }

    private static class ElemePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {

        private Context mContext;

        public void setContext(Context context) {
            mContext = context;
        }

        @Override
        public int getLayoutId() {
            return com.kunminx.linkage.R.layout.default_adapter_linkage_primary;
        }

        @Override
        public int getGroupTitleViewId() {
            return com.kunminx.linkage.R.id.tv_group;
        }

        @Override
        public int getRootViewId() {
            return com.kunminx.linkage.R.id.layout_group;
        }

        @Override
        public void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title) {
            TextView tvTitle = ((TextView) holder.getGroupTitle());
            tvTitle.setText(title);

            tvTitle.setBackgroundColor(mContext.getResources().getColor(
                    selected ? com.kunminx.linkage.R.color.colorPurple : com.kunminx.linkage.R.color.colorWhite));
            tvTitle.setTextColor(ContextCompat.getColor(mContext,
                    selected ? com.kunminx.linkage.R.color.colorWhite : com.kunminx.linkage.R.color.colorGray));
            tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
            tvTitle.setFocusable(selected);
            tvTitle.setFocusableInTouchMode(selected);
            tvTitle.setMarqueeRepeatLimit(selected ? MARQUEE_REPEAT_LOOP_MODE : MARQUEE_REPEAT_NONE_MODE);
        }

        @Override
        public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
            //TODO
        }
    }

    private static class ElemeSecondaryAdapterConfig implements
            ILinkageSecondaryAdapterConfig<ElemeGroupedItem.ItemInfo> {

        private Context mContext;

        public void setContext(Context context) {
            mContext = context;
        }

        @Override
        public int getGridLayoutId() {
            return 0;
        }

        @Override
        public int getLinearLayoutId() {
            return R.layout.adapter_eleme_secondary_linear;
        }

        @Override
        public int getHeaderLayoutId() {
            return com.kunminx.linkage.R.layout.default_adapter_linkage_secondary_header;
        }

        @Override
        public int getFooterLayoutId() {
            return R.layout.empty_adapter_linkage_secondary_footer;
        }

        @Override
        public int getHeaderTextViewId() {
            return R.id.secondary_header;
        }

        @Override
        public int getSpanCountOfGridMode() {
            return SPAN_COUNT_FOR_GRID_MODE;
        }

        @Override
        public void onBindViewHolder(LinkageSecondaryViewHolder holder,
                                     BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

            ((TextView) holder.getView(R.id.iv_goods_name)).setText(item.info.getTitle());

            Glide.with(mContext).load(item.info.getImgUrl()).into((ImageView) holder.getView(R.id.iv_goods_img));

            ((TextView) holder.getView(R.id.iv_goods_detail)).setText(item.info.getContent());

            ((TextView) holder.getView(R.id.iv_goods_price)).setText(item.info.getCost());

            int count = item.info.getCount();

            if (count == 0) {
                ((ImageView) holder.getView(R.id.iv_goods_reduce)).setVisibility(View.INVISIBLE);
                ((TextView) holder.getView(R.id.iv_goods_count)).setVisibility(View.INVISIBLE);
            } else {
                ((ImageView) holder.getView(R.id.iv_goods_reduce)).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.iv_goods_count)).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.iv_goods_count)).setText(String.valueOf(count));
            }

            holder.getView(R.id.iv_goods_item).setOnClickListener(v -> {
                //TODO
            });

            holder.getView(R.id.iv_goods_add).setOnClickListener(v -> {
                //TODO
                item.info.Add();
                ((ImageView) holder.getView(R.id.iv_goods_reduce)).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.iv_goods_count)).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.iv_goods_count)).setText(String.valueOf(item.info.getCount()));

                String cost = item.info.getCost();
                String total = MenuActivity.textView.getText().toString();
                double sum = Double.parseDouble(total.replace("Total:    $", "")) + Double.parseDouble(cost.replace("$", ""));
                textView.setText("Total:    $ " + String.format("%.2f", sum));
                textCheckout.setVisibility(View.VISIBLE);
                checkout.setVisibility(View.VISIBLE);
            });

            holder.getView(R.id.iv_goods_reduce).setOnClickListener(v -> {
                //TODO
                item.info.Reduce();

                if (item.info.getCount() == 0) {
                    ((ImageView) holder.getView(R.id.iv_goods_reduce)).setVisibility(View.INVISIBLE);
                    ((TextView) holder.getView(R.id.iv_goods_count)).setVisibility(View.INVISIBLE);
                } else {
                    ((TextView) holder.getView(R.id.iv_goods_count)).setText(String.valueOf(item.info.getCount()));
                }
                String cost = item.info.getCost();
                String total = MenuActivity.textView.getText().toString();
                double sum = Double.parseDouble(total.replace("Total:    $", "")) - Double.parseDouble(cost.replace("$", ""));
                textView.setText("Total:    $ " + String.format("%.2f", sum));
                if (sum == 0) {
                    textCheckout.setVisibility(View.INVISIBLE);
                    checkout.setVisibility(View.INVISIBLE);
                }
                else {
                    textCheckout.setVisibility(View.VISIBLE);
                    checkout.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder,
                                           BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

            ((TextView) holder.getView(R.id.secondary_header)).setText(item.header);
        }

        @Override
        public void onBindFooterViewHolder(LinkageSecondaryFooterViewHolder holder,
                                           BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

        }
    }

}
