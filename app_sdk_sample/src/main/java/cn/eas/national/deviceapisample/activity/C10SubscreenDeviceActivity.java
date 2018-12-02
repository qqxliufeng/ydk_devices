package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.IC10SubScreenDevicePresenter;
import cn.eas.national.deviceapisample.presenter.impl.C10SubScreenDevicePresenterImpl;

/**
 * @author caizl
 */
public class C10SubscreenDeviceActivity extends BaseDeviceActivity {
	private IC10SubScreenDevicePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c10subscreenapi);

		initView();
	}

	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.btnStartActivityOnSubScreen).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ start activity on subscreen ------------ ");
				presenter.startActivityOnSubScreen();
			}
		});
		findViewById(R.id.btnStartAppOnSubScreen).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ start app on subscreen ------------ ");
				presenter.startAppOnSubScreen();
			}
		});
		findViewById(R.id.btnSetSubscreenApp).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ set subscreen app ------------ ");
				presenter.setSubScreenApp();
			}
		});
		findViewById(R.id.btnRemoveSubscreenApp).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ remove subscreen app ------------ ");
				presenter.removeSubScreenApp();
			}
		});
		findViewById(R.id.btnGetSubscreenInfo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ get subscreen info ------------ ");
				presenter.getSubScreenInfo();
			}
		});
		findViewById(R.id.btnSendData).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ send data to subscreen ------------ ");
				presenter.sendDataToSubScreen();
			}
		});
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		bindDeviceService();
		presenter = new C10SubScreenDevicePresenterImpl(this);
		presenter.connect();
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		presenter.disconnect();
		unbindDeviceService();
	}

    @Override
    public String getModuleDescription() {
        String desc = "C10双屏设备专用接口，用于与客屏的交互操作，只能在C10双屏终端上使用。";
        return desc;
    }
}
