package mod.dooggoo.createatomic.api;

public enum Directions {

        /** -Z */
        NORTH(0, 0, -1),
    
        /** +Z */
        SOUTH(0, 0, 1),
    
        /** -X */
        WEST(-1, 0, 0),
    
        /** +X */
        EAST(1, 0, 0);

        public final int offsetX;
        public final int offsetY;
        public final int offsetZ;
        public final int flag;


        Directions(int x, int y, int z)
        {
            offsetX = x;
            offsetY = y;
            offsetZ = z;
            flag = 1 << ordinal();
        }
}
