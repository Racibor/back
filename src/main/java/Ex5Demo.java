import tree.Node;
import tree.TreeUtil;
import tree.parser.SimpleTreeParser;
import tree.parser.TreeParser;

import java.util.List;

public class Ex5Demo {
    static TreeParser parser = new SimpleTreeParser();
    static List<Node> nestedTree = parser.parse("""
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

    public static void ex5() {
        System.out.println("\n-------Zad 5----------\n");
        System.out.println("drzewo wejściowe: ");
        TreeUtil.printTree(nestedTree.get(0));
        System.out.println("drzewo wyjściowe: ");
        TreeUtil.printTree(TreeUtil.flattenNestedTree(nestedTree.get(0)));
    }
}
