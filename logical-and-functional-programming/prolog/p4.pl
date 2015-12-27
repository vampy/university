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


% sieve of eratostene, luata de pe net
sieve(N, [2|PS]) :-       % PS is list of odd primes up to N
    retractall(mult(_)),
    sieve_O(3,N,PS).
sieve_O(I,N,PS) :-        % sieve odds from I up to N to get PS
    I =< N, !, I1 is I+2,
    (   mult(I) -> sieve_O(I1,N,PS)
    ;   (   I =< N / I ->
            ISq is I*I, DI  is 2*I, add_mults(DI,ISq,N)
        ;   true
        ),
        PS = [I|T],
        sieve_O(I1,N,T)
    ).
sieve_O(I,N,[]) :- I > N.
add_mults(DI,I,N) :-
    I =< N, !,
    ( mult(I) -> true ; assert(mult(I)) ),
    I1 is I+DI,
    add_mults(DI,I1,N).
add_mults(_,I,N) :- I > N.


% 5. Determine all the decompositions of a given N, as a sum of distinct
%   prime numbers.
subsets([], []).
subsets([H|T], [H|Res]) :- subsets(T, Res).
subsets([_|T], Res) :- subsets(T, Res).
% flow(i,o)
% L: initial set Res: a list containing all subsets of the initial set
allSubsets(L, Res) :- findall(Y, subsets(L, Y), Res).

% the second one is the result
sum_primes_correct([], [], _).
sum_primes_correct([H|T], Result, N) :- % list of lists, H is a list
    sum_list(H, Sum),
    Sum =:= N,
    sum_primes_correct(T, ResultNext, N),
    Result = [H | ResultNext].
sum_primes_correct([H|T], Result, N) :-
    sum_list(H, Sum),
    Sum =\= N,
    sum_primes_correct(T, Result, N).

sum_primes(N, Result) :-
    sieve(N - 1, Primes),
    allSubsets(Primes, Subsets),
    sum_primes_correct(Subsets, Result, N).

goldbach(4,[2, 2]) :- !.
goldbach(Number, L) :-
    Number > 4,
    goldbach(Number, L, 3).
goldbach(N, [P, Q], P) :-
    Q is N - P,
    is_prime(Q), !.
goldbach(N, L, P) :-
    P < N,
    next_prime(P, P1),
    goldbach(N, L, P1).

next_prime(P, P1) :-
    P1 is P + 1,
    is_prime(P1), !.
next_prime(P, P1) :-
    P2 is P + 1,
    next_prime(P2, P1).
