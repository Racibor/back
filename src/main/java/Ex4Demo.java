import tree.Node;
import tree.TreeUtil;
import tree.parser.SimpleTreeParser;
import tree.parser.TreeParser;

import java.util.List;

public class Ex4Demo {
    static TreeParser parser = new SimpleTreeParser();
    static List<Node> flatTree = parser.parse("""
                    [
                    { id: 1, parentId: null },
                    { id: 2, parentId: 1 },
                    { id: 3, parentId: 1 },
                    { id: 4, parentId: 3 } ]
                """);

    public static void ex4() {

        System.out.println("\n-------Zad 4----------\n");
        System.out.println("drzewo wejściowe: ");
        TreeUtil.printTree(flatTree);
        System.out.println("drzewo wyjściowe: ");
        TreeUtil.printTree(TreeUtil.nestFlatTree(flatTree));


    }
}
