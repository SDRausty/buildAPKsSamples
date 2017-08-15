package com.marakana.android.unitconverter;

public enum LengthConverter implements UnitConverter {
	mm {
		@Override
		public double toBaseUnit(double amount) {
			return amount;
		}

	},
	m {
		@Override
		public double toBaseUnit(double amount) {
			return amount * 1000.00;
		}

	},
	km {
		@Override
		public double toBaseUnit(double amount) {
			return amount * 1000000.00;
		}

	},
	in {
		@Override
		public double toBaseUnit(double amount) {
			return amount * 25.4;
		}

	},
	ft {
		@Override
		public double toBaseUnit(double amount) {
			return amount * 304.8;
		}

	},
	yd {
		@Override
		public double toBaseUnit(double amount) {
			return amount * 914.4;
		}

	},
	mi {
		@Override
		public double toBaseUnit(double amount) {
			return amount * 1609344.00;
		}

	};
	public abstract double toBaseUnit(double amount);
	
	public double toUnit(double baseUnitAmount) {
		return baseUnitAmount * (1 / (toBaseUnit(1)));
	}
}