public class Moon {

    private int[] pos;
    private int[] vel;

    public Moon(int x, int y, int z) {
        pos = new int[]{x, y, z};
        vel = new int[]{0, 0, 0};
    }

    public Moon(int[] totalInit) {
        pos = new int[]{totalInit[0], totalInit[1], totalInit[2]};
        vel = new int[]{totalInit[3], totalInit[4], totalInit[5]};
    }

    public void updatePos() {
        for (int i = 0; i < pos.length; i++) {
            pos[i] += vel[i];
        }
    }

    public long energy() {
        long potential = 0;
        long kinetic = 0;
        for (int i = 0; i < pos.length; i++) {
            potential += Math.abs(pos[i]);
            kinetic += Math.abs(vel[i]);
        }
        return potential * kinetic;
    }

    public int[] getPos() {
        return pos;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }

    public int[] getVel() {
        return vel;
    }

    public void setVel(int[] vel) {
        this.vel = vel;
    }

}
