package week7_LazyEvaluation

// 容器个数及容量
class Pouring(capacity: Vector[Int]) {

  // State
  type State = Vector[Int]
  // 初始状态
  val initialState = capacity map (x => 0)

  // Moves
  // 定义三种move动作，每种动作都可以改变状态，实现change()方法
  trait Move {
    def change(state: State): State
  }
  // 清空其中一个容器
  case class Empty(glass: Int) extends Move {
    def change(state: State) = state updated (glass, 0)
  }
  // 填满一个容器
  case class Fill(glass: Int) extends Move {
    def change(state: State) = state updated (glass, capacity(glass))
  }
  // 从一个容器倒入另一个容器
  case class Pour(fromGlass: Int, toGlass: Int) extends Move {
    def change(state: State) = {
      // 考虑当前from里有的容量，以及能倒入to里的容量，取最小值
      val amount = state(fromGlass) min (capacity(toGlass) - state(toGlass))
      // 同时更新from, to的容量状态
      state updated (fromGlass, state(fromGlass) - amount) updated (toGlass, state(toGlass) + amount)
    }
  }

  val glasses = 0 until capacity.length

  // 产生当前状态可用的moves动作集合，即Empty ++ Fill ++ Pour的动作集合
  val moves =
    (for (g <- glasses) yield Empty(g)) ++
      (for (g <- glasses) yield Fill(g)) ++
      (for (from <- glasses; to <- glasses; if from != to) yield Pour(from, to))

  // Paths
  // 记录moves动作路径，history为历史动作路径，endState为当前状态 
  class Path(history: List[Move], val endState: State) {
    // 扩展路径，将move加入动作列表中，将当前endState使用move动作
    def extend(move: Move) = new Path(move :: history, move change endState)
    override def toString = (history.reverse mkString " ") + " ==> " + endState + "\n"
  }

  val initialPath = new Path(Nil, initialState)

  // 生成动作路径集合的序列，返回Stream
  def from(paths: Set[Path], explored: Set[State]): Stream[Set[Path]] =
    if (paths.isEmpty) Stream.empty
    else {
      val more = for {
        path <- paths   // 路径列表
        next <- moves map path.extend  // 在path.extend上应用可用的moves动作集合，产生下一个状态
        if !(explored contains next.endState)
      } yield next
      paths #:: from(more, explored ++ more.map(_.endState))
    }

  // 从初始路径集合中出发的Stream序列
  val pathSets = from(Set(initialPath), Set(initialState))

  // 寻找解，返回路径序列
  def solutions(target: Int): Stream[Path] =
    for {
      pathSet <- pathSets
      path <- pathSet
      if path.endState contains target
    } yield path

}