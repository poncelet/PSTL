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
    case Push() => thread.stack.push(thread.getaccu)
    case Pushacc(arg) => thread.stack.push(thread.getaccu); if (arg > 0) thread.setaccu(thread.stack.get(arg))
    case Pop(arg) => for(i<-0 to arg-1) thread.stack.pop
    case Assign(arg) => thread.stack.update(arg,thread.getaccu);thread.setaccu(new Zamint(0))
    
    case Envacc(arg) => thread.setaccu(thread.getenv(arg))
	case Pushenvacc(arg) => thread.stack.push(thread.getaccu); thread.setaccu(thread.getenv(arg))
	
	case Push_Retaddr(pc) => {	thread.stack.push(new Zamint(thread.getextra))
								val OldEnv = new Array[Value](thread.sizeEnv)
								for(i <-0 to thread.sizeEnv-1) OldEnv.update(i, thread.getenv(i))
								thread.stack.push(new Zamblock(BlockT.normal_t, thread.sizeEnv, OldEnv))
								thread.stack.push(new Zamint(thread.getpc + pc))}
	case Apply(arg) => {	if( (arg > 0) && (arg < 4)) {
								val args = new Array[Value](arg)
								for(i<-1 to arg)  args(i-1) = thread.stack.get(i-1)
								
								thread.stack.push(new Zamint(thread.getextra)) //sp[3 + (arg-1)]
								val OldEnv = new Array[Value](thread.sizeEnv)
								for(i <-0 to thread.sizeEnv) OldEnv.update(i, thread.getenv(i))
								thread.stack.push(new Zamblock(BlockT.normal_t, thread.sizeEnv, OldEnv)) //sp[2 (arg-1)]
								thread.stack.push(new Zamint(thread.getpc)) //sp[1 + (arg-1)]
								for(i<-arg to 1) thread.stack.push(args(i-1))
							}
							thread.setextra(arg - 1)
							val accublock = thread.getaccu.asInstanceOf[Zamblock]
							thread.setpc(accublock.at(0).asInstanceOf[Zamint].getval) //code val ...
							thread.clearenv
							accublock.getval.foreach(ent => (thread.setenv(thread.sizeEnv, ent)))
						}//check_stack
	case Appterm(nargs, slotSize) => {
	  val sp = thread.stack.getSp
	  val newsp = thread.stack.getSp + slotSize - nargs
	  for(i<-nargs-1 to 0) thread.stack.update(i+newsp, thread.stack.get(i))
	  for(i<-0 to newsp-1) thread.stack.pop
	  thread.setextra(nargs - 1)
	  val accublock = thread.getaccu.asInstanceOf[Zamblock]
	  thread.setpc(accublock.at(0).asInstanceOf[Zamint].getval)
	  thread.clearenv
	  accublock.getval.foreach(ent => (thread.setenv(thread.sizeEnv, ent)))
	  	}
	case Return(sp) => {
	  for(i<-0 to sp-1) thread.stack.pop
	  if(thread.getextra > 0){
	    thread.setextra(thread.getextra - 1)
	    val accublock = thread.getaccu.asInstanceOf[Zamblock]
	    thread.setpc(accublock.at(0).asInstanceOf[Zamint].getval)
	    thread.clearenv
	    accublock.getval.foreach(ent => (thread.setenv(thread.sizeEnv, ent)))
	  }
	  else {
	    thread.setpc(thread.stack.getVal.asInstanceOf[Zamint].getval)
	    thread.stack.pop
	    thread.clearenv
	    thread.stack.getVal.asInstanceOf[Zamblock].getval.foreach(ent => (thread.setenv(thread.sizeEnv, ent)))
	    thread.stack.pop
	    thread.setextra(thread.stack.getVal.asInstanceOf[Zamint].getval)
	    thread.stack.pop
	  }
	}
	case Restart() => {
	  val num_args = thread.sizeEnv - 2
	  for(i<-num_args-1 to 0) thread.stack.push(thread.getenv(i+2))
	  val nenv = thread.getenv(1)
	  thread.clearenv
	  nenv.asInstanceOf[Zamblock].getval.foreach(ent => (thread.setenv(thread.sizeEnv, ent)))
	  thread.setextra(thread.getextra + num_args)
	}
	case Grab(required) => {
	  if(thread.getextra >= required) thread.setextra(thread.getextra - required)
	  else {
	    val num_args = 1 + thread.getextra
	    val tab = new Array[Value](num_args + 2)
	    
	    val Env = new Array[Value](thread.sizeEnv)
		for(i <-0 to thread.sizeEnv-1) Env.update(i, thread.getenv(i))
	    tab(1) = new Zamblock(BlockT.normal_t, thread.sizeEnv, Env)
	    
	    for(i<-0 to num_args-1) {tab(i+2) = thread.stack.getVal; thread.stack.pop}
	    tab(0) = new Zamint(thread.getpc - 3)
	    val closure = new Zamblock(BlockT.closure_t, num_args + 2, tab)
	    thread.setaccu(closure)
	    
	    thread.setpc(thread.stack.getVal.asInstanceOf[Zamint].getval)
	    thread.stack.pop
	    thread.clearenv
	    thread.stack.getVal.asInstanceOf[Zamblock].getval.foreach(ent => (thread.setenv(thread.sizeEnv, ent)))
	    thread.stack.pop
	    thread.setextra(thread.stack.getVal.asInstanceOf[Zamint].getval)
	    thread.stack.pop
	  }
	}
	case Closure(nvars, pc) => {
	  if(nvars > 0) thread.stack.push(thread.getaccu)
	   val tab = new Array[Value](nvars + 1)
	   tab(0) = new Zamint(thread.getpc + pc)
	   for(i<-0 to nvars-1) {tab(i+1) = thread.stack.getVal; thread.stack.pop}
	    
	   val closure = new Zamblock(BlockT.closure_t, nvars + 1, tab)
	   thread.setaccu(closure)
	}
	case Closurerec(nfuncs, nvars, npc) => {
	  if(nvars > 0) thread.stack.push(thread.getaccu)
	  val tab = new Array[Value](nfuncs * 2 - 1 + nvars)
	  
	  for(i<-0 to nvars-1) {tab((nfuncs * 2) + i) = thread.stack.getVal; thread.stack.pop}
	  tab(0) = new Zamint(thread.getpc + npc(0))
	  
	  val closure = new Zamblock(BlockT.closure_t, nfuncs * 2 - 1 + nvars, tab)
	  thread.setaccu(closure)
	  thread.stack.push(closure)
	  
	  var j = 1
	  for(i<- 1 to nfuncs-1) {
	    tab(j) = new Zamblock(BlockT.infix_t, 1, Array[Value](new Zamint((nfuncs - i)*2 + 1)))
	    j = j + 1
	    tab(j) = new Zamint(thread.getpc + npc(i))
	    thread.stack.push(tab(j))
	    j = j + 1
	  }
	  
	}
	case Pushoffsetclosure(arg) => thread.stack.push(thread.getaccu);thread.setaccu(thread.getenv(arg + thread.offsetrec))
	case Offsetclosure(arg) => thread.setaccu(thread.getenv(arg + thread.offsetrec))
	case Pushoffsetclosurem(arg) => thread.stack.push(thread.getaccu);thread.setaccu(thread.getenv(arg + thread.offsetrec))
	case Offsetclosurem(arg) => thread.setaccu(thread.getenv(-arg + thread.offsetrec))
	
    case Pushgetglobal(arg) => thread.stack.push(thread.getaccu); thread.setaccu(env.atglob(arg))
	case Getglobal(arg) => thread.setaccu(env.atglob(arg))
	case Getglobalfield(arg, field) => thread.stack.push(thread.getaccu); thread.setaccu(env.atglob(arg))
									thread.setaccu(thread.getaccu.asInstanceOf[Zamblock].at(field))
	case Setglobal(arg) => env.addglob(arg, thread.getaccu); thread.setaccu(new Zamint(0))
	case Pushgetglobalfield(arg, field) => thread.setaccu(env.atglob(arg)); thread.setaccu(thread.getaccu.asInstanceOf[Zamblock].at(field))
	
	
	case Getfield(arg) => thread.setaccu(thread.getaccu.asInstanceOf[Zamblock].at(arg))
	case Getfloatfield(arg) => thread.setaccu(thread.getaccu.asInstanceOf[Zamblock].at(arg)) //double_t clone ?
	case Setfield(arg) => thread.getaccu.asInstanceOf[Zamblock].set(arg, thread.stack.getVal)
	case Setfloatfield(arg) => thread.getaccu.asInstanceOf[Zamblock].set(arg, thread.stack.getVal); thread.setaccu(new Zamint(0)) //double_t
	case Pushatom(arg) => thread.stack.push(thread.getaccu); thread.setaccu(env.atom(arg))//accu = atom(arg)
	case Atom(arg) => thread.setaccu(env.atom(arg))//accu = Tag ? // Transform. int > Value = blockT.getValue(arg)
	case Makeblock(size, typ) => { 
	  //size = arg, tag = champ
	  //alloc_small(cible, size, tag)
	  var block : Zamblock = null
	  if(size <= MAX_YOUNG_WOSIZE) {
		  block = new Zamblock(BlockT.apply(typ), size, new Array[Value](size))
		  block.set(0, thread.getaccu)
		  for(i<-1 to size-1) {block.set(i, thread.stack.getVal);thread.stack.pop}
	  }
	  else {
	      block = new Zamblock(BlockT.apply(typ), size, new Array[Value](size))
		  block.set(0, thread.getaccu)
		  for(i<-1 to size-1) {block.set(i, thread.stack.getVal);thread.stack.pop}
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
	  for(i <- 1 to size-1) {block.set(i, thread.stack.getVal);thread.stack.pop} //Double_t
	  thread.setaccu(block)
	}
	
	case Vectlength() => {var mlsize = thread.getaccu.asInstanceOf[Zamblock].getsize
	  						if(thread.getaccu.asInstanceOf[Zamblock].gettag == BlockT.doublearray_t) mlsize = mlsize / DOUBLE_WOSIZE
	  						thread.setaccu(new Zamlong(mlsize))}
	case Getvectitem() => {thread.setaccu(thread.getaccu.asInstanceOf[Zamblock].getval(
							thread.stack.getVal.asInstanceOf[Zamint].getval))
							thread.stack.pop}
	case Setvectitem() => {thread.getaccu.asInstanceOf[Zamblock].set(
							thread.stack.getVal.asInstanceOf[Zamint].getval,
							thread.stack.get(1))
							thread.stack.pop;thread.stack.pop}
	
	case Getstringchar() => {thread.setaccu(thread.getaccu.asInstanceOf[Zamblock].getval(
										thread.stack.getVal.asInstanceOf[Zamint].getval))
							thread.stack.pop}
	case Setstringchar() => {thread.getaccu.asInstanceOf[Zamblock].set(
							thread.stack.getVal.asInstanceOf[Zamint].getval,
							thread.stack.get(1))
							thread.stack.pop;thread.stack.pop}
	
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
	
	case Pushtrap(trappc) => {
	  thread.stack.push(new Zamint(thread.getextra))
	  val Env = new Array[Value](thread.sizeEnv)
	  for(i <-0 to thread.sizeEnv-1) Env.update(i, thread.getenv(i))
	  thread.stack.push(new Zamblock(BlockT.normal_t, thread.sizeEnv, Env))
	  thread.stack.push(new Zamint(thread.getTrapsp))
	  thread.setTrapsp(thread.stack.getSp)
	  thread.stack.push(new Zamint(thread.getpc + trappc))
	}
	case Poptrap() => {
	  thread.stack.pop
	  thread.setTrapsp(thread.stack.getVal.asInstanceOf[Zamint].getval)
	  thread.stack.pop
	  thread.stack.pop
	  thread.stack.pop
	} 
	case Raise() => {
	  if(thread.getTrapsp == 0) throw new ErreurZam("Il n'y a pas d'exception cree, fin du programme")
	  thread.stack.changeSp(thread.getTrapsp)
	  thread.setpc(thread.stack.getVal.asInstanceOf[Zamint].getval)
	  thread.stack.pop
	  thread.setTrapsp(thread.stack.getVal.asInstanceOf[Zamint].getval)
	  thread.stack.pop
	  thread.stack.getVal.asInstanceOf[Zamblock].getval.foreach(ent => (thread.setenv(thread.sizeEnv, ent)))
	  thread.stack.pop
	  thread.setextra(thread.stack.getVal.asInstanceOf[Zamint].getval)
	  thread.stack.pop
	}
	
	case C_Call(narg, itprim) => { //arg
	  //sauv
	val pc = thread.getpc
	val Env = new Array[Value](thread.sizeEnv)
	for(i <-0 to thread.sizeEnv-1) Env.update(i, thread.getenv(i))
	val sp = thread.stack.getSp
	
	//lancement primitive
	env.runtime.run(itprim, narg, thread)
	
	//restaure
	thread.setpc(pc+1)
	thread.clearenv
	Env.foreach(ent => (thread.setenv(thread.sizeEnv, ent)))
	for(i<-0 to narg-1) thread.stack.pop
	}
    
    case Const(arg) => thread.setaccu(new Zamint(arg))
	case Pushconst(arg) => thread.stack.push(thread.getaccu); thread.setaccu(new Zamint(arg))
	case Pushconstint(arg) => thread.stack.push(thread.getaccu); thread.setaccu(new Zamint(arg))
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
						thread.stack.pop}
	case Subint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						-	thread.stack.getVal.asInstanceOf[Zamint].getval)) //+1 ?
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						-	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.pop}
	case Mulint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						*	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						*	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.pop}
	case Divint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) { //test div == 0? (sp[0] != 0)
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						/	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						/	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.pop}
	case Modint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) { //test div == 0? (sp[0] != 0)
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						%	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						%	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.pop}

	case Andint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						&	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						&	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.pop}
	case Orint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint(thread.getaccu.asInstanceOf[Zamint].getval
							    						|	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						|	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.pop}
	case Xorint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint( thread.getaccu.asInstanceOf[Zamint].getval
							    						^	thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong( thread.getaccu.asInstanceOf[Zamlong].getval
							    						^	thread.stack.getVal.asInstanceOf[Zamlong].getval ))
						}
						thread.stack.pop}
	case Lslint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint( thread.getaccu.asInstanceOf[Zamint].getval
							    						<< thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						<< 	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.pop}
	case Lsrint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint( thread.getaccu.asInstanceOf[Zamint].getval
							    						>> thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						>> 	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.pop}
	case Asrint() => {if(thread.getaccu.getClass().toString().equals("class ZAM.Zamint")) {
							thread.setaccu(new Zamint( thread.getaccu.asInstanceOf[Zamint].getval
							    						>> thread.stack.getVal.asInstanceOf[Zamint].getval))
						}
						else {
							thread.setaccu(new Zamlong(thread.getaccu.asInstanceOf[Zamlong].getval
							    						>> 	thread.stack.getVal.asInstanceOf[Zamlong].getval))
						}
						thread.stack.pop}

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
	  	thread.stack.pop}

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
	    
    case _=> throw new ErreurZam("Erreur, Instruction inconnue \n")
    }
    //mise à jour du pc
    thread.setpc(thread.getpc + 1)
  }

}