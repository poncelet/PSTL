package ZAM
import scala.Array
import scala.collection.mutable.ArrayBuffer
/**
 * Classe AST
 * Elle détient la suite d'instruction pointée par les pcs
 */
class AST (tree : ArrayBuffer[Instruction]){

override def toString = { var str = "AST : \n"
						for(i<-0 to tree.size-1) str += tree(i).toString
						str
						}
/**
 * Ajouter une instruction (de type instruction, les valeurs associées sont données en arguments)
 */
def add(instr : Instruction) = tree += instr

/**
 * Retourne l'instruction pointée par ce pc
 */
def get(pc : Int) : Instruction = tree(pc)

def size  : Int = tree.size

}