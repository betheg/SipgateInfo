package net.skweez.sipgate;

import net.skweez.sipgate.model.CallHistory;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

/**
 * 
 * @author mks
 * @author $Author: mks $
 * @version $Rev: 9 $
 * @levd.rating RED Rev:
 */
public class SipgateTabActivity extends TabActivity {

	private final CallHistory history = new CallHistory();

	/** {@inheritDoc} */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initializeUi();
		refresh();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		int currentTab = getTabHost().getCurrentTab();
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.main);
		initializeUi();
		getTabHost().setCurrentTab(currentTab);
		refresh();
	}

	private void initializeUi() {
		initializeTabs();
	}

	private void initializeTabs() {
		TabHost tabHost = getTabHost();
		TabHost.TabSpec tabSpec;
		Intent intent;

		Resources resources = getResources();

		intent = new Intent().setClass(this, AccountInfoActivity.class);
		tabSpec = tabHost
				.newTabSpec("account")
				.setIndicator("Account",
						resources.getDrawable(R.drawable.ic_tab_account))
				.setContent(intent);
		tabHost.addTab(tabSpec);

		tabSpec = tabHost.newTabSpec(getString(R.string.call_tab_tag));
		tabSpec.setIndicator(getString(R.string.call_tab_name),
				resources.getDrawable(R.drawable.ic_tab_recent));
		tabSpec.setContent(new MyTabContentFactory());
		tabHost.addTab(tabSpec);
	}

	/** {@inheritDoc} */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.refresh:
			refresh();
			return true;
		case R.id.setup:
			showSetupActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refresh() {
		history.startRefresh();
	}

	private void showSetupActivity() {
		Intent intent;

		intent = new Intent().setClass(this, SetupActivity.class);
		startActivity(intent);
	}

	private class MyTabContentFactory implements TabContentFactory {

		public View createTabContent(String tag) {

			if (tag.equals(getString(R.string.call_tab_tag))) {
				return createCallList();
			}

			throw new IllegalArgumentException();
		}

		private View createCallList() {
			ListView view = new ListView(getApplicationContext());
			view.setAdapter(new CallListAdapter(SipgateTabActivity.this,
					history));

			return view;
		}

	}
}
