
enum VisionType {
	FULL_VISION, RIGHT_VISION, LEFT_VISION {
		@Override
		public VisionType next() {
			return null;
		};
	};
	
	public VisionType next() {
        return values()[ordinal() + 1];
    }
}
