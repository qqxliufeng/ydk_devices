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
import cn.eas.national.deviceapisample.presenter.IAT24CxxCardPresenter;
import cn.eas.national.deviceapisample.presenter.impl.AT24CxxCardPresenterImpl;
import cn.eas.national.deviceapisample.view.FlowRadioGroup;

/**
 * There are all mag card operations samples in this Activity.
 * @author caizl
 *
 */
public class AT24CxxCardActivity extends BaseDeviceActivity {
    private RadioButton rbAt24C01, rbAt24C02, rbAt24C04, rbAt24C08, rbAt24C16, rbAt24C32, rbAt24C64;
	private IAT24CxxCardPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at24cxxcard);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
        this.rbAt24C01 = (RadioButton) findViewById(R.id.rbAT24C01);
        this.rbAt24C02 = (RadioButton) findViewById(R.id.rbAT24C02);
        this.rbAt24C04 = (RadioButton) findViewById(R.id.rbAT24C04);
        this.rbAt24C08 = (RadioButton) findViewById(R.id.rbAT24C08);
        this.rbAt24C16 = (RadioButton) findViewById(R.id.rbAT24C16);
        this.rbAt24C32 = (RadioButton) findViewById(R.id.rbAT24C32);
        this.rbAt24C64 = (RadioButton) findViewById(R.id.rbAT24C64);
        ((FlowRadioGroup) findViewById(R.id.rgCardType)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                switch (id) {
                    case R.id.rbAT24C01:
                        displayInfo("change to AT24C01 card, please power up again");
                        presenter = new AT24CxxCardPresenterImpl(AT24CxxCardActivity.this, Constants.SyncCard.CARD_TYPE_AT24C01);
                        break;
                    case R.id.rbAT24C02:
                        displayInfo("change to AT24C02 card, please power up again");
                        presenter = new AT24CxxCardPresenterImpl(AT24CxxCardActivity.this, Constants.SyncCard.CARD_TYPE_AT24C01);
                        break;
                    case R.id.rbAT24C04:
                        displayInfo("change to AT24C04 card, please power up again");
                        presenter = new AT24CxxCardPresenterImpl(AT24CxxCardActivity.this, Constants.SyncCard.CARD_TYPE_AT24C01);
                        break;
                    case R.id.rbAT24C08:
                        displayInfo("change to AT24C08 card, please power up again");
                        presenter = new AT24CxxCardPresenterImpl(AT24CxxCardActivity.this, Constants.SyncCard.CARD_TYPE_AT24C01);
                        break;
                    case R.id.rbAT24C16:
                        displayInfo("change to AT24C16 card, please power up again");
                        presenter = new AT24CxxCardPresenterImpl(AT24CxxCardActivity.this, Constants.SyncCard.CARD_TYPE_AT24C01);
                        break;
                    case R.id.rbAT24C32:
                        displayInfo("change to AT24C32 card, please power up again");
                        presenter = new AT24CxxCardPresenterImpl(AT24CxxCardActivity.this, Constants.SyncCard.CARD_TYPE_AT24C01);
                        break;
                    case R.id.rbAT24C64:
                        displayInfo("change to AT24C64 card, please power up again");
                        presenter = new AT24CxxCardPresenterImpl(AT24CxxCardActivity.this, Constants.SyncCard.CARD_TYPE_AT24C01);
                        break;
                    default:
                        displayInfo("change to AT24C01 card, please power up again");
                        presenter = new AT24CxxCardPresenterImpl(AT24CxxCardActivity.this, Constants.SyncCard.CARD_TYPE_AT24C01);
                        break;
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
		presenter = new AT24CxxCardPresenterImpl(this, Constants.SyncCard.CARD_TYPE_AT24C01);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
        String desc = "该模块为AT24Cxx类型的同步卡操作示例。";
		return desc;
	}
}
