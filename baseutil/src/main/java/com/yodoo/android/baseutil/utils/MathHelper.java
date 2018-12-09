package com.yodoo.android.baseutil.utils;

/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Androidͼ������
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */

import android.graphics.PointF;

import java.math.BigDecimal;

/**
 * @ClassName MathHelper
 * @Description ������ ͼ����ص�һЩ���ڼ����С����
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 * 
 */

public class MathHelper {

	private static MathHelper instance = null;

	// Positionλ��
	private float mPosX = 0.0f;
	private float mPosY = 0.0f;
	private PointF mPointF = new PointF();

	// �����㾫��
	private static final int DEFAULT_DIV_SCALE = 10;

	//
	private boolean mHighPrecision = true;

	public MathHelper() {
	}

	public static synchronized MathHelper getInstance() {
		if (instance == null) {
			instance = new MathHelper();
		}
		return instance;
	}

	private void resetEndPointXY() {
		mPosX = mPosY = 0.0f;
		mPointF.x = mPosX;
		mPointF.y = mPosY;
	}

	// ��Բ����꣬�뾶�����νǶȣ������������������Բ��������xy���
	public PointF calcArcEndPointXY(float cirX, float cirY, float radius,
			float cirAngle) {
		resetEndPointXY();
		if (Float.compare(cirAngle, 0.0f) == 0
				|| Float.compare(radius, 0.0f) == 0) {
			return mPointF;
		}
		// ���Ƕ�ת��Ϊ����
		float arcAngle = (float) (Math.PI * div(cirAngle, 180.0f));
		if (Float.compare(arcAngle, 0.0f) == -1)
			mPosX = mPosY = 0.0f;

		if (Float.compare(cirAngle, 90.0f) == -1) {
			mPosX = add(cirX, (float) Math.cos(arcAngle) * radius);
			mPosY = add(cirY, (float) Math.sin(arcAngle) * radius);
		} else if (Float.compare(cirAngle, 90.0f) == 0) {
			mPosX = cirX;
			mPosY = add(cirY, radius);
		} else if (Float.compare(cirAngle, 90.0f) == 1
				&& Float.compare(cirAngle, 180.0f) == -1) {
			arcAngle = (float) (Math.PI * (sub(180f, cirAngle)) / 180.0f);
			mPosX = sub(cirX, (float) (Math.cos(arcAngle) * radius));
			mPosY = add(cirY, (float) (Math.sin(arcAngle) * radius));
		} else if (Float.compare(cirAngle, 180.0f) == 0) {
			mPosX = cirX - radius;
			mPosY = cirY;
		} else if (Float.compare(cirAngle, 180.0f) == 1
				&& Float.compare(cirAngle, 270.0f) == -1) {
			arcAngle = (float) (Math.PI * (sub(cirAngle, 180.0f)) / 180.0f);
			mPosX = sub(cirX, (float) (Math.cos(arcAngle) * radius));
			mPosY = sub(cirY, (float) (Math.sin(arcAngle) * radius));
		} else if (Float.compare(cirAngle, 270.0f) == 0) {
			mPosX = cirX;
			mPosY = sub(cirY, radius);
		} else {
			arcAngle = (float) (Math.PI * (sub(360.0f, cirAngle)) / 180.0f);
			mPosX = add(cirX, (float) (Math.cos(arcAngle) * radius));
			mPosY = sub(cirY, (float) (Math.sin(arcAngle) * radius));
		}

		mPointF.x = mPosX;
		mPointF.y = mPosY;
		return mPointF;
	}

	public PointF getArcEndPointF() {
		return mPointF;
	}

	public float getPosX() {
		return mPosX;
	}

	public float getPosY() {
		return mPosY;
	}

	// �����ĽǶ�
	public double getDegree(float sx, float sy, float tx, float ty) {
		float nX = tx - sx;
		float nY = ty - sy;
		double angrad = 0d, angel = 0d, tpi = 0d;
		float tan = 0.0f;

		if (Float.compare(nX, 0.0f) != 0) {
			tan = Math.abs(nY / nX);
			angel = Math.atan(tan);

			if (Float.compare(nX, 0.0f) == 1) {
				if (Float.compare(nY, 0.0f) == 1
						|| Float.compare(nY, 0.0f) == 0) {
					angrad = angel;
				} else {
					angrad = 2 * Math.PI - angel;
				}
			} else {
				if (Float.compare(nY, 0.0f) == 1
						|| Float.compare(nY, 0.0f) == 0) {
					angrad = Math.PI - angel;
				} else {
					angrad = Math.PI + angel;
				}
			}

		} else {
			tpi = Math.PI / 2;
			if (Float.compare(nY, 0.0f) == 1) {
				angrad = tpi;
			} else {
				angrad = -1 * tpi;
			}
		}
		return Math.toDegrees(angrad);
	}

	// �����ľ���
	public double getDistance(float sx, float sy, float tx, float ty) {
		float nx = Math.abs(tx - sx);
		float ny = Math.abs(ty - sy);

		return Math.hypot(nx, ny);
	}

	/**
	 * ���ٷֱ�ת��Ϊͼ�ĽǽǶ�
	 * 
	 * @param totalAngle
	 *            �ܽǶ�(��:360��)
	 * @param percentage
	 *            �ٷֱ�
	 * @return Բ�ĽǶ�
	 */
	public float getSliceAngle(float totalAngle, float percentage) {
		float Angle = 0.0f;
		try {

			float currentValue = percentage;
			if (currentValue >= 101f || currentValue < 0.0f) {
				// Log.e(TAG,"����İٷֱȲ��Ϲ淶.����0~100֮��.");
			} else {
				Angle = MathHelper.getInstance().round(
						MathHelper.getInstance().mul(
								totalAngle,
								MathHelper.getInstance()
										.div(currentValue, 100f)), 2);
			}

		} catch (Exception ex) {
			Angle = -1f;
		} finally {

		}
		return Angle;
	}

	// ��������LnChart��λ���㷨���ڵ������ǣ����Կ����Ż��¡�[���ᣬ������]
	public float getLnPlotXValPosition(float plotScreenWidth,
			float plotAreaLeft, double xValue, double maxValue, double minValue) {
		// ��Ӧ��X���
		double maxminRange = sub(maxValue, minValue);
		double xScale = div(sub(xValue, minValue), maxminRange);
		return mul(plotScreenWidth, (float) xScale);
	}

	public float getLnXValPosition(float plotScreenWidth, float plotAreaLeft,
			double xValue, double maxValue, double minValue) {
		// ��Ӧ��X���
		return add(
				plotAreaLeft,
				getLnXValPosition(plotScreenWidth, plotAreaLeft, xValue,
						maxValue, minValue));
	}

	public void disableHighPrecision() {
		mHighPrecision = false;
	}

	public void enabledHighPrecision() {
		mHighPrecision = true;
	}

	/**
	 * �ӷ�����
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public float add(float v1, float v2) {
		if (!mHighPrecision) {
			return (v1 + v2);
		} else {
			// BigDecimal bgNum1 = new BigDecimal(Float.toString(v1));
			BigDecimal bgNum1 = new BigDecimal(Float.toString(v1));
			BigDecimal bgNum2 = new BigDecimal(Float.toString(v2));
			return bgNum1.add(bgNum2).floatValue();
		}
	}

	/**
	 * ��������
	 * 
	 * @param v1
	 * @param v2
	 * @return ������
	 */
	public float sub(float v1, float v2) {
		if (!mHighPrecision) {
			return (v1 - v2);
		} else {
			BigDecimal bgNum1 = new BigDecimal(Float.toString(v1));
			BigDecimal bgNum2 = new BigDecimal(Float.toString(v2));
			return bgNum1.subtract(bgNum2).floatValue();
		}
	}

	/**
	 * �˷�����
	 * 
	 * @param v1
	 * @param v2
	 * @return ������
	 */
	public float mul(float v1, float v2) {
		if (!mHighPrecision) {
			return (v1 * v2);
		} else {
			BigDecimal bgNum1 = new BigDecimal(Float.toString(v1));
			BigDecimal bgNum2 = new BigDecimal(Float.toString(v2));
			return bgNum1.multiply(bgNum2).floatValue();
		}
	}

	/**
	 * ������,����ʱ����ȷ��С����10λ
	 * 
	 * @param v1
	 * @param v2
	 * @return ������
	 */
	public float div(float v1, float v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * ������,����ʱ����ȷ��С����scaleλ
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return ������
	 */
	public float div(float v1, float v2, int scale) {
		if (scale < 0)
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		if (Float.compare(v2, 0.0f) == 0)
			return 0.0f;

		if (!mHighPrecision) {
			return (v1 / v2);
		} else {
			BigDecimal bgNum1 = new BigDecimal(Float.toString(v1));
			BigDecimal bgNum2 = new BigDecimal(Float.toString(v2));
			return bgNum1.divide(bgNum2, scale, BigDecimal.ROUND_HALF_UP)
					.floatValue();
		}
	}

	/**
	 * �������뵽С����scaleλ
	 * 
	 * @param v
	 * @param scale
	 * @return
	 */
	public float round(float v, int scale) {
		if (scale < 0)
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		BigDecimal bgNum1 = new BigDecimal(Float.toString(v));
		BigDecimal bgNum2 = new BigDecimal("1");
		return bgNum1.divide(bgNum2, scale, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		// return b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	public double add(double v1, double v2) {
		if (!mHighPrecision) {
			return (v1 + v2);
		} else {
			BigDecimal bgNum1 = new BigDecimal(Double.toString(v1));
			BigDecimal bgNum2 = new BigDecimal(Double.toString(v2));
			return bgNum1.add(bgNum2).doubleValue();
		}
	}

	public double sub(double v1, double v2) {
		if (!mHighPrecision) {
			return (v1 - v2);
		} else {
			BigDecimal bgNum1 = new BigDecimal(Double.toString(v1));
			BigDecimal bgNum2 = new BigDecimal(Double.toString(v2));
			return bgNum1.subtract(bgNum2).doubleValue();
		}
	}

	/**
	 * �����,ʹ��Ĭ�Ͼ���
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public double div(double v1, double v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * �����
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 *            ָ����������
	 * @return
	 */
	public double div(double v1, double v2, int scale) {
		if (scale < 0)
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		if (Double.compare(v2, 0d) == 0)
			return 0d;

		if (!mHighPrecision) {
			return (v1 / v2);
		} else {
			BigDecimal bgNum1 = new BigDecimal(Double.toString(v1));
			BigDecimal bgNum2 = new BigDecimal(Double.toString(v2));
			return bgNum1.divide(bgNum2, scale, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
		}
	}

	/**
	 * �˷�����
	 * 
	 * @param v1
	 * @param v2
	 * @return ������
	 */
	public double mul(double v1, double v2) {
		if (!mHighPrecision) {
			return (v1 * v2);
		} else {
			BigDecimal bgNum1 = new BigDecimal(Double.toString(v1));
			BigDecimal bgNum2 = new BigDecimal(Double.toString(v2));
			return bgNum1.multiply(bgNum2).doubleValue();
		}
	}

	/**
	 * �������뵽С����scaleλ
	 * 
	 * @param v
	 * @param scale
	 * @return
	 */
	public double round(double v, int scale) {
		if (scale < 0)
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		BigDecimal bgNum1 = new BigDecimal(Double.toString(v));
		BigDecimal bgNum2 = new BigDecimal("1");
		return bgNum1.divide(bgNum2, scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		// return b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}

}
