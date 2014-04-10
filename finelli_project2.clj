;; Mario Finelli
;; CMSC 421 - Section 0201
;; Project 2 (due 10 April 2014)

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
  (fresh [q]
    (parento q gc)
    (parento gp q)))

(defn ancestoro
  "A relation where a is the ancestor of b."
  [a b]
  (fresh [q]
    (conde
      [(parento a b)]
      [(parento a q) (parento q b)])))

(defn family-data
  "Return a map of information about person.
  The keys are :ancestors, :descendants, :grandparents, :grandchildren.
  The values are all collections."
  [person]
  {:ancestors (vec (run* [q] (ancestoro q person))),
   :descendants (vec (run* [q] (ancestoro person q))),
   :grandparents (vec (run* [q] (grandparento q person))),
   :grandchildren (vec (run* [q] (grandparento person q)))})

(defn rpslso
  "A relation where p1 beats p2 in rock-paper-scissors-lizard-spock.
  This can be done with (conde), but (matche) may save you some typing:
  https://github.com/frenchy64/Logic-Starter/wiki#wiki-matche"
  [p1 p2]
  (conde
    [(== p1 :scissors) (== p2 :paper)]  ;; scissors cuts paper
    [(== p1 :paper) (== p2 :rock)]      ;; paper covers rock
    [(== p1 :rock) (== p2 :lizard)]     ;; rock crushes lizard
    [(== p1 :lizard) (== p2 :spock)]    ;; lizard poisons spock
    [(== p1 :spock) (== p2 :scissors)]  ;; spock smashes scissors
    [(== p1 :scissors) (== p2 :lizard)] ;; scissors decapitates lizard
    [(== p1 :lizard) (== p2 :paper)]    ;; lizard eats paper
    [(== p1 :paper) (== p2 :spock)]     ;; paper disproves spock
    [(== p1 :spock) (== p2 :rock)]      ;; spock vaporizes rock
                                        ;; and as it always has...
    [(== p1 :rock) (== p2 :scissors)])) ;; rock crushes scissors

(defn sub-triangle
  "Return a collection of vectors of length 4, each a chain of winning moves
  that start and end with gesture. e.g. [:spock :scissors :paper :spock]."
  [gesture]
  (run* [q p r s]
      (== q gesture) ;; here we unify q with gesture so that it's included in
                     ;; our final list
      (== q s) ;; then we unify s with gesture (q) so that the last item in the
               ;; list is also the right gesture
      (rpslso q p)
      (rpslso p r)
      (rpslso r s)))

(def symbols
  "Legal sudoku symbols."
  [:a :b :c :d])

(defn sudokuo
  "A relation where a1...d4 is a valid mini-sudoku board."
  [a4 b4 c4 d4
   a3 b3 c3 d3
   a2 b2 c2 d2
   a1 b1 c1 d1]
  (all
     (permuteo [a4 b4 c4 d4] symbols) ;; one in every row (4: topmost)
     (permuteo [a3 b3 c3 d3] symbols) ;; one in every row (3: middle top)
     (permuteo [a2 b2 c2 d2] symbols) ;; one in every row (2: middle bottom)
     (permuteo [a1 b1 c1 d1] symbols) ;; one in every row (1: bottommost)
     (distincto [a4 a3 a2 a1])        ;; one in every column (a: far left)
     (distincto [b4 b3 b2 b1])        ;; one in every column (b: center left)
     (distincto [c4 c3 c2 c1])        ;; one in every column (c: center right)
     (distincto [d4 d3 d2 d1])        ;; one in every column (d: far right)
     (distincto [a4 b4 a3 b3])        ;; one in every sub-board (top left)
     (distincto [c4 d4 c3 d3])        ;; one in every sub-board (top right)
     (distincto [a2 b2 a1 b1])        ;; one in every sub-board (bottom left)
     (distincto [c2 d2 c1 d1])))      ;; one in every sub-board (bottom right)

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

