% Find the largest consecutive sequence of increasing numbers
% base case
largest(List, Start, End) :-
    largest(List, 0, 0, 0, Start, End).

largest([], _, _, _, 0, 0) :- !.
largest([_], _, _, _, _, _) :- !.
largest([Head, NextHead | Tail], Length, PreviousLength, I, Start, End) :-
    NextHead > Head, % condition % a[i+1] > a[i]
    NextLength is Length + 1,

    NextLength > PreviousLength,                    % new length is bigger than old one
    largest([NextHead | Tail], NextLength, NextLength, I + 1, Start, End),  % previous_len = len

    % finish program, return
    Start is I + 1 - NextLength,
    End is NextLength + 1 + Start, !.

largest([Head, NextHead | Tail], _, PreviousLength, I, Start, End) :- % else, negate first, start over
    NextHead < Head,
    largest([NextHead | Tail], 0, PreviousLength, I + 1, Start, End), !. % previous_len = len

largest([_, NextHead | Tail], Length, PreviousLength, I, Start, End) :- % else, maybe we can find a better length
    largest([NextHead | Tail], Length + 1, PreviousLength, I + 1, Start, End), !.

  polin([H],X,S1,S2):- S2=S1*X+H,!.
  polin([H|T],X,S1,S2):- S3=S1*X+H,polin(T,X,S3,S2).
  polinom(L,X,S):-polin(L,X,0,S).

remove([], _, []) :- !.
remove([Elem | Tail], Del, Result) :-
   Elem = Del,
   remove(Tail, Del, Result).
remove([Elem | Tail], Del, Result) :-
   Result = [Elem | Rest],
   remove(Tail, Del, Rest).

add([], Elem, [Elem]).
add([Head | Tail], Elem, [Head | TailNext]) :-
    add(Tail, Elem, TailNext).

member(Elem, [Elem | _]) :- !.
member(Elem, [_ | Tail]) :-
    member(Elem, Tail).

append_lists([], L, L).
append_lists([H | T], L, [H | R]) :-
    append_lists(T, L, R).

pow(_, 0, 1).
pow(X, Y, Z) :-
    Y1 is Y - 1,
    pow(X, Y1, Z1),
    Z is Z1 * X.


% 14.
%  a. Define a predicate to remove from a list all recurring (repetitive)
%     elements. (ex: l=[1,2,1,4,1,3,4] => l=[2,3])
%  b. Remove all occurrence for a maximum value from a list on integer numbers.
max_list([], 0).
max_list([Head], Head) :- !.
max_list([Head | Tail], Max) :-
    max_list(Tail, MaxNext),
    Max is max(Head, MaxNext).
remove_max_list(List, Result) :-
    max_list(List, Max),
    remove(List, Max, Result), !.

remove_repetitive(List, Result) :-
    remove_repetitive(List, Result, []).

remove_repetitive([], [], _) :- !.
remove_repetitive([Head | Tail], Result, Seen) :- % we got an element
    member(Head, Seen),
    remove_repetitive(Tail, Result, Seen). % without head
remove_repetitive([Head | Tail], Result, Seen) :- % not seen element
    Result = [Head | ResultNext],
    add(Seen, Head, SeenNext),
    remove_repetitive(Tail, ResultNext, SeenNext), !.

my_last(X,[X]).
my_last(X,[_|L]) :- my_last(X,L).

reverse([],[]).
reverse([H|T], Rev) :- reverse(T, Trev), append(Trev, [H], Rev).
