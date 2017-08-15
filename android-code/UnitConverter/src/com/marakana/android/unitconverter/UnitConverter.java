package com.marakana.android.unitconverter;

public interface UnitConverter {
	public double toBaseUnit(double amount);
	public double toUnit(double baseUnitAmount);
}
