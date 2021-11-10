package tree;

import org.junit.jupiter.api.Test;
import tree.parser.SimpleTreeParser;
import tree.parser.TreeParser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TreeUtilTest {
    List<Node> flatTree;
    List<Node> brokenFlatTree;
    List<Node> nestedTree;
    List<Node> brokenNestedTree;
    List<Node> customTree;

    public TreeUtilTest() {
        TreeParser parser = new SimpleTreeParser();
        flatTree = parser.parse("""
                    [
                    { id: 1, parentId: null },
                    { id: 2, parentId: 1 },
                    { id: 3, parentId: 1 },
                    { id: 4, parentId: 3 } ]
                """);
        //tree below has incorrectly assigned parentId
        brokenFlatTree = parser.parse("""
                    [
                    { id: 1, parentId: null },
                    { id: 2, parentId: 1 },
                    { id: 3, parentId: 5 },
                    { id: 4, parentId: 3 } ]
                """);
        nestedTree = parser.parse("""
                    [
                      {
                         id: 1,
                         children: [
                              { id: 2 },
                              { id: 3, children: [
                                   { id: 4 }
                              ] },
                         ],
                      },
                    ]
                """);

        //tree below has array of values instead of nodes
        brokenNestedTree = parser.parse("""
                    [
                      {
                         id: 1,
                         children: [
                              { id: 2 },
                              { id: 3, children: [
                                   test
                              ] },
                         ],
                      },
                    ]
                """);
        customTree = parser.parse("""
                [
                      {
                         id: 1,
                         children: [
                              { id: 2, children: [
                                { id:5, children: [
                                    { id: 6 }
                                ]}, { id: 7}
                              ] },
                              { id: 3, children: [
                                   { id: 4, children: [
                                    { id: 8}
                                   ] }
                              ] },
                         ],
                      },
                    ]
                """);
    }

    @Test
    public void flattenTreeTest() {
        List<Node> flattenedTree = TreeUtil.flattenNestedTree(nestedTree.get(0));
        assertEquals(flattenedTree, flatTree);
    }

    @Test
    public void flattenBrokenTreeTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            List<Node> flattenedTree = TreeUtil.flattenNestedTree(brokenNestedTree.get(0));
        });
    }

    @Test
    public void nestTreeTest() {
        Node nestedTree2 = TreeUtil.nestFlatTree(flatTree);
        assertEquals(nestedTree2, nestedTree.get(0));
    }

    @Test
    public void nestBrokenTreeTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            Node flattenedTree = TreeUtil.nestFlatTree(brokenFlatTree);
        });
    }

    @Test
    public void reversedTransformTest() {
        List<Node> flat = TreeUtil.flattenNestedTree(customTree.get(0));
        Node nested = TreeUtil.nestFlatTree(flat);
        assertEquals(nested, customTree.get(0));

        List<Node> flat2 = TreeUtil.flattenNestedTree(nestedTree.get(0));
        Node nested2 = TreeUtil.nestFlatTree(flat2);
        assertEquals(nested2, nestedTree.get(0));
    }
}
