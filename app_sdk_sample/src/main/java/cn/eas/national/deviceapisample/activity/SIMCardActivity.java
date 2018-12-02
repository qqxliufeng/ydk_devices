package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.data.Constants;
import cn.eas.national.deviceapisample.presenter.ISIMCardPresenter;
import cn.eas.national.deviceapisample.presenter.impl.SIM4428CardPresenterImpl;
import cn.eas.national.deviceapisample.presenter.impl.SIM4442CardPresenterImpl;

/**
 * @author caizl
 */
public class SIMCardActivity extends BaseDeviceActivity {
	private ISIMCardPresenter presenter;
	private int module;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sim4428_4442card);

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
				displayInfo(" ------------ card halt  ------------ ");
				presenter.cardHalt();
			}
		});
		findViewById(R.id.btnRead).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ read  ------------ ");
				presenter.read();
			}
		});
		findViewById(R.id.btnWrite).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ write  ------------ ");
				presenter.write();
			}
		});
		findViewById(R.id.btnChangKey).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ chang password  ------------ ");
				presenter.changePassword();
			}
		});

		this.module = getIntent().getIntExtra(SyncCardActivity.PARAMS_MODULE, Constants.Device.MODULE_CARD_SIM4428);
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
        if (module == Constants.Device.MODULE_CARD_SIM4428) {
            presenter = new SIM4428CardPresenterImpl(this);
        } else {
            presenter = new SIM4442CardPresenterImpl(this);
        }
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
        String desc = "";
		if (module == Constants.Device.MODULE_CARD_SIM4428) {
			desc = "该模块为SIM4428同步卡操作示例。";
		} else {
			desc = "该模块为SIM4442同步卡操作示例。";
		}
		return desc;
	}
}
