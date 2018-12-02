package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.data.Constants;
import cn.eas.national.deviceapisample.presenter.IRFCpuCardPresenter;
import cn.eas.national.deviceapisample.presenter.impl.RFCpuCardPresenterImpl;

/**
 * @author caizl
 */
public class RFCpuCardActivity extends BaseDeviceActivity {
	private IRFCpuCardPresenter presenter;
	private RadioGroup rgDeviceType;
	private RadioButton rbInner, rbExternal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.eas.national.deviceapisample.R.layout.rfcpucard);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		rbInner = (RadioButton) findViewById(R.id.rbInner);
		rbExternal = (RadioButton) findViewById(R.id.rbExternal);

		rgDeviceType = (RadioGroup) findViewById(R.id.rgDeviceType);
		rgDeviceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
				if (id == rbInner.getId()) {
					presenter = new RFCpuCardPresenterImpl(RFCpuCardActivity.this, Constants.RFCard.DEVICE_INNER);
				} else {
					presenter = new RFCpuCardPresenterImpl(RFCpuCardActivity.this, Constants.RFCard.DEVICE_EXTERNAL);
				}
			}
		});

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
		if (rbInner.isChecked()) {
			presenter = new RFCpuCardPresenterImpl(this, Constants.RFCard.DEVICE_INNER);
		} else {
			presenter = new RFCpuCardPresenterImpl(this, Constants.RFCard.DEVICE_EXTERNAL);
		}
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
		String desc = "针对终端上已具备非接模块可直接使用，否则需外接设备进行调用。内置非接读卡器，"
				+ "RFCardReader初始化调用getInstance()接口，寻卡回调需使用RFCardReader.OnSearchListener，"
				+ "外置非接读卡器，RFCardReader初始化调用getOtherInstance(\"EXTRFCARD\")接口，"
				+ "寻卡回调需使用RFCardReader.OnSearchListenerEx。";
		return desc;
	}
}
