% a)

%gcd(A:element, B:element, Rez:element)
%(i,i,o) - determinalistic
gcd(A, 0, A):-!.
gcd(A, B, Rez):-
    X is mod(A, B),
    gcd(B, X, Rez).

%lcm(A:element, B:element, LCM:element)
%(i, i, o) - determinalistic
lcm(A, B, LCM):-
    gcd(A, B, GCD),
    LCM is A*B//GCD.

%lcm_array(L:list, R:element)
%(i, o) - determinalistic
lcm_array([R|_], R).
lcm_array([H|[S|T]], R):-
    lcm(H, S, R1),
    lcm_array([R1|T], R2),
    R =  R2.

% b)






