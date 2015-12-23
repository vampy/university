; An n-ary tree is memorised in the following two ways:
;
;	(node no-subtrees list-subtree-1 list-subtree-2 ...)	(1)
;	(node (list-subtree-1) (list-subtree-2) ...)		(2)
;
; As an example, the tree
;
;	 A
;	/ \
;	B  C
;	  / \
;	 D   E
;
; is represented as follows:
;
;	(A 2 B 0 C 2 D 0 E 0)			(1)
;	(A (B) (C (D) (E)))			(2)
; '(A (B (F (G)) (H)) (C (D) (E)))
; '(A 2 B 2 F 1 G 0 H 0 C 2 D 0 E 0)


; 5.  Return the list of nodes of a tree of type (1) accessed in postorder.
(defun list_append (x y) 
  (let ((head (car x)) (tail (cdr x)))
    (cond 
      ((null x) y) 
      (t (cons head (append tail y))))))

; tree_left(l: list, nr: number)
; - l - process the list to return the left tree
; - nr - the reference count to the children
; nr must start with 1
(defun tree_left (l nr)
  (let ((node (car l)) (node_nr (cadr l)) (tail (cddr l)))
    (cond
      ((= nr 0) NIL) ; ref count is zero, return
      ((and (= nr 1) (= node_nr 0)) (list node node_nr)) ; eg: (E 0), reached a leaf
      (T (cons node (cons node_nr (tree_left tail (+ node_nr (- nr 1))))))))) ; recursively search, update ref count

; tree_right(l: list, nr: number)
; - l - process the list to return the right tree
; - nr - the reference count to the children
; nr must start with 2
(defun tree_right (l nr)
  (let ((node_nr (cadr l)) (tail (cddr l)))
    (cond 
      ((= nr 1) l) ; ref count is 1, we skipped the left graph
      (T (tree_right tail (+ node_nr (- nr 1))))))) ; ref count is 2 or higher, skip left graph

; postorder1(l: list)
; - l - the graph list we want to traverse in post order
(defun postorder1 (l)
  (let ((node (car l)) (nr_nodes (cadr l)) (tail (cddr l)))
    (cond 
      ((null l) NIL)
      ((= 0 nr_nodes) (list node)) ; node is leaf
      ((= 1 nr_nodes) (append (postorder1 (tree_left tail 1)) (list node))) ; has only left tree
      ((= 2 nr_nodes) (append (postorder1 (tree_left tail 1)) (postorder1 (tree_right tail 2)) (list node)))))) ; both

(defun postorder2 (tree)
  (cond
    ((null tree) nil)
    (T (append (postorder2 (cadr tree))  ; left
	       (postorder2 (caddr tree)) ; right
	       (list (car tree))))))    ; root


(defun max-levels (tree &optional (level 0))
  (cond
    ((null tree) (- level 1))
    (T (max (max-levels (cadr tree) (+ level 1)) (max-levels (caddr tree) (+ level 1))))))

(defun level-node (tree node &optional (level 0))
  (cond
    ((null tree) -1)
    ((equal (car tree) node) level)
    (T (max (level-node (cadr tree) node (+ level 1)) (level-node (caddr tree) node (+ level 1))))))

(defun path-node (tree node_find &optional path)
  (let ((node (car tree)) (left (cadr tree)) (right (caddr tree)))
    (cond
      ((null tree) nil)
      ((equal node node_find) (append path (list node))) ; return path
      (t (append path (path-node left node_find path) (path-node right node_find))))))

(defun nodes-klevel (tree k)
  (cond
    ((null tree) nil)
    ((= k 1) (list (car tree)))
    (t (mapcan #'(lambda (l) (nodes-klevel l (- k 1))) (cdr tree)))))

(defun nodes-klevel-replace (tree k e)
  (cond
    ((null tree) nil)
    ((= k 1) (cons e (mapcar #'(lambda (l) (nodes-klevel-replace l (- k 1) e)) (cdr tree))))
    (t (cons (car tree) (mapcar #'(lambda (l) (nodes-klevel-replace l (- k 1) e)) (cdr tree))))))


	

    ; (setX '(1 2 3 1 2 3)) => (1 2 3)
     
    (defun countX(el l)
            (cond
                    ((null l) 0)
                    ((= el (car l)) (+ 1 (countX el (cdr l))))
                    (t (countX el (cdr l)))
            )
    )
     
    (defun removeX(el l)
            (cond
                    ((null l) nil)
                    ((= el (car l)) (removeX el (cdr l)))
                    (t (cons (car l) (removeX el (cdr l))))
            )
    )
     
    (defun setX(l)
            (cond
                    ((null l) nil)
                    ((< 1 (countX (car l) l)) (cons (car l) (setx (removeX (car l) (cdr l)))))
                    (t (setX (cdr l)))
            )
    )

