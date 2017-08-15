package com.marakana.android.unitconverter;

public enum UnitConversionCategory {
	MASS {
		@Override
		public UnitConverter getUnitConverter(int unit) {
			return MassConverter.values()[unit];
		}

		@Override
		public int getArrayResourceId() {
			return R.array.mass_category;
		}
	},
	LENGTH {
		@Override
		public UnitConverter getUnitConverter(int unit) {
			return LengthConverter.values()[unit];
		}

		@Override
		public int getArrayResourceId() {
			return R.array.length_category;
		}
	},
	TEMPERATURE {
		@Override
		public UnitConverter getUnitConverter(int unit) {
			return TemperatureConverter.values()[unit];
		}

		@Override
		public int getArrayResourceId() {
			return R.array.temperature_category;
		}
	};
	public abstract UnitConverter getUnitConverter(int unit);

	public abstract int getArrayResourceId();
}
