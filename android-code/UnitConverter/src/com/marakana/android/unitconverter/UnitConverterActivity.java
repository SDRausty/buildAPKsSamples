package com.marakana.android.unitconverter;

import android.app.Activity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UnitConverterActivity extends Activity implements
		OnItemSelectedListener, OnClickListener, TextWatcher {
	private static final String TAG = "UnitConverterActivity";

	private Spinner category;
	private EditText inputAmount;
	private Spinner inputUnit;
	private TextView outputAmount;
	private Spinner outputUnit;
	private View reverseUnits;
	private View clearInput;
	private View copyResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unit_converter);
		this.category = (Spinner) super.findViewById(R.id.category);
		this.category.setOnItemSelectedListener(this);
		this.inputUnit = (Spinner) super.findViewById(R.id.input_unit);
		this.inputUnit.setOnItemSelectedListener(this);
		this.outputUnit = (Spinner) super.findViewById(R.id.output_unit);
		this.outputUnit.setOnItemSelectedListener(this);
		this.inputAmount = (EditText) super.findViewById(R.id.input_amount);
		this.inputAmount.setText("1");
		this.inputAmount.addTextChangedListener(this);
		this.outputAmount = (TextView) super.findViewById(R.id.output_amount);
		this.reverseUnits = super.findViewById(R.id.reverse_units);
		this.reverseUnits.setOnClickListener(this);
		this.clearInput = super.findViewById(R.id.clear_input);
		this.clearInput.setOnClickListener(this);
		this.copyResult = super.findViewById(R.id.copy_result);
		this.copyResult.setOnClickListener(this);
		this.initSpinners(0);
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == category) {
			Log.d(TAG, "Changing category to position " + position);
			initSpinners(position);
		} else if (parent == inputUnit || parent == outputUnit) {
			this.recalculate();
		} else {
			throw new AssertionError(
					"Unexpected item selected on parent view: " + parent);
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// ignored
	}

	public void onClick(View view) {
		if (view == this.reverseUnits) {
			int inputUnitPosition = this.inputUnit.getSelectedItemPosition();
			this.inputUnit.setSelection(this.outputUnit
					.getSelectedItemPosition());
			this.outputUnit.setSelection(inputUnitPosition);
		} else if (view == this.clearInput) {
			this.inputAmount.setText("");
		} else if (view == this.copyResult) {
			CharSequence result = this.outputAmount.getText();
			if (result.length() > 0) {
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				clipboard.setText(result);
				Toast.makeText(this,
						super.getString(R.string.copy_result_message, result),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			throw new AssertionError("Unexpected click on view: " + view);
		}
		this.recalculate();
	}

	private void recalculate() {
		String inputString = this.inputAmount.getText().toString();
		int categorySelection = this.category.getSelectedItemPosition();
		int inputUnitSelection = this.inputUnit.getSelectedItemPosition();
		int outputUnitSelection = this.outputUnit.getSelectedItemPosition();
		String outputString = null;
		if (inputString == null || inputString.length() == 0) {
			outputString = "";
		} else if (inputUnitSelection == outputUnitSelection) {
			outputString = inputString;
		} else {
			try {
				double input = Double.parseDouble(inputString);
				UnitConverter inputConverter = getUnitConverter(
						categorySelection, inputUnitSelection);
				UnitConverter outputConverter = getUnitConverter(
						categorySelection, outputUnitSelection);
				Log.d(TAG, "Converting [" + input + "] from [" + inputConverter
						+ "] to [" + outputConverter + "]");
				double output = outputConverter.toUnit(inputConverter
						.toBaseUnit(input));
				outputString = String.valueOf(output);
			} catch (NumberFormatException e) {
				outputString = "";
			}
		}
		this.copyResult
				.setVisibility(outputString.length() == 0 ? View.INVISIBLE
						: View.VISIBLE);
		this.outputAmount.setText(outputString);
	}

	private int getCategoryArrayResId(int category) {
		return UnitConversionCategory.values()[category].getArrayResourceId();
	}

	private UnitConverter getUnitConverter(int category, int unitSelection) {
		return UnitConversionCategory.values()[category]
				.getUnitConverter(unitSelection);
	}

	private void initSpinners(int category) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, this.getCategoryArrayResId(category),
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.inputUnit.setAdapter(adapter);
		this.outputUnit.setAdapter(adapter);
	}

	public void afterTextChanged(Editable s) {
		this.recalculate();
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// ignored
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// ignored
	}
}