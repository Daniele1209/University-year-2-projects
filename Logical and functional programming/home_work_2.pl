%a)
%predecessor(L:list, R:list, C:element)
%(i,o,i)
predecessor([], [], _).
predecessor([0], [9], 1).
predecessor([H],[H2], 0):-
    H2 is H-1.
predecessor([0|T], [9|R], 1):-
    predecessor(T, R, 1).
predecessor([H|T], [H2|R], 0):-
    predecessor(T, R, C),
    H2 is H - C.

%b)

% predecessor_list(L1:list of list and numbers, L2:list of lists and
% numbers)
% (i,o)
predecessor_list([],[]).
predecessor_list([H|T], [HR|R]):-
    is_list(H),
    predecessor(H,HR, _),
    predecessor_list(T, R).
predecessor_list([H|T], [H|R]):-
    predecessor_list(T, R).

