% prediction(Count, L, P) =  L, n = 4
%                        prediction(Count+1, candidate(X)+L, P*X),if
%                        P*X!=27

% prediction(Count:element, L:list, R:list, P:element)
% (i, i, o, i)
prediction(4,R,R,_):-!.
prediction(Count,L,R,P):-
    candidate(X),
    Count_aux is Count+1,
    P_aux is P*X,
    P_aux mod 27 =\= 0,
    prediction(Count_aux,[X|L],R,P_aux).

%last candidate represents
candidate(1).
candidate(2).
candidate(3).

% rez(R:list)
% (o)
rez(R):-prediction(1,[1],R,1).
rez(R):-prediction(1,[3],R,3).

% prediction_list(R:list)
% (o)
prediction_list(R) :-
    findall(X, rez(X), R).
