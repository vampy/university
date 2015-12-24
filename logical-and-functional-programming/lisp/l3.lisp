; 6.  Write a function that returns the depth of a list.

(defun max-depth (l)
  (cond
    ((atom l) 0)
    (t (+ 1 (apply #'max (mapcar #'max-depth l))))))


; 3. Se da o lista neliniara. Sa se scrie un program LISP pt determinarea numarului de subliste de la orice
; nivel pt care nr-ul atomilor nenumerici de pe nivelurile pare(de la orice nivel)este impar
;- nivelul superficial al listei se considera 1. Prelucrarea se va face folosind o functie MAP.
; De exemplu, lista A (B 2) (1 C 4) (1(6 F)) (((G)4)6)) are 3 astfel de subliste: lista, (1(6 F)) si ((G)4)
; remove all numbers in list, except the first one odd one number if it exists, this function works as a filter
(defun nr-sublists-remove1 (l &optional found_nr found)
  (let (is_number (numberp (car l)) (is_odd (oddp (car l))) (head (car l)) (tail (cdr l)))
    (cond
      ((null l) nil)
      ((and is_number found_nr found)       (nr-sublists-remove tail T T)) ; ignore number, we found our number
      ((and is_number is_odd (not found_nr) (not found)) (cons head (nr-sublists-remove tail T T))) ; found first number and it is odd
      ((and is_number        found_nr       (not found)) (nr-sublists-remove tail T F))  ; ignore all numbers
      ((and is_number        (not found_nr) (not found)) (nr-sublists-remove tail T F)) ; found a number but it is not odd, remove all numbers
      (t (cons head (nr-sublists-remove tail found))))))
(defun nr-sublists-remove (l &optional found)
  (cond
    ((null l) nil)
    ((and (numberp (car l)) (oddp (car l)) found) (nr-sublists-remove (cdr l) found))
    ((and (numberp (car l)) (oddp (car l)) (not found)) (cons (car l) (nr-sublists-remove (cdr l) T))) ; found or odd umber
    (t (cons (car l) (nr-sublists-remove (cdr l) found)))))
(defun nr-sublists (l)
  (cond
    ((null l) 0)
    ((and (numberp l) (oddp l)) 1)
    ((atom l) 0)
    (t (apply #'+ (mapcar #'nr-sublists (nr-sublists-remove l))))))

; numarul atomilor dintr-o lista neliniara
(defun nr-atoms (l)
  (cond
    ((null l) 0)
    ((atom l) 1) ; mark with number
    (t (apply #'+ (mapcar #'nr-atoms l))))) ; acumulate results, add all of them

; double the element at every N steps
(defun double-el (l n &optional (i 1))
  (cond
    ((null l) nil)
    ((= i n) (cons (car l) (cons (car l) (double-el (cdr l) n 1)))) ; double
    (t (cons (car l) (double-el (cdr l) n (+ i 1)))))) ; normal element



(defun my-or (h &rest l)
  (cond
    ((null l) (if h T nil)) ; only have head :(
    ((not h) (apply #'my-or l)) ; try the rest
    (t T)))

(defun isnode2 (l e)
  (cond
    ((null l) nil)
    ((and (atom l) (eq l e)) T)
    ((atom l) nil)
    (t (apply #'my-or (mapcar #'(lambda(l) (isnode2 l e)) l)))))
