package week7_LazyEvaluation

object Example2_WaterPouringProblem {
  
  // Testing it works
  val problem = new Pouring(Vector(4, 9, 19))     //> problem  : week7_LazyEvaluation.Pouring = week7_LazyEvaluation.Pouring@1cfea
                                                  //| f9c
  problem.moves                                   //> res0: scala.collection.immutable.IndexedSeq[Product with Serializable with w
                                                  //| eek7_LazyEvaluation.Example2_WaterPouringProblem.problem.Move] = Vector(Empt
                                                  //| y(0), Empty(1), Empty(2), Fill(0), Fill(1), Fill(2), Pour(0,1), Pour(0,2), P
                                                  //| our(1,0), Pour(1,2), Pour(2,0), Pour(2,1))
  
  
  problem.solutions(5)                            //> res1: Stream[week7_LazyEvaluation.Example2_WaterPouringProblem.problem.Path]
                                                  //|  = Stream(Fill(1) Pour(1,0) ==> Vector(4, 5, 0)
                                                  //| , ?)
  
}