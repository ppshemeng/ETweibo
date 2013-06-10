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
package com.hao.etweibo;

import cn.sharesdk.framework.AbstractWeibo;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

/**
 * 
 * @ClassName: MainActivity
 * @Description: 主菜单
 * @Author Yoson Hao
 * @WebSite www.haoyuexing.cn
 * @Email haoyuexing@gmail.com
 * @Date 2013-5-31 下午4:07:52
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AbstractWeibo.initSDK(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void sendWeibo(View v) {
		startActivity(new Intent().setClass(MainActivity.this,
				SendWeiboActivity.class));
	}

	public void auth(View v) {
		startActivity(new Intent().setClass(MainActivity.this,
				AuthActivity.class));
	}

	@Override
	protected void onDestroy() {
		AbstractWeibo.stopSDK(this);
		super.onDestroy();
	}

}
