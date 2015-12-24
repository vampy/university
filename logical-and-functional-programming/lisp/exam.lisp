(defun list-append (x y)
  (cond
    ((null x) y)
    (t (cons (car x) (list-append (cdr x) y)))))

(defun list-reverse (l &optional (accum nil))
  (cond
    ((null l) accum)
    (t (list-reverse (cdr l) (cons (car l) accum)))))

(defun min-el (l)
  (cond
    ((numberp l) l)
    ((or (null l) (atom l)) 9999999999999999)
    (t (apply #'min (mapcar #'min-el l)))))

(defun insert-el (l el)
  (insert-el-aux l  el (min-el l)))

; l - the list we insert in
; el - the element we want to insert
; minel - the element what we want to insert after
(defun insert-el-aux(l el minel)
  (cond
    ((null l) nil) ; found end of list
    ((listp (car l)) (list-append (list (insert-el-aux (car l) el minel)) (insert-el-aux (cdr l) el minel))) ; list
    ((and (numberp (car l)) (= (car l) minel)) (cons (car l) (cons el (insert-el-aux (cdr l) el minel)))) ; found minimum, insert
    (t (cons (car l) (insert-el-aux (cdr l) el minel))))) ; continue down the list


; problema lui petru, rezolvata in ~1 min :P
; filter all non numeric from an unliniar list
(defun filter-atoms (l)
  (cond
    ((or (null l) (numberp l)) nil)
    ((atom l) (list l))
    (t (mapcan #'filter-atoms l))))
