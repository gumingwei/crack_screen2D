package com.gmm.crack_screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

@SuppressLint({ "UseSparseArrays", "HandlerLeak" })
public class CustomView extends View {
	private Paint mPaint;
	private SoundPool mSoundPool;
	private Map<Integer, Integer> mSoundMap = new HashMap<Integer, Integer>();
	private int mIndex;
	private Bitmap mBitmap;
	private ArrayList<Float> mXPointList;
	private ArrayList<Float> mYPointList;
	private int mCount = 0;// 点击次数
	private int mLength = 30;// 绘制总数

	public CustomView(Context context, AttributeSet attrs) {
		super(context);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLUE);
		// mPaint.setAlpha(127);
		mPaint.setStrokeWidth(2.0f);
		this.setKeepScreenOn(true);
		this.setFocusable(true);
		this.setLongClickable(true);
		this.mSoundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
		this.mSoundMap.put(1, mSoundPool.load(context, R.raw.cfokwowbfv, 1));
		this.mBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.screen);
		mXPointList = new ArrayList<Float>();
		mYPointList = new ArrayList<Float>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// mG++;
					handler.sendEmptyMessage(0);
				}

			}
		}).start();

	}

	@Override
	public boolean onTouchEvent(MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg1.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// drawBitmap(arg1.getX(), arg1.getY());
			playSound();
			mXPointList.add(arg1.getX());
			mYPointList.add(arg1.getY());
			postInvalidate();
			mCount++;
			if (mCount > mLength) {
				mXPointList.remove(0);
				mYPointList.remove(0);
				mLength++;
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		default:
			break;
		}
		return super.onTouchEvent(arg1);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < mXPointList.size(); ++i) {
			canvas.drawBitmap(mBitmap, mXPointList.get(i) - mBitmap.getWidth()
					/ 2, mYPointList.get(i) - mBitmap.getHeight() / 2, null);
		}
	}

	// 播放
	public void playSound() {
		mIndex = mSoundPool.play(mSoundMap.get(1), 1, 1, 0, 0, 1);
	}

	// 停止播放
	public void stopSound() {
		// Toast.makeText(getContext(), "zzzzz", 0).show();
		mSoundPool.stop(mIndex);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Toast.makeText(getContext(), "keydown", Toast.LENGTH_SHORT).show();
		return super.onKeyDown(keyCode, event);
	}

	// 更新界面
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if (mCount != 0 && mXPointList.size() != 0) {
					for (int i = 0; i < new Random()
							.nextInt(mXPointList.size() + 1); i++) {
						mXPointList.remove(0);
						mYPointList.remove(0);
						mLength++;
					}
				}
				postInvalidate();

			}
		}
	};
}
