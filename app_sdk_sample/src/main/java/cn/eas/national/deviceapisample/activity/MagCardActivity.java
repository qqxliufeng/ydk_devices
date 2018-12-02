package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseMagCardDeviceActivity;
import cn.eas.national.deviceapisample.presenter.IMagCardPresenter;
import cn.eas.national.deviceapisample.presenter.impl.MagCardPresenterImpl;

/**
 * @author caizl
 */
public class MagCardActivity extends BaseMagCardDeviceActivity {
	private IMagCardPresenter presenter;
    private Button btnSearch, btnStopSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magcard);

        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch.setEnabled(false);
                btnStopSearch.setEnabled(true);
                displayInfo(" ------------ start search  ------------ ");
                presenter.searchCard();
            }
        });
        btnStopSearch = (Button) findViewById(R.id.btnStopSearch);
        btnStopSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnSearch.setEnabled(true);
                btnStopSearch.setEnabled(false);
                displayInfo(" ------------ stop search ------------ ");
                presenter.stopSearch();
            }
        });
        btnStopSearch.setEnabled(false);
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
        presenter = new MagCardPresenterImpl(this);
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

    @Override
    public void finishSwipeCard() {
        btnSearch.setEnabled(true);
        btnStopSearch.setEnabled(false);
    }
}
