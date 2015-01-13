package enums;

public enum HandType {
	HAND_LEFT, HAND_RIGHT {
		@Override
		public HandType next() {
			return null;
		};
	};
	
	public HandType next() {
        return values()[ordinal() + 1];
    }
	
	public static HandType first() {
		return values()[0];
	}
}
