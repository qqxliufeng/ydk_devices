package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.ISystemDevicePresenter;
import cn.eas.national.deviceapisample.presenter.impl.SystemDevicePresenterImpl;

/**
 * @author caizl
 */
public class SystemDeviceActivity extends BaseDeviceActivity {
	private ISystemDevicePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.btnUpdateDatetime).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ update datetime  ------------ ");
				presenter.updateTime();
			}
		});
		findViewById(R.id.btnReboot).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ reboot  ------------ ");
				presenter.reboot();
			}
		});
		findViewById(R.id.btnDeviceInfo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ get device info  ------------ ");
				presenter.getDeviceInfo();
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
		presenter = new SystemDevicePresenterImpl(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
        String desc = "获取设备相关信息模块，包括序列号等，以及对设备进行重启和更新时间操作。";
		return desc;
	}
}
