package documentation

/**
  *
  */
// note CTRL + Shift + N if you want to find something quickly.
object DigraphDocumentation {

     /**
       * done [[edu.princeton.cs.algs4.Digraph]]
       * [[edu.princeton.cs.algs4.SymbolGraph]]
       *
       * done [[edu.princeton.cs.algs4.DirectedDFS]]
       *
       * done [[edu.princeton.cs.algs4.BreadthFirstDirectedPaths]]
       * done [[edu.princeton.cs.algs4.DepthFirstDirectedPaths]]
       *
       * done [[edu.princeton.cs.algs4.DirectedCycle]]
       * done [[edu.princeton.cs.algs4.Topological]]
       *
       * done-[[edu.princeton.cs.algs4.KosarajuSharirSCC]]
       *
       * done-[[edu.princeton.cs.algs4.TransitiveClosure]]
       *
       *
       * note data files
       *
       * tinyDAG
       * tinyDG
       * tinyDG_delimiter_mine.txt
       * tinyDAG_delimiter_mine.txt
       * jobs.txt
       * two_prereq_delimiter_mine.txt
       */

}

object DigraphTestingIdeas {

     /**
       *
       * note todo help for all of them, how to test the mechanics of the algorithm itself?
       *
       *
       * done [[edu.princeton.cs.algs4.Digraph]]:
       * done-testing Digraph: (0,2),(0,1),(8,0),(1,4),(5,6),(8,3),(2,3),(3,5), (5,3) ===> 0:1,2, 1:4, 2:3, 3:5, 4:..
       * .8:0,3
       * done-testing Digraph reverse 6:0,1,2, 8:0,3,2, 9:0,2,1 ==> 0:6,8,9,  1:6,9,  2:6,8,9,  3:8
       *
       * done [[edu.princeton.cs.algs4.DirectedDFS]]:
       * note dfs() marks all vertices connected to a source.
       * done-testing in the given tinyDG.txt digraph, testing that src=2, result is 0 1 2 3 4 5
       * done-testing that if src =1, then result = 1
       *
       *
       * done [[edu.princeton.cs.algs4.DepthFirstOrder]] (done while doing topological tests)
       * done-testing preorder: vertex is on queue before recursive calls
       * done-testing postorder: vertex is on queue after recursive calls
       * done-testing reverse postorder: vertex is on stack after recursive calls
       *
       *
       * done-[[edu.princeton.cs.algs4.DepthFirstDirectedPaths]]
       * // note: main idea: pathTo() function hops along the edgeTo[] array so pathTo(5) => 5-0-2
       * // and puts these on a stack so that when printed, they print: 2-0-5
       * // done-testing of adj array: 2:0,3, 0:5,1, 5:4, 4:3,2, 3:5,2, 1:_
       * // done-testing different result
       *       // dfs from 2->3: 2-0-5-4-3
       *       // bfs from 2->3: 2->3 (chose shortest path)
       * // done-testing same result
       *       // dfs from 2->1: 2-0-1
       *       // bfs from 2->1: 2-0-1
       *
       *
       * done- [[edu.princeton.cs.algs4.BreadthFirstDirectedPaths]]
       * // note now that edgeTo[] contains all the information, we can hop from index to another.
       * // done- testing: to find path from 12 -> 6: edgeTo[12] = 11, edgeTo[11] = 9, edgeTo[9] = 6 so path is: 6->9->11->12
       * // note distTo[] just contains distance to the index from 6. So distTo[4] is number of edges passed to get
       * // from 6 to 4. Not used in finding the pathTo(), just used in this printing.
       *
       *
       * done [[edu.princeton.cs.algs4.Topological]]
       * testing-done reverse postorder in DAG (directed acyclic graph) is a topological sort.
       * testing-done 1 is still visited before 2 even if we write 6/1/2 or 6/2/1.
       * (probably because (1) is higher up the graph than (2) is) ( --> two prongs file)
       *
       *
       *
       * done-[[edu.princeton.cs.algs4.KosarajuSharirSCC]]
       * FOR TINYDG.txt (middle)
       * done-testing test that id = [1,0,1,1,1,1,3,4,3,2,2,2,2] after tinyDG
       * done-testing that areStronglyConnected vertices 3 and 4 is true while 8 and 9 is false even though 8 -> 9
       * done-testing stronglyCOnnected components list is
       * done-testing count of components is 5
       * done-testing count is between 1 and num vertices
       * FOR TINYDAG.txt (non connected)
       * done-testing that areStronglyConnected any (all combos) is false
       * done-testing that stronglyConnected list has single elements
       * done-testing count of components is V
       * done-testing count is between 1 and V
       * FOR (all connected)
       * done-testing that areStronglyConnected is true for al
       * done-testing strConn, count, countbetween...
       * FOR edge cases kosaruju1v and 2 v txts
       * help-testing that for kosaruju1v.txt that count == 1, strongConn = 1, count between, id = [1]
       * help-testing for kosaruju2v.txt that count == 1, strongConn = [2,3]...
       *
       */
}
