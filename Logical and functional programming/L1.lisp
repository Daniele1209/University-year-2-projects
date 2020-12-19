; Problem 8

; a) Write a function to return the difference of two sets.

;Contains(l, e) - F, if l = null
;				- T, if l1 = e
;				- Contains(l2..ln, e), otherwise

(defun Contains (l e)
    (cond
        ((null l) nil)
        ((= (car l) e) T)
        (T (Contains (cdr l) e))))

;Difference(l, k) - F, if l = null
;					 - l1 U Difference(l2...ln, k), if ! Contains(k, (l1))
;					 - Difference(l2...ln, k), otherwise

(defun Difference (l k)
    (cond
         ((null l) nil)
         ((not (Contains k (car l))) (cons (car l) (Difference (cdr l) k)))
         (T (Difference (cdr l) k))))

(print (Difference '(1 7 3 10 5) '(2 3 4 5 6 7)))


; b) Write a function to reverse a list with its all sublists, on all levels.

;Append_fct(l, k) - (k), if l is null
;			 	  - l1 U Append_fct(l2...ln, k)

(defun Append_fct (l k)
    (if (null l) 
        k
        (cons (car l) (Append_fct (cdr l) k))))

;Reverse_fct(l) - (), if l is empty
;			    - Append_fct (Reverse_fct(l2...ln), list(Reverse_fct(l1)), if l1 is a list
;				- Append_fct (Reverse_fct(l2...ln), list(l1)), otherwise

(defun Reverse_fct (l)
    (cond
        ((null l) nil)
        ((listp (car l)) (Append_fct (Reverse_fct (cdr l)) (list (Reverse_fct (car l)))))
        (T (Append_fct (Reverse_fct (cdr l)) (list (car l))))))

(print (Reverse_fct '(1 2 (3 (4 5) (6 7)) 8 (9 10 11))))


; c) Write a function to return the list of the first elements of all list elements of a given list with an odd
; number of elements at superficial level. Example:
;  (1 2 (3 (4 5) (6 7)) 8 (9 10 11)) => (1 3 9).

;Length_fact(l) - 0, if l is null
;				- 1 + Length_fct(l2...ln), otherwise

(defun Length_fct (l)
    (if (null l) 
        0
        (+ 1 (Length_fct (cdr l)))))

(defun List_len (l)
    (= (mod (Length_fct l) 2) 1))

;First_elem(l) - nil, if l is an atom
;			   - l1 U (U 'First_elem (l2...ln)), if List_len(l) = T
; 			   - append (mapcar 'First_elem (cdr l)), otherwise

(defun First_elem (l)
    (cond
        ((atom l) nil)
        ((List_len l) (cons (car l) (apply 'append (mapcar 'First_elem (cdr l)))))
        (T (apply 'append (mapcar 'First_elem (cdr l))))))

(print (First_elem '(1 2 (3 (4 5) (6 7)) 8 (9 10 11))))
    


; d) Write a function to return the sum of all numerical atoms in a list at superficial level.

;Sum(l) - l, if l is a number
;		- 0 if l is an atom
;		- Sum(l1)+Sum(l2)+...+Sum(ln)

(defun Sum (l)
    (cond
        ((numberp l) l)
        ((atom l) 0)
        ((listp l) (apply '+ (mapcar 'Sum l)))))

(print (Sum '(1 2 (3 (4 5) (6 7)) 8 (9 10 11))))