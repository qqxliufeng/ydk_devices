package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;

import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;

/**
 * @author caizl
 */
public class SubscreenActivity extends BaseDeviceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.eas.national.deviceapisample.R.layout.subsreen);

		initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(cn.eas.national.deviceapisample.R.menu.activity_main, menu);
        return true;
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		displayInfo("这是主屏应用将其中一个activity投射到客屏的示例");
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public String getModuleDescription() {
		String desc = "投影到客屏的示例activity";
		return desc;
	}
}
