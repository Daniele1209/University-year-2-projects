% a)

%gcd(A:element, B:element, Res:element)
%(i,i,o)
gcd(A, 0, A):-!.
gcd(0, B, B):-!.
gcd(A, B, Res):-
    X is mod(A, B),
    gcd(B, X, Res).

%lcm(A:element, B:element, LCM:element)
%(i,i,o)
lcm(A, B, LCM):-
    gcd(A, B, GCD),
    LCM is A*B//GCD.

%lcm_array(L:list, R:element)
%(i,o)
lcm_array([R], R).
lcm_array([H|[S|T]], R):-
    lcm(H, S, R1),
    lcm_array([R1|T], R2),
    R =  R2.

% b)

%add_array(L:list, C:element, N:element, X:element, Res:list)
%(i,i,i,i,o)
add_array([],_,_,_,[]).
add_array([H|T],Count,N,X,[H|T1]) :-
    Count =\= N,
    C is Count + 1,
    add_array(T,C,N,X,T1).
add_array([H|T],Count,N,X,[H|[X|T1]]) :-
    Count =:= N,
    C is Count + 1,
    Next is N * 2,
    add_array(T,C,Next,X,T1).

%add(L1:list, X:element, L2:list)
%(i,i,o)
add(L1,X,L2):-
    Count = 1,
    N = 1,
    add_array(L1,Count,N,X,L2).








