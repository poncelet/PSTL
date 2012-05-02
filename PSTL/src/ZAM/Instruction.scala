package ZAM
import java.util.ArrayList

/**
 * Implantation des instructions (interpret)
 * arg permet de factoriser les ACC(n) et autres instructions de ce type
 * Pour les instructions ayant besoin de valeur, elles sont passées en argument
 */
abstract class Instruction () {
  
  override def toString = print(this)
  
  def print(t : Instruction) : String = t match {
	  case Acc(arg) => "acc " + arg + "\n"
	  case Push() => "push\n"
	  case Pushacc(arg) => "pushacc " + arg + "\n"
	  case Pop(arg) => "pop " + arg + "\n"
	  case Assign(arg) => "assign " + arg + "\n"
	  
	  case Envacc(arg) => "envacc " + arg + "\n"
	  case Pushenvacc(arg) => "pushenvacc " + arg + "\n"
	  
	  case Push_Retaddr(pc) => "push_retaddr " + pc + "\n"
	  case Apply(arg) => "apply " + arg + "\n"
	  case Appterm(nargs, slotSize) => "appterm " + nargs + ", " + slotSize + "\n"
	  case Return(sp) => "return " + sp + "\n" 
	  case Restart() => "restart \n"
	  case Grab(required) => "grab " + required + "\n"
	  case Closure(nvars) => "closure " + nvars + "\n"
	  case Closurerec(nfuncs, nvars) => "closurerec " + nfuncs + ", " + nvars + "\n"
	  case Pushoffsetclosure(arg) => "pushoffsetclosure " + arg + "\n"
	  case Offsetclosure(arg) => "offsetclosure " + arg + "\n"
	  case Pushoffsetclosurem(arg) => "pushoffsetclosurem " + arg + "\n"
	  case Offsetclosurem(arg) => "offsetclosurem " + arg + "\n"
	    
	  case Pushgetglobal(arg) => "pushgetglobal " + arg + "\n"
	  case Getglobal(arg) => "getglobal " + arg + "\n"
	  case Getglobalfield(arg, field) => "getglobalfield " + arg + " : " + field + " \n"
	  case Setglobal(arg) => "setglobal " + arg + "\n"
	  case Pushgetglobalfield(arg, field) => "pushgetglobalfield " + arg + " : " + field + " \n"
	    
	  case Getfield(arg) => "getfield " + arg + "\n"
	  case Getfloatfield(arg) => "getfloatfield " + arg + "\n"
	  case Setfield(arg) => "setfield " + arg + "\n"
	  case Setfloatfield(arg) => "setfloatfield " + arg + "\n"
	  case Pushatom(arg) => "pushatom " + arg + "\n"
	  case Atom(arg) => "atom " + arg + "\n"
	  case Makeblock(size, typ) => "makeblock :size " + size + " :type: " + BlockT.apply(typ).toString() + "\n"
	  case Makefloatblock(size) => "makefloatblock :size " + size + "\n"
	  
	  case Vectlength() => "vectlength \n"
	  case Getvectitem() => "vectlength \n"
	  case Setvectitem() => "vectlength \n"
	    
	  case Branch(flag) => "branch " + flag + "\n"
	  case Branchif(flag) => "branchif " + flag + "\n"
	  case Branchifnot(flag) => "branchifnot " + flag + "\n"
	  case Switch(sizes,flags) => {var str = "switch " + sizes + " : "
		  							flags.foreach(ent => str += ent + " ")
		  							str += "\n"
		  							str
	  								}
	  case Boolnot() => "boolnot \n"
	    
	  case C_Call(narg) => "c_call " + narg + "\n" 
	  
	  case Const(arg) => "const " + arg + "\n"
	  case Pushconst(arg) => "pushconst " + arg + "\n"
	  case Pushconstint(arg) => "pushconstint (" + arg + ")\n"
	  case Constint(arg) => "constint (" + arg + ")\n"
	  
	  case Negint() => "negint \n"
	  case Addint() => "addint \n"
	  case Subint() => "subint \n"
	  case Mulint() => "mulint \n"
	  case Divint() => "divint \n"
	  case Modint() => "modint \n"

	  case Andint() => "andint \n"
	  case Orint() => "orint \n"
	  case Xorint() => "xorint \n"
	  case Lslint() => "lslint \n"
	  case Lsrint() => "lsrint \n"
	  case Asrint() => "asrint \n"

	  case Integer_comparision(tst) => "integer_comparision " + tst + "\n"

	  case Integer_branch_comparision(arg : Long, pc : Int, tst: String) => "integer_branch_comparision " + arg + " : " + pc + tst + "\n"

	  case Offsetint(arg) => "offsetint " + arg + "\n"
	  case Offsetref(arg) => "offsetref " + arg + "\n"
	  case Isint() => "isint \n"
	    
	  case _ => "Instruction inconnue"
	}
}

/**
 * Les instructions possibles de la ZAM
 */

case class Acc(arg : Int) extends Instruction
case class Push extends Instruction
case class Pushacc(arg : Int) extends Instruction
case class Pop(arg : Int = 0) extends Instruction
case class Assign(arg : Int) extends Instruction

case class Envacc(arg : Int) extends Instruction
case class Pushenvacc(arg : Int) extends Instruction

case class Push_Retaddr(pc : Int) extends Instruction
case class Apply(arg : Int) extends Instruction //check_stacks
case class Appterm(nargs : Int, slotSize : Int) extends Instruction
case class Return(sp : Int) extends Instruction
case class Restart extends Instruction
case class Grab(required : Int) extends Instruction
case class Closure(nvars : Int) extends Instruction
case class Closurerec(nfuncs : Int, nvars : Int) extends Instruction
case class Pushoffsetclosure(arg : Int) extends Instruction
case class Offsetclosure(arg : Int) extends Instruction
case class Pushoffsetclosurem(arg : Int) extends Instruction
case class Offsetclosurem(arg : Int) extends Instruction

case class Pushgetglobal(arg : Int) extends Instruction
case class Getglobal(arg : Int) extends Instruction
case class Getglobalfield(arg : Int, field : Int) extends Instruction
case class Setglobal(arg : Int) extends Instruction
case class Pushgetglobalfield(arg: Int, field : Int) extends Instruction

case class Getfield(arg : Int) extends Instruction
case class Getfloatfield(arg : Int) extends Instruction
case class Setfield(arg : Int) extends Instruction
case class Setfloatfield(arg : Int) extends Instruction
case class Pushatom(arg : Int) extends Instruction
case class Atom(arg : Int) extends Instruction
case class Makeblock(size : Int, typ : Int) extends Instruction
case class Makefloatblock(size : Int) extends Instruction

case class Vectlength extends Instruction
case class Getvectitem extends Instruction
case class Setvectitem extends Instruction

case class Branch(flag : Int) extends Instruction
case class Branchif(flag : Int) extends Instruction
case class Branchifnot(flag : Int) extends Instruction
case class Switch(sizes : Int, flags : Array[Int]) extends Instruction
case class Boolnot extends Instruction

case class C_Call(narg : Int) extends Instruction 

case class Const(arg : Int) extends Instruction
case class Pushconst(arg : Int) extends Instruction
case class Pushconstint(arg : Int) extends Instruction
case class Constint(arg : Int) extends Instruction

case class Negint() extends Instruction
case class Addint() extends Instruction
case class Subint() extends Instruction
case class Mulint() extends Instruction
case class Divint() extends Instruction
case class Modint() extends Instruction

case class Andint() extends Instruction
case class Orint() extends Instruction
case class Xorint() extends Instruction
case class Lslint() extends Instruction
case class Lsrint() extends Instruction
case class Asrint() extends Instruction

case class Integer_comparision(tst : String) extends Instruction

case class Integer_branch_comparision(arg : Long, pc : Int, tst: String) extends Instruction

case class Offsetint(arg : Int) extends Instruction
case class Offsetref(arg : Int) extends Instruction
case class Isint() extends Instruction