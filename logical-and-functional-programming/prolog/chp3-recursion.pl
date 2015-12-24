% 3.2 http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse11
directlyIn(natasha, irina).
directlyIn(olga, natasha).
directlyIn(katarina, olga).

in(X, Y) :- directlyIn(X, Y).

in(X, Y) :-
    directlyIn(X, Z),
    in(Z, Y).


% 3.3
directTrain(saarbruecken, dudweiler).
   directTrain(forbach, saarbruecken).
   directTrain(freyming, forbach).
   directTrain(stAvold, freyming).
   directTrain(fahlquemont, stAvold).
   directTrain(metz, fahlquemont).
   directTrain(nancy, metz).

travelFromTo(X, Y) :- directTrain(X, Y).

travelFromTo(X, Y) :-
   directTrain(X, Z),
   travelFromTo(Z, Y).

% 3.4
greater_than(X, 0).
greater_than(succ(X), succ(Y)) :-
	greater_than(X, Y).

% 3.5
leaf(Label).

tree(B1, B2) :-
   leaf(B1),
   leaf(B2).

swap(0, T).
swap(B1, T) :-
   tree(Z, B1),
   swap(Z, T).
