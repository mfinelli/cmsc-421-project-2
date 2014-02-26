parent(antonio, vito).
parent(vito, michael).
parent(vito, sonny).
parent(vito, fredo).
parent(michael, mary).
parent(michael, anthony).

grandparent(X, Y) :- parent(X, Z), parent(Z, Y).

ancestor(A, B) :- parent(A, B).
ancestor(A, B) :- parent(A, X), ancestor(X, B).
