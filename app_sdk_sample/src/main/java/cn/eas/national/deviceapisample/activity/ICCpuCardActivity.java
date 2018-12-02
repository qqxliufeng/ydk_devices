package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.IICCpuCardPresenter;
import cn.eas.national.deviceapisample.presenter.impl.ICCpuCardPresenterImpl;

/**
 * @author caizl
 */
public class ICCpuCardActivity extends BaseDeviceActivity {
	private IICCpuCardPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.eas.national.deviceapisample.R.layout.iccpucard);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.btnCardPower).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ card power  ------------ ");
				presenter.cardPower();
			}
		});
		findViewById(R.id.btnCardHalt).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayInfo(" ------------ card halt ------------ ");
				presenter.cardHalt();
			}
		});
		findViewById(cn.eas.national.deviceapisample.R.id.btnExchangeApdu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ exchange apdu ------------ ");
				presenter.exchangeApdu();
			}
		});
		findViewById(cn.eas.national.deviceapisample.R.id.btnExist).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ exist ------------ ");
				presenter.exist();
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
		presenter = new ICCpuCardPresenterImpl(this);
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
		return null;
	}
}
