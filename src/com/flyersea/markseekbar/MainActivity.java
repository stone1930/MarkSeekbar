package com.flyersea.markseekbar;

import com.flyersea.markseekbar.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	private Button btnSeekTo;
	private MarkSeekBar seekbar;
	private EditText txtSeekProgress;
	private ToggleButton btnToogleSeek;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		seekbar = ((MarkSeekBar) findViewById(R.id.seekBar0));
		
		seekbar.setTextSize(21);// 设置字体大小
		seekbar.setTextColor(Color.WHITE);// 颜色
		seekbar.setMyPadding(10, 10, 10, 10);// 设置padding 调用setpadding会无效
		seekbar.setImageOffset(0, 0);// 可以不设置
		//seekbar.setTextPadding(-5, 0);// 可以不设置
		seekbar.setMarksOffsetLeftRight(5);
		seekbar.addMark(50);
		seekbar.addMark(30);
		seekbar.addMark(80);
	}


}
