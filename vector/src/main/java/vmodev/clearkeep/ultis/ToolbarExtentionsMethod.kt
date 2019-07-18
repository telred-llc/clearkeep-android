package vmodev.clearkeep.ultis

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

/**
 * Setup v7 toolbar to AppCompatActivity
 */
fun Toolbar.setupSupportToolbar(activity: AppCompatActivity, title: Int): Toolbar {
    activity.setSupportActionBar(this);
    activity.supportActionBar?.setTitle(title);
    activity.supportActionBar?.setDisplayHomeAsUpEnabled(true);
    activity.supportActionBar?.setDisplayShowHomeEnabled(true);
    this.setNavigationOnClickListener {
        activity.onBackPressed();
    }
    return this;
}