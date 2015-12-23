% 5. Determine the value of a polynomial in a point. The polynomial is given as the 
%   list of its coefficients. 

% list starts from 0 as being the first coefficient
polynomial(X, List, Result) :-
    polynomial(X, List, Result, 0).

% If list of coefficients is empty, then we will return 0 as the result
polynomial(_, [], 0, _) :- !.
polynomial(X, [Head | Tail], Result, Power) :-
    NextPower is Power + 1,                        % next power
    polynomial(X, Tail, NextResult, NextPower),    % next coeficcient
    ValuePower is X ** Power,                      % calculate current power: X^n
    Result is Head * ValuePower + NextResult.      % a * current power + next coeficcient
