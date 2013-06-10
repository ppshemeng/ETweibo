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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.douban.Douban;
import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.framework.WeiboActionListener;
import cn.sharesdk.netease.microblog.NetEaseMicroBlog;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sohu.microblog.SohuMicroBlog;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.hao.etweibo.entity.PlatformBean;

/**
 * 
 * @ClassName: SendWeiboActivity
 * @Description: 发送微博Activity
 * @Author Yoson Hao
 * @WebSite www.haoyuexing.cn
 * @Email haoyuexing@gmail.com
 * @Date 2013-6-1 下午2:24:36
 * 
 */
@SuppressLint("HandlerLeak")
public class SendWeiboActivity extends Activity {

	private static final String TAG = "SendWeiboActivity";

	// 平台数量
	private int platformCount = 6;
	// 文本框
	private EditText mContent;
	// 剩余字数,发送按钮
	private TextView mCount, mSend;
	// 平台选择按钮
	private ImageButton sinaweibo, tencentweibo, sohumicroblog,
			neteasemicroblog, douban, renren;
	// 平台实例
	private AbstractWeibo sinaweiboShare, tencentweiboShare,
			sohumicroblogShare, neteasemicroblogShare, doubanShare,
			renrenShare;

	// 选中图标
	private ImageButton tempImageButton = null;

	// 平台选择按钮的数组
	// private ImageButton[] platforms;

	// onCreate
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_send_weibo);
		super.onCreate(savedInstanceState);
		initDb();
		initView();
		initPlatforms();
		initLsnr();
		initLastPlatforms();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mContent = (EditText) this.findViewById(R.id.content);
		mCount = (TextView) this.findViewById(R.id.count);
		mSend = (TextView) this.findViewById(R.id.send);

		sinaweibo = (ImageButton) this.findViewById(R.id.sinaweibo);
		tencentweibo = (ImageButton) this.findViewById(R.id.tencentweibo);
		// sohumicroblog = (ImageButton) this.findViewById(R.id.sohumicroblog);
		// neteasemicroblog = (ImageButton) this
		// .findViewById(R.id.neteasemicroblog);
		renren = (ImageButton) this.findViewById(R.id.renren);
		// douban = (ImageButton) this.findViewById(R.id.douban);
	}

	/**
	 * 初始化监听
	 */
	private void initLsnr() {
		mContent.addTextChangedListener(new ContentTextChangedLsnr());
		sinaweiboShare.setWeiboActionListener(new PlatformLsnr());
		tencentweiboShare.setWeiboActionListener(new PlatformLsnr());
		sohumicroblogShare.setWeiboActionListener(new PlatformLsnr());
		neteasemicroblogShare.setWeiboActionListener(new PlatformLsnr());
		renrenShare.setWeiboActionListener(new PlatformLsnr());
		doubanShare.setWeiboActionListener(new PlatformLsnr());
	}

	/**
	 * 初始化数据库
	 */
	private void initDb() {
		FinalDb db = FinalDb.create(SendWeiboActivity.this, "etweibo.db", true);
		if (db.findAll(PlatformBean.class).size() == platformCount) {
			// Log
			Log.d(TAG, "equle platformCount");
			return;
		}

		PlatformBean sinaweibo = new PlatformBean();
		sinaweibo.setPlatformName("sinaweibo");
		sinaweibo.setDescription("false");
		db.save(sinaweibo);

		PlatformBean tencentweibo = new PlatformBean();
		tencentweibo.setPlatformName("tencentweibo");
		tencentweibo.setDescription("false");
		db.save(tencentweibo);

		PlatformBean sohumicroblog = new PlatformBean();
		sohumicroblog.setPlatformName("sohumicroblog");
		sohumicroblog.setDescription("false");
		db.save(sohumicroblog);

		PlatformBean neteasemicroblog = new PlatformBean();
		neteasemicroblog.setPlatformName("neteasemicroblog");
		neteasemicroblog.setDescription("false");
		db.save(neteasemicroblog);

		PlatformBean renren = new PlatformBean();
		renren.setPlatformName("renren");
		renren.setDescription("false");
		db.save(renren);

		PlatformBean douban = new PlatformBean();
		douban.setPlatformName("douban");
		douban.setDescription("false");
		db.save(douban);
	}

	/**
	 * 初始化微博实例
	 */
	private void initPlatforms() {
		sinaweiboShare = AbstractWeibo.getWeibo(this, SinaWeibo.NAME);
		tencentweiboShare = AbstractWeibo.getWeibo(this, TencentWeibo.NAME);
		sohumicroblogShare = AbstractWeibo.getWeibo(this, SohuMicroBlog.NAME);
		neteasemicroblogShare = AbstractWeibo.getWeibo(this,
				NetEaseMicroBlog.NAME);
		renrenShare = AbstractWeibo.getWeibo(this, Renren.NAME);
		doubanShare = AbstractWeibo.getWeibo(this, Douban.NAME);
	}

	/**
	 * 初始化上次选择的平台
	 */
	private void initLastPlatforms() {
		FinalDb db = FinalDb.create(SendWeiboActivity.this, "etweibo.db", true);
		List<PlatformBean> list = new ArrayList<PlatformBean>();
		list = db.findAllByWhere(PlatformBean.class, "description='true'");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getPlatformName().equals("sinaweibo")) {
					sinaweibo.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_rating_important));
					sinaweibo.setTag("true");
				}

				if (list.get(i).getPlatformName().equals("tencentweibo")) {
					tencentweibo.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_rating_important));
					tencentweibo.setTag("true");
				}

				if (list.get(i).getPlatformName().equals("sohumicroblog")) {
					sohumicroblog.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_rating_important));
					sohumicroblog.setTag("true");
				}

				if (list.get(i).getPlatformName().equals("neteasemicroblog")) {
					neteasemicroblog.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_rating_important));
					neteasemicroblog.setTag("true");
				}

				if (list.get(i).getPlatformName().equals("renren")) {
					renren.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_rating_important));
					renren.setTag("true");
				}

				if (list.get(i).getPlatformName().equals("douban")) {
					douban.setImageDrawable(getResources().getDrawable(
							R.drawable.ic_rating_important));
					douban.setTag("true");
				}
			}
		} else {
			// Log
			Log.i(TAG, "list is null");
			return;
		}
	}

	/**
	 * 键盘开关按钮点击事件
	 * 
	 * @param v
	 */
	public void keyboard(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 发送按钮点击事件
	 * 
	 * @param v
	 */
	public void send(View v) {
		// 如果 sinaweibo selected
		if ("true".equals(sinaweibo.getTag().toString())) {
			SinaWeibo.ShareParams sParams = new SinaWeibo.ShareParams();
			sParams.text = mContent.getText().toString();
			sinaweiboShare.share(sParams);
			System.out.println("开始");
		}
		// 如果 sinaweibo selected
		if ("true".equals(tencentweibo.getTag().toString())) {
			TencentWeibo.ShareParams sParams = new TencentWeibo.ShareParams();
			sParams.text = mContent.getText().toString();
			tencentweiboShare.share(sParams);
		}
		// 如果 renren selected
		if ("true".equals(renren.getTag().toString())) {
			Renren.ShareParams sParams = new Renren.ShareParams();
			sParams.text = " ";
			sParams.titleUrl = "http://www.haoyuexing.cn";
			sParams.title = "来自:EasyToShare";
			sParams.comment = mContent.getText().toString();
			renrenShare.share(sParams);
		}
	}

	/**
	 * 所有平台的选择按钮点击事件
	 * 
	 * @param v
	 */
	public void selectPlatform(View v) {
		tempImageButton = (ImageButton) v;

		if ("false".equals(v.getTag().toString())) {
			// 当没有被选中时点击
			// 不同平台的不同点击事件
			switch (v.getId()) {
			case R.id.sinaweibo:
				if (!sinaweiboShare.isValid()) {
					sinaweiboShare.authorize();
				} else {
					tempImageButton.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_rating_important));
					v.setTag("true");
					return;
				}
				break;

			case R.id.tencentweibo:
				if (!tencentweiboShare.isValid()) {
					tencentweiboShare.authorize();
				} else {
					tempImageButton.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_rating_important));
					v.setTag("true");
					return;
				}
				break;

			// case R.id.sohumicroblog:
			// break;

			// case R.id.neteasemicroblog:
			// break;

			case R.id.renren:
				if (!renrenShare.isValid()) {
					renrenShare.authorize();
				} else {
					tempImageButton.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_rating_important));
					v.setTag("true");
					return;
				}
				break;

			// case R.id.douban:
			// break;

			default:
				break;
			}
		} else {
			tempImageButton.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_rating_not_important));
			v.setTag("false");
		}
	}

	// 认证平台成功的toast
	Handler authHandlerOnComplete = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			tempImageButton.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_rating_important));
			tempImageButton.setTag("true");
			Toast.makeText(SendWeiboActivity.this, "授权成功", Toast.LENGTH_SHORT)
					.show();
			super.handleMessage(msg);
		}
	};

	/**
	 * 
	 * @ClassName: PlatformLsnr
	 * @Description: 认证和分享时的监听
	 * @Author Yoson Hao
	 * @WebSite www.haoyuexing.cn
	 * @Email haoyuexing@gmail.com
	 * @Date 2013-6-3 下午8:26:32
	 * 
	 */
	class PlatformLsnr implements WeiboActionListener {

		@Override
		public void onCancel(AbstractWeibo arg0, int arg1) {
			Log.i(TAG, "PlatformLsnr-onCancel");
		}

		@Override
		public void onComplete(AbstractWeibo arg0, int arg1,
				HashMap<String, Object> arg2) {
			if (arg1 == AbstractWeibo.ACTION_USER_INFOR) {
				Message message = authHandlerOnComplete.obtainMessage();
				message.sendToTarget();
				Log.i(TAG, arg0.getName() + "-PlatformLsnr-onComplete-auth");
			}

			if (arg1 == AbstractWeibo.ACTION_SHARE) {
				FinalDb db = FinalDb.create(SendWeiboActivity.this,
						"etweibo.db", true);
				if ("SinaWeibo".equals(arg0.getName())) {
					PlatformBean sinaweiboBean = new PlatformBean();
					sinaweiboBean.setDescription(sinaweibo.getTag().toString());
					db.update(sinaweiboBean, "platformName='sinaweibo'");
					System.out.println("dismiss");
				}

				if ("TencentWeibo".equals(arg0.getName())) {
					PlatformBean tencentweiboBean = new PlatformBean();
					tencentweiboBean.setDescription(tencentweibo.getTag()
							.toString());
					db.update(tencentweiboBean, "platformName='tencentweibo'");
				}

				if ("SohuMicroBlog".equals(arg0.getName())) {
					PlatformBean sohumicroblogBean = new PlatformBean();
					sohumicroblogBean.setDescription(sohumicroblog.getTag()
							.toString());
					db.update(sohumicroblogBean, "platformName='sohumicroblog'");
				}

				if ("NetEaseMicroBlog".equals(arg0.getName())) {
					PlatformBean neteasemicroblogBean = new PlatformBean();
					neteasemicroblogBean.setDescription(neteasemicroblog
							.getTag().toString());
					db.update(neteasemicroblogBean,
							"platformName='neteasemicroblog'");
				}

				if ("Renren".equals(arg0.getName())) {
					PlatformBean renrenBean = new PlatformBean();
					renrenBean.setDescription(renren.getTag().toString());
					db.update(renrenBean, "platformName='renren'");
				}

				if ("Douban".equals(arg0.getName())) {
					PlatformBean doubanBean = new PlatformBean();
					doubanBean.setDescription(douban.getTag().toString());
					db.update(doubanBean, "platformName='douban'");
				}

				Log.i(TAG, arg0.getName() + "-PlatformLsnr-onComplete-send");
			}

		}

		@Override
		public void onError(AbstractWeibo arg0, int arg1, Throwable arg2) {
			if (arg1 == 8) {
				Log.i(TAG, "PlatformLsnr-onError-auth");
				System.out.println(arg2.toString());
			}
			if (arg1 == 9) {
				Log.i(TAG, "PlatformLsnr-onError-send");
				System.out.println(arg2.toString());
			}

			// tempImageButton.setImageDrawable(getResources().getDrawable(
			// R.drawable.ic_rating_not_important));
			// tempImageButton.setTag("false");

		}
	}

	/**
	 * 
	 * @ClassName: ContentTextChangedLsnr
	 * @Description: 文本输入框字数剩余监听
	 * @Author Yoson Hao
	 * @WebSite www.haoyuexing.cn
	 * @Email haoyuexing@gmail.com
	 * @Date 2013-6-1 下午3:09:21
	 * 
	 */
	class ContentTextChangedLsnr implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			int tempCount = s.toString().length();
			mCount.setText("剩余文字:" + (140 - tempCount));
			mCount.setTextSize((float) (15 + 0.08 * tempCount));
			mSend.setTextSize((float) (15 + 0.08 * tempCount));
			if (tempCount == 140) {
				mCount.setTextColor(0xffadd8e6);
			}
		}
	}

	/**
	 * 
	 * @ClassName: DetailDialog
	 * @Description: 发送时弹出的dialog
	 * @Author Yoson Hao
	 * @WebSite www.haoyuexing.cn
	 * @Email haoyuexing@gmail.com
	 * @Date 2013-6-4 下午3:19:47
	 * 
	 */
	class DetailDialog extends Dialog {
		Context context;
		int index;

		public DetailDialog(Context context) {
			super(context);
			this.context = context;
		}

		public DetailDialog(Context context, int theme) {
			super(context, theme);
			this.context = context;
		}

		public DetailDialog(Context context, int theme, int index) {
			super(context, theme);
			this.context = context;
			this.index = index;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.dialog_send, null);
			LinearLayout linearLayout = (LinearLayout) view
					.findViewById(R.id.detail);
			linearLayout.addView(new Button(getContext()));
			setContentView(view);
		}
	}
}