package tree.parser;

import org.junit.jupiter.api.Test;
import tree.Node;
import tree.parser.SimpleTreeParser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleTreeParserTest {
    TreeParser parser = new SimpleTreeParser();

    @Test
    public void blankScenario() {
        List<Node> list = parser.parse("");
        assertEquals(list.size(), 0);
    }

    @Test
    public void singleNodeScenario() {
        List<Node> list = parser.parse("{}");
        assertEquals(list.size(), 1);
        assertInstanceOf(Node.class, list.get(0));
    }

    @Test
    public void singleArrayScenario() {
        List<Node> list = parser.parse("[]");
        assertEquals(list.size(), 0);
        assertInstanceOf(ArrayList.class, list);
    }

    @Test
    public void arrayInsideNodeScenario() {
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse("{[]}");
        });
    }

    @Test
    public void NodeInsideArrayScenario() {
        List<Node> list = parser.parse("[{}]");
        assertInstanceOf(Node.class, list.get(0));

        list = parser.parse("[{},{},{}]");
        assertEquals(list.size(), 3);
        list.forEach(e -> assertInstanceOf(Node.class, e));

        list = parser.parse("[{}{}{}]");
        assertEquals(list.size(), 3);
        list.forEach(e -> assertInstanceOf(Node.class, e));
    }

    @Test
    public void NodeWithStringValuesScenario() {
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse("{key}");
        });

        List<Node> list = parser.parse("{key:value}");
        Node root = list.get(0);
        assertNotNull(root.get("key"));
        assertInstanceOf(String.class, root.get("key"));

        String val = (String)root.get("key");
        assertEquals(val, "value");

        //----

        List<Node> list2 = parser.parse("{key:value, key2:value2}");
        Node root2 = list2.get(0);
        assertNotNull(root2.get("key"));
        assertInstanceOf(String.class, root2.get("key"));

        String val2 = (String)root.get("key");
        assertEquals(val2, "value");

        assertNotNull(root2.get("key2"));
        assertInstanceOf(String.class, root2.get("key2"));

        val2 = (String)root2.get("key2");
        assertEquals(val2, "value2");
    }

    @Test
    //first array must contain nodes only
    public void ArrayWithStringValuesScenario() {
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse("[test,test2]");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse("[key:value]");
        });
    }

    @Test
    public void nestedNodesScenario() {
        List<Node> list = parser.parse("{id:1, leaf:{id:2,leaf:{id:3}}}");
        Node root = list.get(0);
        assertInstanceOf(String.class, root.get("id"));
        assertEquals((String)root.get("id"), "1");

        assertInstanceOf(Node.class, root.get("leaf"));
        Node firstLeaf = (Node) root.get("leaf");
        assertInstanceOf(String.class, firstLeaf.get("id"));
        assertEquals((String)firstLeaf.get("id"), "2");

        assertInstanceOf(Node.class, firstLeaf.get("leaf"));
        Node secondLeaf = (Node) firstLeaf.get("leaf");
        assertInstanceOf(String.class, secondLeaf.get("id"));
        assertEquals((String)secondLeaf.get("id"), "3");
    }

    @Test
    public void nestedNodesAndArraysScenario() {
        List<Node> list = parser.parse("{vals:[test, [test2]]}");
        List<Object> array = (List<Object>)list.get(0).get("vals");
        assertInstanceOf(String.class, array.get(0));
        assertEquals((String) array.get(0), "test");

        assertInstanceOf(ArrayList.class, array.get(1));
        List<Object> secondArray = (List<Object>) array.get(1);
        assertInstanceOf(String.class, secondArray.get(0));
        assertEquals((String) secondArray.get(0), "test2");
    }
}
