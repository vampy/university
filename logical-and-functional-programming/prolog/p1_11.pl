% Butum Daniel - G921
%
% el = number
% list = el*
%
% 11. a. Test whether a list is a set.
%
% is_set(L:list)
%   flow model: (i) or (o)
%   L = the list that we want to test
%

member(Elem, [Elem | _]) :- !.
member(Elem, [_ | Tail]) :-
    member(Elem, Tail).

% is_set(List) :-
%    length(List, Length),
%    sort(List, Sorted), % removes duplicates
%    length(Sorted, Length).

is_set([]).
is_set([Head | Tail]) :-
	not(member(Head, Tail)),
	is_set(Tail).

% 11. b. Remove the first three occurences of an element in a list.
%        If the element occurs less than three times, all occurences will be removed.
%
% remove_3(L: list, D: number, R: list)
%       flow model: (i, i, i) or (i, i, o)
%       L = the input list
%       D = the number we want to remove 3 times
%       R = the result list
%
% remove_3(L: list, D: number, R: list, N: number)
%       flow model: (i, i, i, i) or (i, i, o, i)
%       L = the input list
%       D = the number we want to remove 3 times
%       R = the result list
%       N = the number of succesfully deleted numbers (the first call should be 0)
% Model:
%   True                                    if L and R are empty
%   remove_3(L2, L3, ... Ln, D, R, N + 1)   if L1 = D and N != 3
%   remove_3(L2, L3, ... Ln, D, [L1, R], N) otherwise

remove_3(List, Del, Result) :-
	remove_3(List, Del, Result, 0).

remove_3([], _, [], _).
remove_3([Elem | Tail], Del, Result, N) :-
	(
	   Elem == Del, N \= 3
	   ->  N1 is N + 1, remove_3(Tail, Del, Result, N1);

	       Result = [Elem | Rest],
           remove_3(Tail, Del, Rest, N)
	).
