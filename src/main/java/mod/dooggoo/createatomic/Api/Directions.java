package mod.dooggoo.createatomic.api;

public enum Directions {

    	/** -Y */
        DOWN(0, -1, 0),

        /** +Y */
        UP(0, 1, 0),
    
        /** -Z */
        NORTH(0, 0, -1),
    
        /** +Z */
        SOUTH(0, 0, 1),
    
        /** -X */
        WEST(-1, 0, 0),
    
        /** +X */
        EAST(1, 0, 0),
    
        UNKNOWN(0, 0, 0);

        public final int offsetX;
        public final int offsetY;
        public final int offsetZ;
        public final int flag;
        public static final Directions[] VALID_DIRECTIONS = {DOWN, UP, NORTH, SOUTH, WEST, EAST};


        private Directions(int x, int y, int z)
        {
            offsetX = x;
            offsetY = y;
            offsetZ = z;
            flag = 1 << ordinal();
        }
}
