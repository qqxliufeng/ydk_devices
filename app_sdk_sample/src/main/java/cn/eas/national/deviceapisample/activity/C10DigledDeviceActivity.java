package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.IC10DigledDevicePresenter;
import cn.eas.national.deviceapisample.presenter.impl.C10DigledDevicePresenterImpl;

/**
 * @author caizl
 */
public class C10DigledDeviceActivity extends BaseDeviceActivity {
	private IC10DigledDevicePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c10digledapi);

		initView();
	}

	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.btnDigledInfo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ get digled info ------------ ");
				presenter.getDigledInfo();
			}
		});
		findViewById(R.id.btnFlashLight).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ flash light ------------ ");
				presenter.flashLight();
			}
		});
		findViewById(R.id.btnStopFlash).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ stop flash ------------ ");
				presenter.stopFlash();
			}
		});
		findViewById(R.id.btnDisplay).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ display ------------ ");
				presenter.display();
			}
		});
		findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ clear ------------ ");
				presenter.clear();
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
		presenter = new C10DigledDevicePresenterImpl(this);
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

    @Override
    public String getModuleDescription() {
        String desc = "C10单屏设备数码管专用接口，用于数码管交互操作，只能在C10单屏终端上使用。";
        return desc;
    }
}
