package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.ISignPanelPresenter;
import cn.eas.national.deviceapisample.presenter.impl.SignPanelPresenterImpl;

/**
 * @author caizl
 */
public class SignPanelActivity extends BaseDeviceActivity {
	private ISignPanelPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signpanel);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.btnStartSign).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ start sign  ------------ ");
				presenter.startSign();
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
		presenter = new SignPanelPresenterImpl(this);
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
        String desc = "支持外接签名板（如S160键盘）且主控版本及签名板驱动模块达到要求的终端。";
		return desc;
	}
}
