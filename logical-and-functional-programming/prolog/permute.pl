generate_list(N, N, [N]) :- !.
generate_list(K, N, [K|R]) :-
    K1 is K + 1,
    generate_list(K1, N, R).
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
