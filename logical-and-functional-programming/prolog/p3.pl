% 15. Given a list of integer numbers.
%     Remove from list the longest sequence formed from prime numbers.
%     Ex: [1, 3, 5, 4, 2, 5, 7, 8] => [1, 3, 5, 4, 8]

prime_is_divisible(Number, Divisor) :-
    Number mod Divisor =:= 0.
prime_is_divisible(Number, Divisor) :-
	NextDivisor is Divisor + 2,            % increment by two, we work only with odd numbers
	Divisor * Divisor < Number,            % L < sqrt(N)
	prime_is_divisible(Number, NextDivisor).

is_prime(2) :- !.
is_prime(3) :- !.
is_prime(Number) :-
	Number > 3,
	Number mod 2 =\= 0,   % take out even numbers
	not(prime_is_divisible(Number, 3)).


% base case
largest_p(List, Start, End) :-
    largest_p(List, 0, 0, 0, 0, Start, End).
largest_p([], _, _, _, _, -1, -1) :- !.
largest_p([Head], 0, 0, _, _, -1, -1) :- not(is_prime(Head)), !. % no sequence found
largest_p([_], 0, 0, I, _, Start, End) :-
    Start is I,
    End is I + 1, !. % no sequence found
largest_p([_], _, PreviousLength, _, StartI, Start, End) :-
    Start is StartI,
    End is PreviousLength + StartI + 1, !.
largest_p([Head, NextHead | Tail], Length, PreviousLength, I, _, Start, End) :-
    is_prime(Head),
    is_prime(NextHead), % condition % a[i+1] > a[i]
    NextLength is Length + 1,

    NextLength > PreviousLength,  % new length is bigger than old one
    largest_p([NextHead | Tail], NextLength, NextLength, I + 1, I + 1 - NextLength, Start, End), !.  % previous_len = len
largest_p([Head, NextHead | Tail], _, PreviousLength, I, StartI, Start, End) :- % if HEAD is  not prime, ignore it, move forward
    not(is_prime(Head)),
    largest_p([NextHead | Tail], 0, PreviousLength, I + 1, StartI, Start, End), !. % previous_len = len
largest_p([_, NextHead | Tail], Length, PreviousLength, I, StartI, Start, End) :- % else, maybe we can find a better length
    largest_p([NextHead | Tail], Length + 1, PreviousLength, I + 1, StartI, Start, End), !.


remove_p(List, Result) :-
	largest_p(List, Start, End),
	remove_p(List, Start, End, 0, Result).
remove_p([], _, _, _, []) :- !.
remove_p([_ | Tail], Start, End, I, Result) :- % element is in range, do not include
    I >= Start, I < End,
    remove_p(Tail, Start, End, I + 1, Result).
remove_p([Elem | Tail], Start, End, I, Result) :- % include element
    Result = [Elem | Rest],
    remove_p(Tail, Start, End, I + 1, Rest), !.
