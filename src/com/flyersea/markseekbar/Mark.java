package com.flyersea.markseekbar;

import android.graphics.RectF;

public class Mark extends RectF{
	public Mark(int postion) {
		this.postion = postion;
	}
	public int postion;
	@Override
	public String toString() {
		return "Mark [postion=" + postion + ", left=" + left + ", top=" + top + ", right=" + right + ", bottom=" + bottom + "]";
	}
	
	
}
