% Prolog 20

max_list([], 0).
max_list([Head], Head) :- !.
max_list([Head | Tail], Max) :-
    max_list(Tail, Max1),
    Max is max(Head, Max1).
  
 
el(X,[X|L],L).
el(X,[_|L],R) :- el(X,L,R).

   
n_power(_, 0, 1) :- !.
n_power(N, Power, Result) :-
	Power1 is Power - 1,
	n_power(N, Power1, Result1),
	Result is Result1*N.

	
subset([], []).
subset([H|Tail], [H|Result]) :-
	subset(Tail, Result).
subset([_|Tail], Result) :-
   subset(Tail, Result).

  
generate_list(N, N, [N]) :- !.
generate_list(K, N, [K|R]) :-
    K1 is K+1
    generate_list(K1, N, R);
perm([], []).
perm([E|L], Result) :-
    perm(L, Result1),
    perm_ins(E, Result, Result1).	
perm_ins(E, [E|X], X).
perm_ins(E, [A|X], [A|Y]) :-
	perm_ins(E, X, Y).
allperm(L, Result) :-
	findall(X, perm(L, X), Result).

p(N, Result) :-
    generate_list(1, N, R1),
    allperm(R1, Result).


	
comb(0, _, []).
comb(N, [H|L], [H|R]) :-
	N > 0,
	N1 is N - 1,
	comb(N1, L, R).
comb(N, [_|L], R) :- 
    N > 0,
    comb(N, L, R).
aranj(N, L, R) :-
    comb(N, L, R1),
    perm(R1, R).

perm_diff3_abs(A, B) :-
	Diff is abs(A - B),
	Diff =< 3.
perm_diff3_filter([_]) :- !.
perm_diff3_filter([H1,H2| L]) :-
	perm_diff3_abs(H1, H2),
	perm_diff3_filter([H2|L]).
perm_diff3_correct([], []) :- !.
perm_diff3_correct([H | T], Result) :-
    perm_diff3_filter(H),
    perm_diff3_correct(T, ResultNext),
    Result = [H | ResultNext].
perm_diff3_correct([H | T], Result) :-
	perm_diff3_correct(T, Result).
perm_diff3(L, Result) :-
    allperm(L, AllPerms),
    perm_diff3_correct(AllPerms, Result), !.   
    
    

pairsaux(A, [B|_], [A, B]).
pairsaux(A, [_|L], X) :-
    pairsaux(A, L, X).

	
remove([], _, []) :- !.
remove([Elem | Tail], Del, Result) :-
   Elem = Del,
   remove(Tail, Del, Result).
remove([Elem | Tail], Del, Result) :-
   Result = [Elem | Rest],
   remove(Tail, Del, Rest).
   
	
sum_to_n(N, Result) :-
    sum_to_n(N, Result, N).
sum_to_n(_, 0, 0) :- !.
sum_to_n(N, Result, I) :-
	I1 is I - 1,
	sum_to_n(N, Result1, I1),
	Result is I + Result1.

max_positions(List, Result) :-
	max_list(List, Max),
	max_positions(List, Result, Max, 1).
	
max_positions([], [], _, _) :-  !.
max_positions([Head | Tail], Result, Max, Pos) :- % found element
	Head =:= Max,
	Pos1 is Pos + 1,
	max_positions(Tail, Result1, Max, Pos1), 
	Result = [Pos | Result1], !.
	
max_positions([Head | Tail], Result, Max, Pos) :- % element not found
    Pos1 is Pos + 1,
    max_positions(Tail, Result, Max, Pos1), !.


double_el([], []).
double_el([H | L], [H, H | Result]) :-
	double_el(L, Result).    
	
	
f([],[]).
 
f(L,R):-
faux(L,0,1,1,R).
faux([],_,_,_,[]).
faux([_|T],Index,PosToDel,PosC,R):-
	PosToDel == PosC,
	NewIndex is (Index+1),
	NewPosToDel is (PosToDel + NewIndex),
	NewPosC is (PosC+1),
	faux(T,NewIndex,NewPosToDel,NewPosC,R), !.
faux([H|T],Index,PosToDel,PosC,[H|R]):-
	NewPosC is (PosC+1),
	faux(T,Index,PosToDel,NewPosC,R).
