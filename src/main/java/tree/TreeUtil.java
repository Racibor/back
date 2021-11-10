package tree;

import java.util.*;
import java.util.stream.Collectors;

public class TreeUtil {
    public static Node nestFlatTree(Node flatTree) {
        List<Node> tree = (List<Node>) flatTree.get("children");
        if(tree == null) {
            throw new IllegalArgumentException();
        }
        return nestFlatTree(tree);
    }

    public static Node nestFlatTree(List<Node> flatTree) {
        Stack<Node> level = new Stack<>();
        Node nestedTree = new Node();
            for(Node node : flatTree) {
                Node nestedNode = new Node();
                String id = (String)node.get("id");
                nestedNode.put("id", id);
                if(level.isEmpty()) {
                    nestedTree.put("null", nestedNode);
                    level.push(nestedNode);
                    continue;
                }
                while(!((String)level.peek().get("id")).equals((String)node.get("parentId"))) {
                    level.pop();
                    if(level.isEmpty()) {
                        throw new IllegalArgumentException("error in tree structure (incorrectly assigned ids)");
                    }
                }

                Node currentNode = level.peek();
                Set<Map.Entry<String, Object>> entries = node.getNodeObjects();
                entries.stream()
                      .filter(entry -> !"children".equals(entry.getKey())
                               && !"parentId".equals(entry.getKey()))
                      .forEach(entry -> nestedNode.put(entry.getKey(), entry.getValue()));
                List<Node> childrenList = (List<Node>) currentNode.get("children");
                if(childrenList == null) {
                    childrenList = new ArrayList<Node>();
                    currentNode.put("children", childrenList);
                    childrenList.add(nestedNode);
                    level.push(nestedNode);
                } else {
                    childrenList.add(nestedNode);
                    level.push(nestedNode);
                }
            }
            return (Node) nestedTree.get("null");
    }

    public static List<List<Node>> flattenNestedTree(List<Node> nodes) {
        return nodes.stream().map(node -> flattenNestedTree(node)).collect(Collectors.toList());
    }

    public static List<Node> flattenNestedTree(Node root) throws IllegalArgumentException {
        if(root == null) {
            throw new IllegalArgumentException("");
        }
        List<Node> container = new ArrayList<>();
        try {
            Stack<Node> stack = new Stack<>();
            Stack<String> parent = new Stack<>();
            stack.push(root);
            while (!stack.isEmpty()) {
                Node node = stack.pop();
                Node newNode = new Node();
                String nodeId = (String) node.get("id");
                newNode.put("id", nodeId);
                newNode.put("parentId", (parent.isEmpty() ? "null" : parent.pop()));
                Set<Map.Entry<String, Object>> entries = node.getNodeObjects();
                entries.stream()
                        .filter(entry -> !entry.getKey()
                                .equals("children"))
                        .forEach(entry -> newNode.put(entry.getKey(), entry.getValue()));
                Object children = node.get("children");
                List<Node> childrenList = (List<Node>) children;
                if (childrenList != null) {
                    ListIterator<Node> it = childrenList.listIterator(childrenList.size());
                    while (it.hasPrevious()) {
                        Node child = it.previous();
                        parent.push((String) nodeId);
                        stack.push(child);
                    }
                }
                container.add(newNode);
            }
            return container;
        } catch(ClassCastException ex) {
            throw new IllegalArgumentException("error in tree structure (expecting list of child nodes under the \"children\" key)");
        }
    }

    public static void printTree(List<Node> nodes) {
        nodes.forEach(TreeUtil::printTree);
    }

    public static void printTree(Node node) {
        System.out.print("{");
        System.out.print(" \"id\": ");
        String id = (String)node.get("id");
        if(id != null) {
            System.out.print(id);
        }
        String parentId = (String)node.get("parentId");
        if(parentId != null) {
            System.out.print(", \"parentId\": " + parentId);
        }

        List<Node> childrenList = (List<Node>) node.get("children");
        if(childrenList != null) {
            System.out.print(", \"children\": [");
            childrenList.forEach(e -> TreeUtil.printTree(e, true));
            System.out.print("]");
        }

        System.out.print("}\n");
    }

    private static void printTree(Node node, Boolean tmp) {
        System.out.print("{");
        System.out.print(" \"id\": ");
        String id = (String)node.get("id");
        if(id != null) {
            System.out.print(id);
        }
        String parentId = (String)node.get("parentId");
        if(parentId != null) {
            System.out.print(", \"parentId\": " + parentId);
        }

        List<Node> childrenList = (List<Node>) node.get("children");
        if(childrenList != null) {
            System.out.print(", \"children\": [");
            childrenList.forEach(e -> TreeUtil.printTree(e, true));
            System.out.print("]");
        }
        System.out.print("}");
    }

}
