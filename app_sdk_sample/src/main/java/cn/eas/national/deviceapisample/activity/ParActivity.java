package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.IParPresenter;
import cn.eas.national.deviceapisample.presenter.impl.ParPresenterImpl;

/**
 * @author caizl
 */
public class ParActivity extends BaseDeviceActivity {
    public static final int KEY_TYPE_STRING = 1;
    public static final int KEY_TYPE_BOOLEAN = 2;

    private EditText edtModuleName, edtFileName, edtKey, edtValue;
    private RadioButton rbString, rbBoolean;
	private IParPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parmanager);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
        edtModuleName = (EditText) findViewById(cn.eas.national.deviceapisample.R.id.edtModuleName);
        edtFileName = (EditText) findViewById(R.id.edtFileName);
        edtKey = (EditText) findViewById(cn.eas.national.deviceapisample.R.id.edtKey);
        edtValue = (EditText) findViewById(cn.eas.national.deviceapisample.R.id.edtValue);
        rbString = (RadioButton) findViewById(cn.eas.national.deviceapisample.R.id.rbString);
        rbBoolean = (RadioButton) findViewById(cn.eas.national.deviceapisample.R.id.rbBoolean);

		findViewById(R.id.btnExist).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayInfo(" ------------ exist ------------ ");
                if (resetPresenter()) {
                    presenter.isExist();
                }
			}
		});
		findViewById(R.id.btnFirstRun).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayInfo(" ------------ is first run ------------ ");
                if (resetPresenter()) {
                    presenter.isFirstRun();
                }
			}
		});
		findViewById(R.id.btnReadParam).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayInfo(" ------------ read param ------------ ");
                if (resetPresenter()) {
                    String key = edtKey.getText().toString().trim();
                    if (TextUtils.isEmpty(key)) {
                        displayInfo("请输入参数名");
                        return;
                    }
                    presenter.read(getKeyType(), key);
                }
			}
		});
		findViewById(R.id.btnWriteParam).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayInfo(" ------------ write param ------------ ");
                if (resetPresenter()) {
                    String key = edtKey.getText().toString().trim();
                    String value = edtValue.getText().toString().trim();
                    if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                        displayInfo("请输入参数名和参数值");
                        return;
                    }
                    presenter.write(getKeyType(), key, value);
                }
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
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
		String desc = "par参数文件操作。";
		return desc;
	}

	private boolean check() {
        String moduleName = edtModuleName.getText().toString().trim();
        String fileName = edtFileName.getText().toString().trim();
        if (TextUtils.isEmpty(moduleName) || TextUtils.isEmpty(fileName)) {
            return false;
        }
        return true;
    }

    private boolean resetPresenter() {
        if (check()) {
            String moduleName = edtModuleName.getText().toString().trim();
            String fileName = edtFileName.getText().toString().trim();
            presenter = new ParPresenterImpl(ParActivity.this, moduleName, fileName);
            return true;
        }
        displayInfo("请输入模块名和文件名");
        return false;
    }

    private int getKeyType() {
        if (rbString.isChecked()) {
            return KEY_TYPE_STRING;
        }
        return KEY_TYPE_BOOLEAN;
    }
}
