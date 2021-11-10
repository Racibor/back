package tree.parser;

import tree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class SimpleTreeParser implements TreeParser{
    private Stack<Node> nodeStack;
    private Stack<List<Object>> arrayStack;
    private Stack<TreeParserStatus> statusStack;
    private Node lastNode = null;
    private List<Object> lastArray = null;
    private TreeParserStatus lastStatus = null;
    private int index = 0;
    private StringBuilder key = null;
    private StringBuilder value = null;
    private boolean val = false;

    public SimpleTreeParser() {

    }

    private void init() {
        nodeStack = new Stack<>();
        arrayStack = new Stack<>();
        statusStack = new Stack<>();
        key = new StringBuilder();
        value = new StringBuilder();
        val = false;
        lastNode = null;
        lastArray = null;
        lastStatus = null;
        index = 0;
    }

    private boolean inArray() {
        if(statusStack.isEmpty()) {
            throw new IllegalArgumentException("parsing error at index: " + index + " ]");
        }
        return statusStack.peek().equals(TreeParserStatus.ARRAY);
    }


    private boolean inNode() {
        if(statusStack.isEmpty()) {
            throw new IllegalArgumentException("parsing error at index: " + index + " ]");
        }
        return statusStack.peek().equals(TreeParserStatus.NODE);
    }

    private void createArray(List<Object> node) {
        statusStack.push(TreeParserStatus.ARRAY);
        arrayStack.push(node);
        val = true;
    }

    private void addToArray(Object obj) {
        arrayStack.peek().add(obj);
    }

    private List<Object> getArray() {
        return arrayStack.peek();
    }

    private List<Object> popArray() {
        if(arrayStack.isEmpty()) {
            throw new IllegalArgumentException("parsing error at index: " + index + " - unexpected \"]\" sign]");
        }
        lastStatus = statusStack.pop();
        lastArray = arrayStack.pop();
        return lastArray;
    }

    private void createNode(Node node) {
        statusStack.push(TreeParserStatus.NODE);
        nodeStack.push(node);
        val = false;
    }
    private void putInNode(String key, Object obj) {
        nodeStack.peek().put(key, obj);
    }

    private Node getNode() {
        return nodeStack.peek();
    }

    private Node popNode() {
        if(nodeStack.isEmpty()) {
            throw new IllegalArgumentException("parsing error at index: " + index + " - unexpected \"}\" sign");
        }
        lastStatus = statusStack.pop();
        lastNode = nodeStack.pop();
        return lastNode;
    }

    public List<Node> parse(String text) throws IllegalArgumentException {
        init();
        String chars[] = text.trim().split("");

        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();

        while(index < chars.length) {
            switch(chars[index]) {
                case "{":
                    Node node = new Node();
                    if(!nodeStack.isEmpty()) {
                        if(inArray()) {
                            val = true;
                        }
                        if(val) {
                            if(inNode()) {
                                putInNode(key.toString(), node);
                                if(value.length() > 0) {
                                    throw new IllegalArgumentException("parsing error at index: " + index + " - wrong value");
                                }
                                key.setLength(0);
                                createNode(node);
                            } else if(inArray()) {
                                addToArray(node);
                                if(value.length() > 0) {
                                    throw new IllegalArgumentException("parsing error at index: " + index + " - wrong value");
                                }
                                createNode(node);
                            }
                        } else {
                            throw new IllegalArgumentException("parsing error at index: " + index + " - node can't be a key");
                        }
                    } else {
                        if(!arrayStack.isEmpty()) {
                            if(inArray()) {
                                addToArray(node);
                            }
                        }
                        createNode(node);
                    }

                    break;
                case "}":
                    if(value.length() > 0 && key.length() > 0) {
                        putInNode(key.toString(), value.toString());
                    } else if(key.length() > 0) {
                        throw new IllegalArgumentException("parsing error at index: " + index + " - no value given");
                    }

                    if(inNode()) {
                        val = false;
                    } else if (inArray()) {
                        val = true;
                    }

                    value.setLength(0);
                    key.setLength(0);
                    popNode();
                    break;
                case "[":
                    List<Object> list = new ArrayList<>();
                    if(!arrayStack.isEmpty()) {
                        if(val) {
                            if(inNode()) {
                                putInNode(key.toString(), list);
                                if(value.length() > 0) {
                                    throw new IllegalArgumentException("parsing error at index: " + index + " - wrong value");
                                }
                                key.setLength(0);
                                createArray(list);
                            } else if(inArray()) {
                                addToArray(list);
                                if(value.length() > 0 || key.length() > 0) {
                                    throw new IllegalArgumentException("parsing error at index: " + index + " - wrong value");
                                }
                                createArray(list);
                            }
                        } else {
                            throw new IllegalArgumentException("parsing error at index: " + index + " - array can't be a key");
                        }
                    } else {
                        if(!statusStack.isEmpty()) {
                            if(!val) {
                                throw new IllegalArgumentException("parsing error at index: " + index + " - array can't be a key");
                            }
                            putInNode(key.toString(), list);
                            if(value.length() > 0) {
                                throw new IllegalArgumentException("parsing error at index: " + index + " - wrong value");
                            }
                            key.setLength(0);
                        }
                        createArray(list);
                    }
                    break;
                case "]":

                    if(value.length() > 0) {
                        addToArray(value.toString());
                    }

                    if(inNode()) {
                        val = false;
                    } else if (inArray()) {
                        val = true;
                    }

                    value.setLength(0);
                    key.setLength(0);
                    popArray();
                    break;
                case ":":
                    if(val) {
                        throw new IllegalArgumentException("parsing error at index: " + index);
                    } else {
                        val = true;
                    }
                    break;
                case ",":
                    if(inNode()) {
                        val = false;
                        if (value.length() > 0 && key.length() > 0) {
                            putInNode(key.toString(), value.toString());
                        }
                        value.setLength(0);
                        key.setLength(0);
                    } else if(inArray()) {
                        val = true;
                        if(value.length() > 0) {
                            addToArray(value.toString());
                            value.setLength(0);
                        }
                    }
                    break;
                default:
                    if(!chars[index].isEmpty() && Character.isLetterOrDigit(chars[index].charAt(0))) {
                        if(val) {
                            value.append(chars[index]);
                        } else {
                            key.append(chars[index]);
                        }
                    }
                    break;
            }
            index++;
        }
        if(!arrayStack.isEmpty()) {
            throw new IllegalArgumentException("parsing error: unclosed arrays");
        } else if(!nodeStack.isEmpty()) {
            throw new IllegalArgumentException("parsing error: unclosed nodes");
        }
        List<Node> list = new ArrayList<>();
        if(lastStatus == null) {
            return list;
        }
        if(lastStatus.equals(TreeParserStatus.NODE)) {
            list.add(lastNode);
            return list;
        } else {
            try {
                list = lastArray.stream().map(e -> (Node) e).collect(Collectors.toList());
            } catch(ClassCastException ex) {
                throw new IllegalArgumentException("parsing error: first array must contain nodes only");
            }
            return list;
        }

    }
}

