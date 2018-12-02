package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.impl.ScannerPresenterImpl;

/**
 * @author caizl
 */
public class ScannerActivity extends BaseDeviceActivity {
	private ScannerPresenterImpl presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.eas.national.deviceapisample.R.layout.scanner);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		findViewById(cn.eas.national.deviceapisample.R.id.btnStartBrScanner).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ start br scan  ------------ ");
				presenter.startBrScan();
			}
		});

		findViewById(cn.eas.national.deviceapisample.R.id.btnStopBrScanner).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ stop br scan  ------------ ");
				presenter.stopBrScan();
			}
		});

		findViewById(cn.eas.national.deviceapisample.R.id.btnStartScanner).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ start scan  ------------ ");
				presenter.startScan();
			}
		});
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(cn.eas.national.deviceapisample.R.menu.activity_main, menu);
        return true;
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		bindDeviceService();
		presenter = new ScannerPresenterImpl(this);
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
		String desc = "该模块针对外接扫码枪设备。";
		return desc;
	}
}
