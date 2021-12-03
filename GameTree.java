import java.util.ArrayList;

/**
 * @author Nick
 * @version 1.0
 */
public class GameTree {
    /**
     * @author Nick
     * @version 1.0
     */
    public static class Node {

        State.Move move;
        int val;
        Node[] children;
        Node bestChild;

        /**
         *
         * @param m State.Move
         */
        public Node(State.Move m) {
            this.move = m;
        }

        /**
         *
         * @return int
         */
        public int getValue() {
            return this.val;
        }

        /**
         *
         * @return State.Move
         */
        public State.Move getMove() {
            return this.move;
        }

        /**
         *
         * @return Node
         */
        public Node getBestChild() {
            return this.bestChild;
        }

        /**
         *
         * @param m State.Move
         * @return node
         */
        public Node findChild(State.Move m) {
            for (int i = 0; i < children.length; i++) {
                if (children[i].move.equals(m)) {
                    return children[i];
                }
            }
            return null;
        }

        /**
         *
         * @return String
         */
        public String getPrediction() {
            if (val == -1) {
                return "player";
            } else if (val == 1) {
                return "computer";
            } else {
                return "no one";
            }
        }
    }

    Node root;
    int size;

    /**
     *
     * @param start State
     */
    public GameTree(State start) {
        this.size = 0;
        this.root = buildTree(start, null);
    }

    /**
     *
     * @return Node
     */
    public Node getRoot() {
        return this.root;
    }

    /**
     *
     * @param state State
     * @param move State.Move
     * @return node
     */
    public Node buildTree(State state, State.Move move) {
        Node n = new Node(move);
        size++;
        ArrayList<State.Move> moves = state.findAllMoves();
        if (moves.size() == 0) {
            n.val = state.getValue();
        }
        n.children = new Node[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            state.doMove(moves.get(i));
            Node child = buildTree(state, moves.get(i));
            n.children[i] = child;
            state.undoMove(moves.get(i));
            if (n.bestChild == null) {
                n.bestChild = child;
                n.val = child.val;
            } else if (!state.isPlayerTurn() && child.val > n.val) {
                n.bestChild = child;
                n.val = child.val;
            } else if (state.isPlayerTurn() && child.val < n.val) {
                n.bestChild = child;
                n.val = child.val;
            }
        }
        return n;
    }

    /**
     *
     * @return int
     */
    public int getSize() {
        return this.size;
    }

}
