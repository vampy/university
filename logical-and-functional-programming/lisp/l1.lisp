;; 4.
;;  a. Write a function to return the maximum value of all the numerical atoms of a list,
;;     at any level.
;;  b. Write a function to return the list of all permutations of a given list.
;;  c. Write a function to return T if a list has an even number of elements on the first level,
;;     and NIL on the contrary case, without counting the elements of the list.
;;  d. Write a function to return the inner product of two vectors.


;; a.
(defun max_value (a b)
  (if (and (numberp a) (numberp b))
    (cond
      ((> a b) a)
      (b))
    (error "ERROR: both are not atoms and numbers")))

; max_list(l: list)
; - l - the list we are searching the maximum in, at any level
(defun max_list (l)
  (let ((head (first l)) (tail (rest l)))
    (cond
      ((null head) 0) ; 0 default max
      ((and (null tail) (numberp head)) head) ; one element number
      ((and (null tail) (listp head)) (max_list head)) ; one element list
      ((numberp head) (max head (max_list tail))) ; head is a number
      ((listp head) (max (max_list head) (max_list tail))) ; head is a list
      (t (max_list tail)))))


;; b) Write a function to return the list of all permutations of a given list.
(defun list_length (l)
  (let ((tail (cdr l)))
    (cond
      ((null l) 0)
      (t (+ 1 (list_length tail))))))
(defun list_append (x y)
  (let ((head (car x)) (tail (cdr x)))
    (cond
      ((null x) y)
      (t (cons head (append tail y))))))

; insert element on position n
; insert_pos(elem: atom, n: number, l: list)
; - elem - the element we want to insert
; - n - the position we want to insert the element
; - l - the list where we want the element to be inserted
(defun insert_pos (elem n l)
  (let ((head (car l)) (tail (cdr l)))
    (cond
      ((= n 1) (cons elem l)) ; we found our element, insert on this position
      (t (cons head (insert_pos elem (- n 1) tail)))))) ; keep first element, insert on position n-1, rest of the list

; insert an element on all positions up to n, auxiliary function
; insert_all_n(e: atom, n: number, l: list)
; - e - the element we want to insert in n positions
; - n - in how many positions and lists we will return
; - l - the list we want to insert the element in n positions
(defun insert_all_n (e n l)
  (cond
     ((= n 0) NIL) ; pos 0, insert nothing
     (t (cons (insert_pos e n l) (insert_all_n e (- n 1) l))))) ; insert on position n, and n-1 positions

; insert an element on all positions of a list, generate a list of sublists
(defun insert_all (e l)
  (insert_all_n e (+ 1 (length l)) l))

; insert an element on all positions of the sublists, permute_aux, kinda
; add_to_sublists(e: atom, l: list):
; - e - the element to insert in all the sublists
; - l - the lists of lists what we want to insert into
(defun add_to_sublists(e l) ; l is list of lists
  (let ((head_list (car l)) (tail (cdr l)))
    (cond
      ((null l) NIL) ; do nothing
      (t (append (insert_all e head_list) (add_to_sublists e tail)))))) ; recursively on all sublists

; permute the tail of the list, and insert all the element in the permutations obtained
; permute(l: list):
; - l - the list we want to generate the permutations of
(defun permute (l)
  (let ((head (car l)) (tail (cdr l)))
    (cond
      ((null tail) (list (list head))) ; one element, return a list of list
      (t (add_to_sublists head (permute tail)))))) ; permute the rest of the list


;; c) Write a function to return T if a list has an even number of elements on the first level,
; and NIL on the contrary case, without counting the elements of the list.
; is_even(l: list)
; - l - the list to check if the number of elements is even
(defun is_even (l)
  (let ((head1 (car l)) (head2 (cadr l)) (tail (cddr l)))
    (cond
      ((and (null head1) (null head2)) T) ; both are null, list has even number of elements
      ((null head2) NIL) ; first element is not null, second is, odd
      (T (is_even tail))))) ; compute rest


;; d) Write a function to return the inner product of two vectors.
; dot_product(A: list, B: list)
; - A - the first vector
; - B - the second vector
(defun dot_product (A B) ; vectors should be equal in length
  (let ((Ahead (car A)) (Atail (cdr A)) (Bhead (car B)) (Btail (cdr B)))
    (cond
      ((and (null Atail) (null Btail)) (* Ahead Bhead)) ; both lists ended
      (T (+ (* Ahead Bhead) (dot_product Atail Btail)))))) ; rest of the array
