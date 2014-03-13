A quick introduction to logic programming in Clojure
====================================================

> I think things, and they happen! Fear me, lesser creatures...
> 
> --Niftu Cal (Mass Effect 2) 

core.logic
----------

First, if you didn't do this already, place the following in your ~/.lein/profiles.clj

    {:user {:dependencies [[org.clojure/core.logic "0.8.7"]]}}

To run the examples below, start up a repl, and execute

    (use 'clojure.core.logic)
    
### (run) and (run*)

Clojure's core.logic is an implementation of miniKanren which is less of a library and more of an embedded sub-language. There are 3 core logical operators, plus the functions (run*) and (run) which act as a way to interface with miniKanren from Clojure. For example:

    (run 2 [q]
      logical-clauses...)
      
is a request for 2 instantiations of the logic variable (or "lvar") q that satisfies (i.e. makes true) all the logical clauses that follow.

    (run* [q]
      logical-clauses...)
      
is a request for *all* of the instantiations of q that satisfy the logical clauses that follow. Exercise caution; if there are an infinite number of ways to satisfy the logical clauses, then (run*) may not terminate.

So what goes in logical clauses? Logical operators!

### Core logical operators ###

(==) (pronounced "unify") can be thought of as declaration of equality.

    user=> (run* [q]
             (== q 1))
    (1)

Unify is symmetric; (== q 1) and (== 1 q) are equivalent.

Multiple clauses are wrapped in an implicit logical conjunction (i.e. "and").

    user=> (run* [q]
              (== q 1)
              (== q 2))
    ()
              
It's impossible to unify a value with both 1 and 2 which is why we get back the empty list. (core.logic's way of saying, "It is impossible to instantiate q such that the following statements are all true.")

(fresh) introduces new logic variables in a manner similar to a (let) statement:

    user=> (run* [q]
             (fresh [x y]
               (== x 1)
               (== y x)
               (== q [x y]))
    ([1 1])

The (fresh \[x y\]) essentially says "x and y are logic variables in the clauses that follow", and the clauses that follow are declarations to unify x with 1, y with x, and q with a list containing x and y. (core.logic lets you unify lvars with collections.)

Finally, (conde) is a declaration of logical disjunction.

    user=> (run* [q]
             (conde
               [(== q 1)]
               [(== q 2)]))
    (1 2)
    
The above expression is roughly equivalent to "Give me all values of q such that q equals 1 OR q equals 2". Obviously, the only such values are 1 and 2.

    user=> (run* [q]
             (== q q))
    (_0)
    
What happened here? Well, we declared that q must equal q. But *any* value of q satisfies this, so q never had to be instantiated. Think of the underscore as "anything".

There's a reason the underscore is indexed. Consider:

    user=> (run* [q]
             (fresh [x y]
               (== q [x y])))
    ([_0 _1])

versus
    
    user=> (run* [q]
             (fresh [x y]
                (== x y)
                (== q [x y])))
    ([_0 _0])

Building new logical relations
------------------------------
    
Using the core logical operators, we can build new logical relations. For example:

    (defn rpso
      "A relation where p1 beats p2 in a game of rock-paper-scissors."
      [p1 p2]
      (conde
        [(== p1 :rock) (== p2 :scissors)]
        [(== p1 :scissors) (== p2 :paper)]
        [(== p1 :paper) (== p2 :rock)]))

(By convention, relations end in the letter "o".)

Through (run) and (run*) we can ask questions about the relation.

Does rock beat scissors?

    user=> (run* [q] (rpso :rock :scissors))
    (_0)

Yep.
    
Does rock beat paper?

    user=> (run* [q] (rpso :rock :paper))
    ()
    
Nope.

Frodo, Sam and Gandalf play a single game of rock-paper-scissors. Gandalf beats Sam and Gandalf also beats Frodo. How could this have happened?

    user=> (run* [q]
             (fresh [frodo sam gandalf]
               (rpso gandalf sam)
               (rpso gandalf frodo)
               (== q {"Gandalf" gandalf, "Frodo" frodo, "Sam" sam})))
               
    ({"Gandalf" :rock, "Frodo" :scissors, "Sam" :scissors}
    {"Gandalf" :scissors, "Frodo" :paper, "Sam" :paper}
    {"Gandalf" :paper, "Frodo" :rock, "Sam" :rock})

And so on...

The crux of the matter is this: we're not telling core.logic *how* to solve the problems we give it, we're just telling core.logic *what is true* in terms of logical statements. core.logic conducts the appropriate inference without need for further instruction.

Hence the name "logic programming".
    
Other useful relations in core.logic
------------------------------------

(membero x l) A relation where l is a collection, such that l contains x.

(appendo x y z) A relation where x, y, and z are proper collections, such that z is x appended to y.

(permuteo xl yl) A relation that will permute xl into the yl.

(firsto l a) A relation where l is a collection, such that a is the first of l.

(resto l d) A relation where l is a collection, such that d is the rest of l.

(!= u v) Disequality constraint. Ensures that u and v will never unify.

(distincto l) A relation which guarantees no element of l will unify with another element of l.

(rembero x l o) A relation between l and o where x is removed from l exactly one time.

(all) Like fresh but does does not create logic variables.

Example: The natural numbers
----------------------------

**This is provided as an example of what can be done with core.logic. You may skip this section.**

However, if you [grok](https://en.wikipedia.org/wiki/Grok) everything in this section, you will find project 2 to be laughably easy.

The [Peano axioms](https://en.wikipedia.org/wiki/Peano_axioms#The_axioms) are a set of axioms for the natural numbers.

A number is either:

* zero
* the successor of a number

We'll let zero be represented as :zero and the successor of a number n be represented as \[n\] (a vector containing n). Then, in core.logic:

    (defn nato [n]             ;; call the relation "nato"
      (conde
        [(== n :zero)]         ;; :zero is a number
        [(fresh [n-1]         
                (== n [n-1])   ;; n is the successor of n-1
                (nato n-1))])) ;; n-1 is a number

"Give me 5 numbers." (You *really* don't want to use run* on this.)

    user=> (run 5 [q] (nato q))
    (:zero [:zero] [[:zero]] [[[:zero]]] [[[[:zero]]]])

We can also define plus axiomatically:

    0 + n = n
    
    n1 + n2 = n3
    ------------
    [n1] + n2 = [n3]
    
In English: "zero + n = n unconditionally" and "If n1 + n2 = n3, then (successor of n1) + n2 = (successor of n3)"

    (defn pluso [n1 n2 n3]
      (conde
        [(== n1 :zero) (== n2 n3)]
        [(fresh [n1-1 n3-1]
                (== n1 [n1-1])
                (== n3 [n3-1])
                (pluso n1-1 n2 n3-1))]))
                
What's 2 + 2?

    user=> (run* [q] (pluso [[:zero]] [[:zero]] q))
    ([[[[:zero]]]])
    
4 (obviously).
    
"1 + x = y; y + z = 4; x = 0; what are acceptable values for x, y and z?"

    user=> (run 1 [q]
             (fresh [x y z]
               (pluso [:zero] x y)
               (pluso y z [[[[:zero]]]])
               (== x :zero)
               (== q {:x x :y y :z z})))
               
    ({:x :zero, :y [:zero], :z [[[:zero]]]})

**Challenge problem**: define mulo (multiplication).

(You will **not** be graded on this. But if you bring me a working definition, I promise to be suitably impressed.)
    
Prolog (review)
---------------

**You can skip this section if you are already familiar with Prolog.**

Pure Prolog (i.e. horn clauses) allows for three types of clauses. Intuitively, a **fact** says "this is always true". Example facts (these could be entered in a file, say, example.pl, then loaded into the interactive interpreter with "consult(example)."):

    parent(antonio, vito).
    parent(vito, michael).
    parent(vito, sonny).
    parent(vito, fredo).
    parent(michael, mary).
    parent(michael, anthony).
    
Note that to add facts in an *interactive* Prolog session, you have to wrap them with assert(). Example:

    ?- assert(parent(vito, michael)).
    
A **rule** is of the form: "u :- p, q, ..., t." Intuitively, "u is true if p, q, ..., and t are all true". Example:

    grandparent(X, Y) :- parent(X, Z), parent(Z, Y).
    
Again, to interactively add rules, wrap with assert:

    ?- assert((grandparent(X, Y) :- parent(X, Z), parent(Z, Y))).
    
Prolog considers anything beginning with a capital letter to be a variable. You can think of the above rule as shorthand for

    ∀X, Y, Z (grandparent(X, Y) <- parent(X, Z) ∧ parent(Z, Y)

Finally, there is the **query**. Intuitively, "make this true". Example at an interactive session:

    ?- grandparent(X, Y).
    X = vito,
    Y = mary ;
    X = vito,
    Y = anthony .
    
(A semicolon in Prolog denotes disjunction.)

Note that rules can be *recursive*. If we wanted a generic "ancestor" rule:

    ancestor(A, B) :- parent(A, B).
    ancestor(A, B) :- parent(A, X), ancestor(X, B).

Intuitively, "If A is the parent of B, then A is the ancestor of B. Also, if A is the parent of someone (X) who is the ancestor of B, then A is also an ancestor of B."
    
Recreating the Prolog Example
-----------------------------

We can define the parento relation with conde:

    (defn parento [p c]
      (conde
        [(== p :antonio) (== c :vito)]
        [(== p :vito) (== c :michael)]
        [(== p :vito) (== c :sonny)]
        [(== p :vito) (== c :fredo)]
        [(== p :michael) (== c :mary)]
        [(== p :michael) (== c :anthony)]))
 
Defining the grandparent and ancestor relations is left as an exercise in project 2.

Another Prolog to Minikanren example
------------------------------------

In Prolog:

    r(a).
    q(a).
    p(X) :- q(X), r(X).
    
    ?- p(X).
    X = a.
    
In core.logic

    
    (defn q [x]
      (== x :a))
    
    (defn r [x]
      (== x :a))
    
    (defn p [x] 
      (all 
        (q x)
        (r x)))
    
    (run* [x]
      (p x))
    ;; => (:a)
    
    