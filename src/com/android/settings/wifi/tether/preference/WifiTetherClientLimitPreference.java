/*
 * Copyright (C) 2022 Project Kaleidoscope
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

package com.android.settings.wifi.tether.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.android.settings.R;
import com.android.settings.SeekBarDialogPreference;

import com.google.android.material.slider.Slider;

public class WifiTetherClientLimitPreference extends SeekBarDialogPreference implements
        Slider.OnChangeListener {

    private Context mContext;
    private Slider mSlider;
    private int mValue;
    private int mMin;
    private int mMax;

    public WifiTetherClientLimitPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
        setText(getSummaryForValue((int) value));
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        setText(getSummaryForValue(mValue));

        mSlider = getSlider(view);
        if (mSlider != null) {
            mSlider.clearOnChangeListeners();
            if (mMax > mMin) {
                mSlider.setValueFrom(mMin);
                mSlider.setValueTo(mMax);
                mSlider.setStepSize(1f);
                mSlider.setValue(Math.max(mMin, Math.min(mMax, mValue)));
            }
            mSlider.addOnChangeListener(this);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult && mSlider != null) {
            setValue((int) mSlider.getValue(), true);
        }
    }

    private String getSummaryForValue(int value) {
        return mContext.getResources().getQuantityString(
            R.plurals.wifi_hotspot_client_limit_summary, value, value);
    }

    public void setMin(int min) {
        mMin = min;
    }

    public void setMax(int max) {
        mMax = max;
    }

    public void setValue(int value, boolean callListener) {
        if (value == 0) value = mMax;
        mValue = value;
        String summary = getSummaryForValue(value);
        setSummary(summary);
        setText(summary);
        if (callListener) callChangeListener(value);
    }

    public int getValue() {
        return mValue;
    }
}
