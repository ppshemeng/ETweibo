/**
 * Copyright (c) 2013, Yoson Hao 郝悦兴 (haoyuexing@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hao.etweibo.adapter;

import com.hao.etweibo.R;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.sharesdk.framework.AuthorizeAdapter;

public class ShareAuthorizeAdapter extends AuthorizeAdapter {
	public void onCreate() {
		getTitleLayout().getTvTitle().setText("授权");

		getTitleLayout().getChildAt(5).setVisibility(View.GONE);
		getTitleLayout().getChildAt(1).setBackgroundColor(
				Color.rgb(111, 143, 199));

		ImageView back = (ImageView) getTitleLayout().getChildAt(0);
		back.setBackgroundResource(R.drawable.title_background);
		back.setImageResource(R.drawable.ic_action_back);

		getTitleLayout().setBackgroundColor(Color.rgb(41, 29, 41));
		getBodyView().setBackgroundColor(Color.rgb(60, 60, 60));

		((LinearLayout) (getBodyView().getChildAt(0))).getChildAt(0)
				.setBackgroundColor(Color.rgb(111, 143, 199));
	}

	public void onDestroy() {
	}
}