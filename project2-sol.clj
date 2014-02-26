(use 'clojure.core.logic)

(defn parento [p c]
  (conde
    [(== p :antonio) (== c :vito)]
    [(== p :vito) (== c :michael)]
    [(== p :vito) (== c :sonny)]
    [(== p :vito) (== c :fredo)]
    [(== p :michael) (== c :mary)]
    [(== p :michael) (== c :anthony)]))
    
(defn grandparento [gp gc]
  (fresh [p]
    (parento gp p)
    (parento p gc)))

(defn ancestoro [a b]
    (fresh [x]
      (conde
        [(parento a b)]
        [(parento a x) (ancestoro x b)])))


