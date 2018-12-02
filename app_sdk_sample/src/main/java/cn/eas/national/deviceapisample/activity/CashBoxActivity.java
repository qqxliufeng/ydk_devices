package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.ICashBoxPresenter;
import cn.eas.national.deviceapisample.presenter.impl.CashBoxPresenterImpl;

/**
 * @author caizl
 */
public class CashBoxActivity extends BaseDeviceActivity {
	private ICashBoxPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashbox);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.btnOpen).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayInfo(" ------------ open ------------ ");
				presenter.open();
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
		presenter = new CashBoxPresenterImpl(this);
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
		String desc = "针对不同型号终端，接入钱箱有所区分。C10有专用钱箱端口，而A8等终端需通过底座或者转换线的方式进行接入。";
		return desc;
	}
}
