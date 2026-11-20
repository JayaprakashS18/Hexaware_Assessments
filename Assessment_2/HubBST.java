
package logistics;

public class HubBST {

    // BST Node
    private class Node {

        Hub data;
        Node left;
        Node right;

        Node(Hub data) {
            this.data = data;
            left = null;
            right = null;
        }
    }

    private Node root = null;

    // Insert hub
    public boolean insert(Hub hub) {

        if (hub == null) {
            return false;
        }

        if (root == null) {
            root = new Node(hub);
            return true;
        }

        Node current = root;

        while (true) {

            int comparison = hub.getHubCode()
                    .compareTo(current.data.getHubCode());

            if (comparison < 0) {

                if (current.left == null) {
                    current.left = new Node(hub);
                    return true;
                }

                current = current.left;
            }

            else if (comparison > 0) {

                if (current.right == null) {
                    current.right = new Node(hub);
                    return true;
                }

                current = current.right;
            }

            else {
                return false;
            }
        }
    }

    // Search hub
    public Hub search(String hubCode) {

        if (hubCode == null) {
            return null;
        }

        Node current = root;

        while (current != null) {

            int comparison = hubCode.compareTo(
                    current.data.getHubCode());

            if (comparison == 0) {
                return current.data;
            }

            else if (comparison < 0) {
                current = current.left;
            }

            else {
                current = current.right;
            }
        }

        return null;
    }

    // Calculate BST height
    public int height() {
        return calculateHeight(root);
    }

    private int calculateHeight(Node node) {

        if (node == null) {
            return -1;
        }

        int leftHeight = calculateHeight(node.left);
        int rightHeight = calculateHeight(node.right);

        return 1 + Math.max(leftHeight, rightHeight);
    }

    // Display hubs in ascending order
    public void displayInOrder() {

        if (root == null) {
            System.out.println("No hubs available.");
            return;
        }

        System.out.println("\nHubs in sorted order:");

        inOrder(root);
    }

    private void inOrder(Node node) {

        if (node != null) {

            inOrder(node.left);

            System.out.println(node.data);

            inOrder(node.right);
        }
    }

    // Check whether BST is empty
    public boolean isEmpty() {
        return root == null;
    }
}
