% a)

%gcd(A:element, B:element, Rez:element)
%(i,i,o) - deterministic
gcd(A, 0, A):-!.
gcd(A, B, Rez):-
    X is mod(A, B),
    gcd(B, X, Rez).

%lcm(A:element, B:element, LCM:element)
%(i, i, o) - deterministic
lcm(A, B, LCM):-
    gcd(A, B, GCD),
    LCM is A*B//GCD.

%lcm_array(L:list, R:element)
%(i, o) - deterministic
lcm_array([R|_], R).
lcm_array([H|[S|T]], R):-
    lcm(H, S, R1),
    lcm_array([R1|T], R2),
    R =  R2.

% b)

%list_length(L:list, N:element)
%(i,o) - deterministic
list_length([], 0).
list_length([_|T] , L):-
    list_length(T,N),
    L is N+1 .

%add_elem(L:list, C:element, V:element, N:element, Rez:list)
%(i,_,i,o) - deterministic
add_array([T], C, _,_, Rez):-
    list_length(T, L),
    append([Rez], [T], Rez),
    C > L,
    !.
add_array([H|T], C, V, N, Rez):-
    C \== N,
    !,
    append([Rez], [V], Rez),
    add_array([H|T], C+1, V, N*2, Rez).






