package ZAM
import scala.collection.mutable.HashMap

/**
 * Un etat du programme
 * Il comporte le pointeur de code, la pile, l'accumulateur, l'environnement, les variables globales et l'extra arg
 */
class State (pc : Int, sp : Array[Value], accu : Value, env : Array[Value], glob : HashMap[Int, Value], extra_args : Int){

  def getpc = pc
  def getsp = sp
  def getaccu = accu
  def getenv = env
  def getglob = glob
  def getextra = extra_args

}