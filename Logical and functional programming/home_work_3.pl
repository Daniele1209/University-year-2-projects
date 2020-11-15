variant(1).
variant(3). % X = 3
variant(2).

% toate(n, c, p) =
%	c, n = 4
%	toate(n + 1, candidat(x) + c, p * x), p*x % 27 = 0

% toate(N:number, C:list, R:list, P:number)
% toate(i, i, o, i)

prediction(4,R,R,_):-!.
prediction(I,C,R,P):-
    candidat(X),
    I1 is I+1,
    P1 is P*X,
    P1 mod 27 =\= 0,
    toate(I1,[X|C],R,P1).

% rez(R:list)
% rez(o)

rez(R):-toate(1,[1],R,1).
rez(R):-toate(1,[3],R,1).

% allsolutions(R:list)
% allsolutions(o)

prediction_list(R) :-
    findall(RPartial, rez(RPartial), R).
