package com.marakana.android.unitconverter;

public enum MassConverter implements UnitConverter {
	g {
		@Override
		public double toBaseUnit(double amount) {
			return amount;
		}

	},
	kg {
		@Override
		public double toBaseUnit(double amount) {
			return amount * 1000.00;
		}

	},
	oz {
		@Override
		public double toBaseUnit(double amount) {
			return amount * 28.3495231;
		}

	},
	lb {
		@Override
		public double toBaseUnit(double amount) {
			return amount * 453.59237;
		}

	};
	public abstract double toBaseUnit(double amount);

	public double toUnit(double baseUnitAmount) {
		return baseUnitAmount * (1 / (toBaseUnit(1)));
	}
}