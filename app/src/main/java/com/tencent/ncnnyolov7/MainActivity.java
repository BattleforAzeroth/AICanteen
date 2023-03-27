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

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tencent.ncnnyolov7.databinding.MainBinding;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    public static final int REQUEST_CAMERA = 100;
    private NcnnYolov7 ncnnyolov7 = new NcnnYolov7();
    private int facing = 0;
    private MainBinding mBinding;
    private int current_model = 0;
    private String modelList[] = {"v7tiny"};
    private final String[] CPUGPUList = {"CPU", "GPU"};
    private int current_cpugpu = 0;
    private SurfaceView cameraView;

    /**
     * Called when the activity is first created.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = MainBinding.inflate(LayoutInflater.from(this));
        View view = mBinding.getRoot();
        setContentView(view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mBinding.cameraview.getHolder().setFormat(PixelFormat.RGBA_8888);
        mBinding.cameraview.getHolder().addCallback(this);

        mBinding.buttonSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int new_facing = 1 - facing;
                ncnnyolov7.closeCamera();
                ncnnyolov7.openCamera(new_facing);
                facing = new_facing;
            }
        });
        mBinding.buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(MainActivity.this, MenuActivity.class); //切换窗口
                int[] labels = ncnnyolov7.getDetectionLabels();
                Bundle bundle = new Bundle();
                bundle.putSerializable("labels", labels);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, modelList);
        mBinding.autoComplete1.setAdapter(arrayAdapter1);
        mBinding.autoComplete1.setInputType(InputType.TYPE_NULL);
        mBinding.autoComplete1.setText("v7tiny", false);

        ((AutoCompleteTextView)mBinding.exposedDropdownMenuModel.getEditText()).setOnItemClickListener((adapterView, view1, position, id) -> {
            if (position != current_model) {
                current_model = position;
                reload();
            }
        });

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, CPUGPUList);
        mBinding.autoComplete2.setAdapter(arrayAdapter2);
        mBinding.autoComplete2.setInputType(InputType.TYPE_NULL);
        mBinding.autoComplete2.setText("CPU", false);

        ((AutoCompleteTextView)mBinding.exposedDropdownMenuCPUGPU.getEditText()).setOnItemClickListener((adapterView, view1, position, id) -> {
            if (position != current_cpugpu) {
                current_cpugpu = position;
                reload();
            }
        });
        reload();
    }

    private void reload() {
        boolean ret_init = ncnnyolov7.loadModel(getAssets(), current_model, current_cpugpu);
        if (!ret_init) {
            Log.e("MainActivity", "ncnnyolov7 loadModel failed");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        ncnnyolov7.setOutputWindow(holder.getSurface());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

        ncnnyolov7.openCamera(facing);
    }

    @Override
    public void onPause() {
        super.onPause();

        ncnnyolov7.closeCamera();
    }
}
