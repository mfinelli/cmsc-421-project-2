(use 'clojure.core.logic)

(defn parento
  "A relation where p is the parent of c."
  [p c]
  (conde
    [(== p :antonio) (== c :vito)]
    [(== p :vito) (== c :michael)]
    [(== p :vito) (== c :sonny)]
    [(== p :vito) (== c :fredo)]
    [(== p :michael) (== c :mary)]
    [(== p :michael) (== c :anthony)]))
    
(defn grandparento
  "A relation where gp is the grandparent of gc."
  [gp gc]
  (run* [q]
    (parento q gc) (parento gp q)))

(defn ancestoro
  "A relation where a is the ancestor of b."
  [a b]
  nil)

(defn family-data
  "Return a map of information about person.
  The keys are :ancestors, :descendants, :grandparents, :grandchildren.
  The values are all collections."
  [person]
  {:ancestors [], :descendants [], :grandparents [], :grandchildren []})

(defn rpslso
  "A relation where p1 beats p2 in rock-paper-scissors-lizard-spock.
  This can be done with (conde), but (matche) may save you some typing:
  https://github.com/frenchy64/Logic-Starter/wiki#wiki-matche"
  [p1 p2]
  nil)

(defn sub-triangle
  "Return a collection of vectors of length 4, each a chain of winning moves
  that start and end with gesture. e.g. [:spock :scissors :paper :spock]."
  [gesture]
  nil)

(def symbols 
  "Legal sudoku symbols."
  [:a :b :c :d])

(defn sudokuo
  "A relation where a1...d4 is a valid mini-sudoku board."
  [a4 b4 c4 d4
   a3 b3 c3 d3
   a2 b2 c2 d2
   a1 b1 c1 d1]
  nil)

(defn print-sudoku
  "Print n solutions to mini-sudoku. (Use to test your sudokuo relation.)"
  [n]
  (doall
    (map println
         (run n [q]
           (fresh [a4 b4 c4 d4 a3 b3 c3 d3 a2 b2 c2 d2 a1 b1 c1 d1]
             (sudokuo a4 b4 c4 d4
                      a3 b3 c3 d3
                      a2 b2 c2 d2
                      a1 b1 c1 d1)
             (== q [[a4 b4 c4 d4]
                    [a3 b3 c3 d3]
                    [a2 b2 c2 d2]
                    [a1 b1 c1 d1]])))))
  nil)

