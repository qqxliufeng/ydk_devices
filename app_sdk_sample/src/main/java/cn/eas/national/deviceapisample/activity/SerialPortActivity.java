package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;

import com.landicorp.android.eptapi.device.SerialPort;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.ISerialPortPresenter;
import cn.eas.national.deviceapisample.presenter.impl.SerialPortPresenterImpl;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * @author caizl
 */
public class SerialPortActivity extends BaseDeviceActivity {

	private RadioButton rbBaudRate9600, rbBaudRate19200, rbBaudRate38400, rbBaudRate57600, rbBaudRate115200;
	private RadioButton rbDataBits7, rbDataBits8;
	private RadioButton rbParityNo, rbParityEven, rbParityOdd;

	private ISerialPortPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serialport);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		rbBaudRate9600 = (RadioButton) findViewById(R.id.rbBaudRate9600);
		rbBaudRate19200 = (RadioButton) findViewById(R.id.rbBaudRate19200);
		rbBaudRate38400 = (RadioButton) findViewById(R.id.rbBaudRate38400);
		rbBaudRate57600 = (RadioButton) findViewById(R.id.rbBaudRate57600);
		rbBaudRate115200 = (RadioButton) findViewById(R.id.rbBaudRate115200);
		rbDataBits7 = (RadioButton) findViewById(R.id.rbDataBit7);
		rbDataBits8 = (RadioButton) findViewById(R.id.rbDataBit8);
		rbParityNo = (RadioButton) findViewById(R.id.rbParityNo);
		rbParityEven = (RadioButton) findViewById(R.id.rbParityEven);
		rbParityOdd = (RadioButton) findViewById(R.id.rbParityOdd);
		findViewById(R.id.btnInit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ init  ------------ ");
				int baudRate = SerialPort.BPS_9600;
				if (rbBaudRate19200.isChecked()) {
					baudRate = SerialPort.BPS_19200;
				} else if (rbBaudRate38400.isChecked()) {
					baudRate = SerialPort.BPS_38400;
				} else if (rbBaudRate57600.isChecked()) {
					baudRate = SerialPort.BPS_57600;
				} else if (rbBaudRate115200.isChecked()) {
					baudRate = SerialPort.BPS_115200;
				}
				int parity = SerialPort.PAR_NOPAR;
				if (rbParityEven.isChecked()) {
					parity = SerialPort.PAR_EVEN;
				} else if (rbParityOdd.isChecked()) {
					parity = SerialPort.PAR_ODD;
				}
				int dataBits = SerialPort.DBS_7;
				if (rbDataBits8.isChecked()) {
					dataBits = SerialPort.DBS_8;
				}
				presenter.init(baudRate, parity, dataBits);
			}
		});
		findViewById(R.id.btnOpen).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ open ------------ ");
				presenter.open();
			}
		});
		findViewById(R.id.btnRead).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ read ------------ ");
				presenter.read(new byte[1024]);
			}
		});
		findViewById(R.id.btnWrite).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ write ------------ ");
				byte[] data = ByteUtil.hexString2Bytes("1234567890");
				presenter.write(data);
			}
		});
		findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ close ------------ ");
				presenter.close();
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
		presenter = new SerialPortPresenterImpl(this);
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
