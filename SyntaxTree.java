public class SyntaxTree {
    private SyntaxTree root;
    private SyntaxTree left;
    private SyntaxTree right;

    public SyntaxTree(SyntaxTree root) {
        this.root = root;
        this.left = null;
        this.right = null;
    }

    public SyntaxTree getRoot() {
        return root;
    }

    public void setRoot(SyntaxTree root) {
        this.root = root;
    }

    public SyntaxTree getLeft() {
        return left;
    }

    public void setLeft(SyntaxTree left) {
        this.left = left;
    }

    public SyntaxTree getRight() {
        return right;
    }

    public void setRight(SyntaxTree right) {
        this.right = right;
    }
}