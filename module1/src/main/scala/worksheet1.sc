import sun.jvm.hotspot.HelloWorld
// This worksheet is going to be used to demonstrate all the
// features of scala. Consider it scala101 express.

//variable declaration. Immutable variables declared with val
// all variable declarations have type defined after the variable
// name. i.e.
// [val | var] <variable_name> : <variable_type> = <variable_value>

val text : String = "this is an immutable string"

//below doesn't compile!
//text = "new value"

// Here is an example of var definitions.

var changeMe : String = "this variable can be changed"

//this works
changeMe = "new value"

//Scala has automatic type inference. So you don't need to
// necessarily supply a type. In intellij the ALT + = key shortcut
// on the variable name shows what type scala has inferred.

val newText = "Look I didn't supply a type!"

//Function definitions are done by using def
// requires parameter list with type.
// Return type is declared after parameter list.
// no need for explicit return. the last expression is the
// return value in scala.
def sayHello (name : String) : String = {
  //This is an interpolated string, similar to printf but more
  // concise. requires 's' before the String starts.
  s"Hello $name"
  //look Ma - no return!
}

// declaring function without type inference.
def sayHello2 (name : String) = {
  s"Hello yet again"
}
//function invocation is simple...
sayHello("fahad")

///functions can have multiple parameter lists as well

def sayHello3 (firstParam1 : String, secondParam1: Int)
              (firstParam2 : String) = {
  s"Hiya $firstParam1. You are $secondParam1 years old!"
  s"We rrruuvv you too $firstParam2"
}

sayHello3("Fahad", 25)("Stranger")

//multiple parameter lists function can be used as a pattern to pass function to the
// second parameter list. In here we define an anonymous function definition which
// returns String.
def paramListDemoWithFunction (name: String)
                              (whoareyou : () => String) : String = {
  //Notice $whoareyou is being invoked here, rather than used as a value.
  s"Look $name! We found more details about you: ${whoareyou()}"

}

//we are going to create an implementation of () => String function
def provideDetails() = {
  s"You Like Ice Cream"
}

// This invocation passes provideDetails function to the paramListDemoWithFunction
paramListDemoWithFunction("Fahad")(provideDetails)

//This invocation uses anonymous function to be passed as a parameter rather than
// an predefined one
// its bit of a strange syntax, for the function definition. Just specify function
// parameter with {} and voila...
paramListDemoWithFunction("Fahad"){ () => "You Like Games too"}


//Scala implicits allow you to define implicit parameters which Scala will resolve
// with any implicit defintions that matches the parameter signature.
// FYI implicit function requires its own parameter list.
//
def sayHelloImplicitly(name: String)(implicit message: String) = {
  s"Hi $name, we have this message for you: $message"
}

// Strangely only vals can replace implicit params. vars don't work!
implicit val hello_val  = "Hello"
// Also, if compiler finds multiple values that can match the signature, it gives error
//Uncomment below and compile to see what happens
//implicit val hello_val2 = "Hiya!"

//No need to specify the implicit second parameter list.
sayHelloImplicitly("Fahad")

def paramListDemoImplicitly(name : String)(implicit whoareyou: () => String)={
  s"We know a lot about you $name, even if you don't provide details! \n" +
    s"For instance we know that ${whoareyou()}"

}

//This implicit function matches the above functions signature
implicit def fahadLikes() = {
  "You Like Pizza!"
}

//So we don't even need to specify the second parameter list!
paramListDemoImplicitly("Fahad")

// Scala classes
// Parameter lists define class member variables.
// Prefixing the parameter list with val or var defines getter/setters
// val -> getter only (as it is immutable)
// var -> getter/setter
// by default all parameter list values are public.
class HelloWorld ( val name: String, var message: String){

  //private method not visible
  private def randomMethod(localName: String)(localMessage: String): String ={
    s"Hello $name $message"
  }

  // Class body is the primary constructor.
  // Note introducted to parameter list for randomMethod Function
  val greeting = randomMethod(name)(message)

  println(greeting)

}

val first = new HelloWorld("Fahad", "World!")
// can change the message since its a var
first.message = "Something else!"
// can't for name as its a val. uncomment to see the error
// first.name = "compiler error"

// rechecking first.greeting, but looks like it still has the old value. This is because
// greeting was already assigned when the class got created!
first.greeting

val second = new HelloWorld("Fahad", "Sheikh")


//Scala case classes

//Prefix class definition with the 'case' statement and Scala provides these
//functionalities out of the box:
// - implements equality, hashcode and toString methods
// - case classes are serializable
// - don't require 'new' keyword
// - can be used in pattern matching.
// - all args are prefixed with val

case class Person (fname : String, lastName: String)

// Doesn't require new keyword to instantiate an instance
val a = Person("Fahad", "Sheikh")
val b = Person("Fahad","Sheikh")


// Default classes don't have equals defined.
first.equals(second)
//Default classes also don't have toString defined.
first.toString()

// Compared to case classes, they have toString implemented
a.toString()

// And also equals/hashCode defined.
a.equals(b)
a.hashCode()
b.hashCode()

//Pattern matching example
class Person2 (fname: String, lname: String){
  def fullName() = {s"$fname-$lname"}
}

//extend Person2 class to implement Student class
//Note how the Student class defines its own parameter list first
// and then while extending from Person2 it passes the value of fname,lname from
// Student!
case class Student (fname:String, lname:String, id:Int) extends Person2(fname,lname)


//Using generics here. After function definition square brackets define generics
// Here Upperbound is Person2 followed by parameter list.
def patternMatchExample [T <: Person2](matchMe : T) ={
  matchMe match {
      // for matching, either specify all the parameter list from classname
      // and then you can use to extract all the fields here.
    case Student(fname,lname,id) => s"$fname-$lname-$id"
      //Or create a val (p) of the Type to match Person2 here, and then call functions
      // on it.
    case p: Person2 => p.fullName()
  }
}

patternMatchExample(new Student("Fahad", "Sheikh", 23))

patternMatchExample(new Person2("Fahad", "Sheikh"))