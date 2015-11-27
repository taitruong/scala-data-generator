/**
  * Created by YSE on 26.11.2015.
  */
package bi.generator

object Tools {
  def EnsureRange( x : Int, start : Int, end : Int ) : Int = {
    x match{
      case x if x < start => start
      case x if x > end => end
      case _ => x
    }
  }
}
