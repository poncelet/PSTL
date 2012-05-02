package ZAM
import scala.collection.mutable.ArrayBuffer

/**
 * Classe Evaluator
 * Elle est chargée d'évaluer une instruction sur un environnement
 **/
class Evaluator {

  val MAX_YOUNG_WOSIZE = 256
  val DOUBLE_WOSIZE = 1 //double size / value size
  
  /**
 * Avance la thread t d'un pas en appliquant l'instruction qu'elle pointe
 * inst : L'instruction evaluée
 * Env : L'environnement courant
 * t :Int Le numéro de la thread qui s'exécute
 */
 def exec(inst : Instruction, env : GlobalState, itT : Int) = {
   
    //sauvegarde de l'etat
    var thread = env.getthread(itT)
    
    inst match {
    
    case Acc(arg) => thread.setaccu(thread.stack.get(arg))
    case Push() => thread.stack.addVal(thread.getaccu)
    case Pushacc(arg) => thread.stack.addVal(thread.getaccu); if (arg > 0) thread.setaccu(thread.stack.get(arg))
    case Pop(arg) => for(i<-0 to arg) thread.stack.push
    case Assign(arg) => thread.stack.update(arg,thread.getaccu)
    
    case Envacc(arg) => thread.setaccu(thread.getenv(arg))
	case Pushenvacc(arg) => thread.stack.addVal(thread.getaccu); thread.setaccu(thread.getenv(arg))
	
	case Push_Retaddr(pc) => {	thread.stack.addVal(new Zamint(thread.getpc + pc))
								thread.stack.addVal(new Zamblock(BlockT.double_t, 4, Array[Value](thread.getenv(0), thread.getenv(1), thread.getenv(2), thread.getenv(3))))
								thread.stack.addVal(new Zamlong(thread.getextra))}
	case Apply(arg) => {	if( (arg > 0) && (arg < 4)) {
								val args = new Array[Value](arg)
								for(i<-1 to arg) {
									args(i-1) = thread.stack.getVal
									thread.stack.push
								}
								for(i<-0 to arg-1) {
									thread.stack.addVal(args(i))
								}	
								thread.stack.addVal(new Zamint(thread.getpc))
								thread.stack.addVal(new Zamblock(BlockT.double_t, 4, Array[Value](thread.getenv(0), thread.getenv(1), thread.getenv(2), thread.getenv(3))))
								thread.stack.addVal(new Zamlong(thread.getextra))
							}
							thread.setextra(arg - 1)
							thread.setpc(thread.getaccu.asInstanceOf[Zamint].getval) //code val ...
							for(i<-0 to 3) thread.setenv(i, thread.getaccu.asInstanceOf[Zamblock].at(i))
						}//check_stack
	case Appterm(nargs, slotSize) => {
	  val newsp = thread.stack.getSp + slotSize - nargs
	  thread.stack.changeSp(newsp)
	  thread.setextra(nargs - 1)
	  thread.setpc(thread.getaccu.asInstanceOf[Zamint].getval) //code val ...
	  for(i<-0 to 3) thread.setenv(i, thread.getaccu.asInstanceOf[Zamblock].at(i))

	  	}
	case Return(sp) => {
	  thread.stack.changeSp(thread.stack.getSp + sp)
	  if(thread.getextra > 0){
	    thread.setextra(thread.getextra - 1)
	    thread.setpc(thread.getaccu.asInstanceOf[Zamint].getval)
	    for(i<-0 to 3) thread.setenv(i, thread.getaccu.asInstanceOf[Zamblock].at(i))
	  }
	  else {
	    thread.setpc(thread.stack.getVal.asInstanceOf[Zamint].getval)
	    thread.stack.push
	    for(i<-0 to 3) thread.setenv(i, thread.stack.getVal.asInstanceOf[Zamblock].at(i))
	    thread.stack.push
	    thread.setextra(thread.stack.getVal.asInstanceOf[Zamint].getval)
	    thread.stack.push
	  }
	}
	case Restart() => {
	  val num_args = thread.env.size - 2
	  for(i<-0 to num_args) thread.stack.addVal(thread.getenv(i+2))
	  val nenv = thread.getenv(1)
	  for(i<-0 to 3) thread.setenv(i, nenv.asInstanceOf[Zamblock].at(i))
	   thread.setextra(thread.getextra + num_args)
	}
	case Grab(required) => {
	  if(thread.getextra >= required) thread.setextra(thread.getextra - required)
	  else {
	    val num_args = 1 + thread.getextra
	    val tab = new Array[Value](num_args + 2)
	    tab(1) = new Zamblock(BlockT.double_t, 4, Array[Value](thread.getenv(0), thread.getenv(1), thread.getenv(2), thread.getenv(3)))
	    for(i<-0 to num_args) {tab(i+2) = thread.stack.getVal; thread.stack.push}
	    val closure = new Zamblock(BlockT.closure_t, num_args + 2, tab)
	    thread.setaccu(closure) //(code_Val(pc -3)
	    thread.setpc(thread.stack.getVal.asInstanceOf[Zamint].getval)
	    thread.stack.push
	    for(i<-0 to 3) thread.setenv(i, thread.stack.getVal.asInstanceOf[Zamblock].at(i))
	    thread.stack.push
	    thread.setextra(thread.stack.getVal.asInstanceOf[Zamint].getval)
	    thread.stack.push
	  }
	}
	case Closure(nvars) => {
	  if(nvars > 0) thread.stack.addVal(thread.getaccu)
	   val tab = new Array[Value](nvars + 1)
	   tab(1) = new Zamblock(BlockT.double_t, 4, Array[Value](thread.getenv(0), thread.getenv(1), thread.getenv(2), thread.getenv(3)))
	   for(i<-0 to num_args) {tab(i+2) = thread.stack.getVal; thread.stack.push}
	   val closure = new Zamblock(BlockT.closure_t, nvars + 1, tab)
	   thread.setaccu(closure)
	}
	case Closurerec(nfuncs, nvars) => "closurerec " + nfuncs + ", " + nvars + "\n"
	case Pushoffsetclosure(arg) => "pushoffsetclosure " + arg + "\n"
	case Offsetclosure(arg) => "offsetclosure " + arg + "\n"
	case Pushoffsetclosurem(arg) => "pushoffsetclosurem " + arg + "\n"
	case Offsetclosurem(arg) => "offsetclosurem " + arg + "\n"
	
    case Pushgetglobal(arg) => thread.stack.addVal(thread.getaccu); thread.setaccu(env.atglob(arg))
	case Getglobal(arg) => thread.setaccu(env.atglob(arg))
	case Getglobalfield(arg, field) => thread.stack.addVal(thread.getaccu); thread.setaccu(env.atglob(arg))
									thread.setaccu(thread.getaccu.asInstanceOf[Zamblock].at(field))
	case Setglobal(arg) => env.addglob(arg, thread.getaccu); thread.setaccu(new Zamint(0))
	case Pushgetglobalfield(arg, field) => thread.setaccu(env.atglob(arg)); thread.setaccu(thread.getaccu.asInstanceOf[Zamblock].at(field))
	
	
	case Getfield(arg) => thread.setaccu(thread.getaccu.asInstanceOf[Zamblock].at(arg))
	case Getfloatfield(arg) => thread.setaccu(thread.getaccu.asInstanceOf[Zamblock].at(arg)) //double_t clone ?
	case Setfield(arg) => thread.getaccu.asInstanceOf[Zamblock].set(arg, thread.stack.getVal)
	case Setfloatfield(arg) => thread.getaccu.asInstanceOf[Zamblock].set(arg, thread.stack.getVal); thread.setaccu(new Zamint(0)) //double_t
	case Pushatom(arg) => thread.stack.addVal(thread.getaccu); thread.setaccu(new Zamint(arg))//accu = atom(arg) autre Hash ? gc.h/mlvalues/ alloc.c > traitement des headers
	case Atom(arg) => thread.setaccu(new Zamint(arg))//accu = Tag ? // Transform. int > Value = blockT.getValue(arg)
	case Makeblock(size, typ) => { 
	  //size = arg, tag = champ
	  //alloc_small(cilbe, size, tag)
	  var block : Zamblock = null
	  if(size <= MAX_YOUNG_WOSIZE) {
		  block = new Zamblock(BlockT.apply(typ), size, new Array[Value](size))
		  block.set(0, thread.getaccu)
		  for(i<-1 to size-1) {block.set(i, thread.stack.getVal);thread.stack.push}
	  }
	  else {
	      block = new Zamblock(BlockT.apply(typ), size, new Array[Value](size))
		  block.set(0, thread.getaccu)
		  for(i<-1 to size-1) {block.set(i, thread.stack.getVal);thread.stack.push}
	  }
	  thread.setaccu(block)
	}
	case Makefloatblock(size) => {
	  // size = arg
	  var block : Zamblock = null
	  if( size <= MAX_YOUNG_WOSIZE / DOUBLE_WOSIZE) {
	    block = new Zamblock(BlockT.doublearray_t, size * DOUBLE_WOSIZE, new Array[Value](size))
	  }
	  else {
	     block = new Zamblock(BlockT.doublearray_t, size * DOUBLE_WOSIZE, new Array[Value](size))
	  }
	  block.set(0, thread.getaccu) //Double_t 
	  for(i <- 1 to size-1) {block.set(i, thread.stack.getVal);thread.stack.push} //Double_t
	  thread.setaccu(block)
	}
	
	case Vectlength() => {var mlsize = thread.getaccu.asInstanceOf[Zamblock].getsize
	  						if(thread.getaccu.asInstanceOf[Zamblock].gettag == BlockT.doublearray_t) mlsize = mlsize / DOUBLE_WOSIZE
	  						thread.setaccu(new Zamlong(mlsize))}
	case Getvectitem() => {thread.setaccu(thread.getaccu.asInstanceOf[Zamblock].getval(
							thread.stack.getVal.asInstanceOf[Zamint].getval))
							thread.stack.push}
	case Setvectitem() => {thread.getaccu.asInstanceOf[Zamblock].set(
							thread.stack.getVal.asInstanceOf[Zamint].getval,
							thread.stack.get(thread.stack.size-2))
							thread.stack.push;thread.stack.push}
	
	case Branch(flag) => thread.setpc(thread.getpc + (flag-1))
	case Branchif(flag) => {if(thread.getaccu.asInstanceOf[Zamint].getval != 0) thread.setpc(thread.getpc + (flag-1))}
	case Branchifnot(flag) => {if(thread.getaccu.asInstanceOf[Zamint].getval == 0) thread.setpc(thread.getpc + (flag-1))}
	case Switch(sizes,flags) => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamblock")) {
									val index = thread.getaccu.asInstanceOf[Zamblock].gettag.id
									thread.setpc(thread.getpc + (flags((sizes & 0xFFFF) + index)) -1)
								}
								else {
								  val index = thread.getaccu.asInstanceOf[Zamint].getval
								  thread.setpc(thread.getpc + (flags(index)-1))
								}}
	case Boolnot() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							if(thread.getaccu.asInstanceOf[Zamint].getval == 0) thread.setaccu(new Zamint(1))
							else thread.setaccu(new Zamint(1))
						}
						else {
							if(thread.getaccu.asInstanceOf[Zamlong].getval == 0) thread.setaccu(new Zamint(1))
							else thread.setaccu(new Zamint(1))
						}} //?
	
	case C_Call(narg) => { //arg Block(custom ?)
	  //sauv
	val pc = thread.getpc
	val env = new Array[Value](4)
	for(i<-0 to 3) env(i) = thread.getenv(i)
	val sp = thread.stack.getSp
	
	//lancement primitive
	//thread.setaccu()
	
	//restaure
	thread.setpc(pc+1)
	for(i<-0 to 3) thread.setenv(i, env(i))
	thread.stack.changeSp(sp) //?
	}
    
    case Const(arg) => thread.setaccu(new Zamint(arg))
	case Pushconst(arg) => thread.stack.addVal(thread.getaccu); thread.setaccu(new Zamint(arg))
	case Pushconstint(arg) => thread.stack.addVal(thread.getaccu); thread.setaccu(new Zamint(arg))
	case Constint(arg) => thread.setaccu(new Zamint(arg))
	
	case Negint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint(~thread.getaccu.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(~thread.getaccu.asInstanceOf[Zamlong].getval))
						}} //?
	case Addint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						+	thread.stack.getVal.asInstanceOf[Zamint].getval)) //-1 ?
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						+	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.push}
	case Subint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						-	thread.stack.getVal.asInstanceOf[Zamint].getval)) //+1 ?
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						-	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.push}
	case Mulint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						*	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						*	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.push}
	case Divint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) { //test div == 0? (sp[0] != 0)
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						/	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						/	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.push}
	case Modint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) { //test div == 0? (sp[0] != 0)
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						%	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						%	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.push}

	case Andint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						&	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						&	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.push}
	case Orint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						|	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						|	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.push}
	case Xorint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint( thread.getaccu.asInstanceOf[Zamint].getval
							    						^	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong( thread.getaccu.asInstanceOf[Zamlong].getval
							    						^	thread.stack.getVal.asInstanceOf[Zamlong].getval ))
						}
						thread.stack.push}
	case Lslint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint( thread.getaccu.asInstanceOf[Zamint].getval
							    						<< thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						<< 	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.push}
	case Lsrint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint( thread.getaccu.asInstanceOf[Zamint].getval
							    						>> thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						>> 	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.push}
	case Asrint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint( thread.getaccu.asInstanceOf[Zamint].getval
							    						>> thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						>> 	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.push}

	case Integer_comparision(tst) => { 
	  var val1 : Long = 0
	  var val2 : Long = 0
	  var res = false
	  val Strclass = thread.getaccu.getClass().toString()
	  if(! Strclass.equals(thread.stack.getVal.getClass().toString()))
	    thread.setaccu(new Zamint(0))
	  else {
	    Strclass match {
	      case "class ZAM.Zamint" => val1 = thread.getaccu.asInstanceOf[Zamint].getval; val2 = thread.stack.getVal.asInstanceOf[Zamint].getval
	      case "class ZAM.Zamlong" => val1 = thread.getaccu.asInstanceOf[Zamlong].getval; val2 = thread.stack.getVal.asInstanceOf[Zamlong].getval
	    }
		  tst match {
		  case "EQ" => res = val1 == val2
		  case "NEQ" => res = val1 != val2
		  case "LTINT" => res = val1 < val2
		  case "LEINT" => res = val1 <= val2
		  case "GTINT" => res = val1 > val2
		  case "GEINT" => res = val1 >= val2
		  case "ULTINT" => res = val1 < val2 // U ?
		  case "UGEINT" => res = val1 >= val2
		  }
		  if(res) thread.setaccu(new Zamint(1))
		  else thread.setaccu(new Zamint(0))
	  	  }
	  	thread.stack.push}

	case Integer_branch_comparision(arg : Long, pc : Int, tst: String) => {
	   var val1 : Long = 0
	   var res = false
	   thread.getaccu.getClass().toString() match {
	   	  case "class ZAM.Zamint" => val1 = thread.getaccu.asInstanceOf[Zamint].getval
	      case "class ZAM.Zamlong" => val1 = thread.getaccu.asInstanceOf[Zamlong].getval
	    }
		  tst match {
		  case "BEQ" => res = arg == val1
		  case "BNEQ" => res = arg != val1
		  case "BLTINT" => res = arg < val1
		  case "BLEINT" => res = arg <= val1
		  case "BGTINT" => res = arg > val1
		  case "BGEINT" => res = arg >= val1
		  case "BULTINT" => res = arg < val1
		  case "BUGEINT" => res = arg >= val1
		  }
		  if(res) thread.setpc(thread.getpc + (pc-1))}

	case Offsetint(arg) => thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval + (arg << 1)))
	case Offsetref(arg) => "offsetref " + arg + "\n"//pointeur ?
	case Isint() => val res = thread.getaccu.getClass().toString.equals("class ZAM.Zamint"); thread.setaccu(new Zamint(res.asInstanceOf[Int]))
	    
    case _=> ()
    }
    //mise à jour du pc
    thread.setpc(thread.getpc + 1)
  }

}