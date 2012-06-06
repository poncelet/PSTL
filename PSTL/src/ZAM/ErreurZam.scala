package ZAM

class ErreurZam(err : String, env : GlobalState) extends Exception(err) {
  env.addErrStream(err)
}