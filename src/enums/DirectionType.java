package enums;

public enum DirectionType {
	DIRECTION_AWAY, DIRECTION_TOWARD {
		@Override
		public DirectionType next() {
			return null;
		};
	};
	
	public DirectionType next() {
        return values()[ordinal() + 1];
    }

	public static DirectionType first() {
		return values()[0];
	}
}
