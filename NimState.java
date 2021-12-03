import java.util.ArrayList;

/**
 * @author Nick
 * @version 1.0
 */
public class NimState implements State {
    /**
     * @author Nick
     * @version 1.0
     */
    public static class Move implements State.Move {

        int stack;
        int num;

        /**
         *
         * @param stack int
         * @param num int
         */
        public Move(int stack, int num) {
            this.stack = stack;
            this.num = num;
        }

        /**
         *
         * @param o obj
         * @return boolean
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof Move) {
                Move m = (Move) o;
                return m.stack == stack && m.num == num;
            }
            return false;
        }

        /**
         *
         * @return string
         */
        @Override
        public String toString() {
            return "Taking " + num + " from stack " + stack;
        }

    }

    int[] stacks;
    boolean pt;

    /**
     *
     * @param n int
     * @param pt pt
     */
    public NimState(int n, boolean pt) {
        this.stacks = new int[n];
        for (int i = 0; i < n; i++) {
            stacks[i] = i + 1;
        }
        this.pt = pt;
    }

    /**
     *
     * @param stacks int[]
     * @param pt boolean
     */
    public NimState(int[] stacks, boolean pt) {
        this.pt = pt;
        this.stacks = new int[stacks.length];

        for (int i = 0; i < stacks.length; i++) {
            this.stacks[i] = stacks[i];
            new NimState(this.stacks[i], pt);
        }
    }

    /**
     *
     * @return int[]
     */
    public int[] getStacks() {
        return this.stacks;
    }

    /**
     *
     * @return boolean
     */
    @Override
    public boolean isPlayerTurn() {
        return this.pt;
    }

    /**
     *
     * @return string
     */
    @Override
    public String toString() {
        String str = "";

        for (int stack = 1; stack <= stacks.length; stack++) {
            str += stack + ": ";
            for (int num = 1; num <= stacks[stack - 1]; num++) {
                str += "X";
            }
            str += "\n";
        }
        return str;
    }

    /**
     *
     * @return boolean
     */
    @Override
    public boolean gameOver() {
        for (Integer i : stacks) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return int
     */
    @Override
    public int getValue() {
        if (!gameOver()) {
            throw new IllegalStateException();
        }
        if (pt) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     *
     * @param move Move The move to be done.
     *
     * @return boolean
     */
    @Override
    public boolean doMove(State.Move move) {
        Move m = (Move) move;

        if (m.stack > stacks.length || m.num > stacks[m.stack - 1] || m.num <= 0 || m.num > 3) {
            return false;
        } else {
            stacks[m.stack - 1] -= m.num;
            pt = !pt;
            return true;
        }
    }

    /**
     *
     * @param move Move The move to be undone.
     */
    @Override
    public void undoMove(State.Move move) {
        Move m = (Move) move;
        stacks[m.stack - 1] += m.num;
        pt = !pt;
    }

    /**
     *
     * @return ArrayList of State.Move
     */
    @Override
    public ArrayList<State.Move> findAllMoves() {
        ArrayList<State.Move> moves = new ArrayList<>();
        for (int stack = 1; stack <= stacks.length; stack++) {
            for (int num = 1; stacks[stack - 1] >= num && num <= 3; num++) {
                moves.add(new Move(stack, num));
            }
        }
        return moves;
    }

}
