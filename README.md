Project 2
=========

Read logic-intro.md, then code solutions to the following:

Part I
------

example.pl provides a number of Prolog facts and relations. We've provided a (parento p c) relation in project2.clj which relates parents to their children in a way equivalent to the Prolog facts.

Implement the (grandparento gp gc) relation where gp is the grandparent of gc.

Implement the (ancestoro a b) relation where a is an ancestor of b.

Write a *function* (family-data person) which returns the following map:

    {:ancestors [collection of all ancestors of person],
     :descendants [collection of all descendants of person],
     :grandparents [collection of all grandparents of person],
     :grandchildren [collection of all grandchildren of person]}

This function should use core.logic and the relations you have defined. You will receive no points if you 'hard-code' the solution.

Part II
-------

> Scissors cuts paper
>
> Paper covers rock
>
> Rock crushes lizard
>
> Lizard poisons Spock
>
> Spock smashes scissors
>
> Scissors decapitates lizard
>
> Lizard eats paper
>
> Paper disproves Spock
>
> Spock vaporizes rock
>
> And as it always has...
>
> Rock crushes scissors
> 
> -- [Sheldon Cooper](https://www.youtube.com/watch?v=cSLeBKT7-sM)

Code the relation (rpslso p1 p2) where p1 beats p2 in a game of rock-paper-scissors-lizard-spock. Use keywords :rock, :paper, :scissors, :lizard, :spock to represent the gestures.

Write a *function* (sub-triangle gesture) that returns a collection of all chains of length 3 that start and end with gesture. Each chain should be a sequence of length 4, beginning and ending with gesture, e.g.

    [:spock :scissors :paper :spock]
    
(Spock beats scissors beats paper beats Spock.)

Again, you must use the rpslo relation; no points for 'hard-coding' the solution.

Part III
--------

Mini-sudoku is sudoku played on a 4 by 4 board, instead of a 9 by 9 board. The goal is to fill all 16 squares:

    a4 b4 | c4 d4
    a3 b3 | c3 d3
    -------------
    a2 b2 | c2 d2
    a1 b1 | c1 d1

Such that :a :b :c :d appear exactly once in every row, column and 2 by 2 sub-board.

Code the relation:

    (sudokuo a4 b4 c4 d4
             a3 b3 c3 d3
             a2 b2 c2 d2
             a1 b1 c1 d1)
              
Such that a1...d4 is a valid mini-sudoku board.

**Note: for performance reasons, do not use the (membero) relation.**

Hint: logic-intro.md has a list of useful relations included with core.logic

Submission
----------

Name your file lastname\_project2.clj and push it (and *only* it) to submit server (submit.cs.umd.edu)