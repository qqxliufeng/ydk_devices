package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.IAlgorithmPresenter;
import cn.eas.national.deviceapisample.presenter.impl.AlgorithmPresenterImpl;

/**
 * @author caizl
 */
public class AlgorithmActivity extends BaseDeviceActivity {
	private IAlgorithmPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.btnCalcData).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ calculate data  ------------ ");
				presenter.calcData();
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
		presenter = new AlgorithmPresenterImpl(this);
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
        String desc = "该模块列举一般常用的几种算法，如：AES/MAC/TDES/SM4/SHA1/SHA256/RSA等。";
		return null;
	}
}
