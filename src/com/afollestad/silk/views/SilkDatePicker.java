package com.afollestad.silk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.afollestad.silk.R;

import java.util.Calendar;

/**
 * A date picker that takes up less vertical space than the stock DatePicker.
 *
 * @author Aidan Follestad (afollestad)
 */
public class SilkDatePicker extends LinearLayout {

    public SilkDatePicker(Context context) {
        super(context);
        init();
    }

    public SilkDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SilkDatePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public class SilkSpinnerAdapter extends ArrayAdapter<String> {
        public SilkSpinnerAdapter(Context context) {
            super(context, R.layout.spinner_item);
            super.setDropDownViewResource(R.layout.spinner_item_dropdown);
        }
    }


    private Calendar mCal;
    private int mCurrentYear;
    private SilkSpinnerAdapter mMonth;
    private SilkSpinnerAdapter mDay;
    private SilkSpinnerAdapter mYear;

    public Calendar getCalendar() {
        return mCal;
    }

    public int getMinYear() {
        return mCurrentYear - 100;
    }

    public int getMaxYear() {
        return mCurrentYear + 100;
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setWeightSum(3);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.silk_date_picker, this, true);

        mCal = Calendar.getInstance();
        mCurrentYear = mCal.get(Calendar.YEAR);
        Spinner monthSpinner = (Spinner) getChildAt(0);
        Spinner daySpinner = (Spinner) getChildAt(1);
        Spinner yearSpinner = (Spinner) getChildAt(2);

        mMonth = new SilkSpinnerAdapter(getContext());
        mDay = new SilkSpinnerAdapter(getContext());
        mYear = new SilkSpinnerAdapter(getContext());
        monthSpinner.setAdapter(mMonth);
        daySpinner.setAdapter(mDay);
        yearSpinner.setAdapter(mYear);

        fillMonths();
        fillDays();
        fillYears();
        monthSpinner.setSelection(mCal.get(Calendar.MONTH));
        daySpinner.setSelection(mCal.get(Calendar.DAY_OF_MONTH) - 1);
        yearSpinner.setSelection(mCurrentYear - getMinYear());

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCal.set(Calendar.MONTH, position);
                fillDays();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCal.set(Calendar.DAY_OF_MONTH, position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCal.set(Calendar.YEAR, getMinYear() + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fillMonths() {
        mMonth.clear();
        String[] months = getContext().getResources().getStringArray(R.array.months);
        mMonth.addAll(months);
        mMonth.notifyDataSetChanged();
    }

    private void fillDays() {
        int daysInMonth = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        mDay.clear();
        for (int i = 1; i <= daysInMonth; i++) mDay.add(i + "");
        mDay.notifyDataSetChanged();
    }

    private void fillYears() {
        mYear.clear();
        for (int i = getMinYear(); i <= getMaxYear(); i++) mYear.add(i + "");
        mYear.notifyDataSetChanged();
    }
}