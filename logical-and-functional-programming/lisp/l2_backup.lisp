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

(defun transform(l)
  (let ((node (car l)) (node_nr (cadr l)) (tail (cddr l)))
  (cond
    ((null l) NIL)
    ((and (not (numberp node)) (= node_nr 0) (null tail)) (cons node '(NIL NIL) )) ; leaf
    ((and (not (numberp node)) (= node_nr 0)) (cons (cons node '(NIL NIL)) (list (transform tail))))
    ((not (numberp node)) (cons node (list (transform tail)))) ; is a node
    (T (transform (cdr l)))))) ; is a number, not sure....!

(defun tree_left(l nr)
  (let ((node (car l))) (print node)
  (cond
    ((= nr 0) nil) ; ref count is zero, return
    ;((atom l) l) ; node is leaf
    ((numberp node) (cons node (tree_left (cdr l) nr)))
    ((and (= nr 1) (= (cadr l) 0)) (list node (cadr l))) ; (E 0)
    (T (cons (car l) (tree_left (cdr l) (+ (car (cdr l)) (- nr 1))))))))

(defun tree_right(l nr)
  (cond
    ((= nr 1) (tree_left l nr)) ; ref count is 1, go left
    ;((atom l) l) ; node is leaf
    (T (tree_right (cddr l) (+ (cadr l) (- nr 1)))))) ; ref count is 2

(defun transf (l)
  (let ((node (car l)) (nr_nodes (cadr l)) (tail (cddr l)))
    (cond
      ((null l) NIL)
      ((= 0 nr_nodes) (cons node '(NIL NIL))) ; node is leaf
      ((= 1 nr_nodes) (list node (transf (tree_left tail '1)))) ; has only left tree
      ((= 2 nr_nodes) (list node (transf (tree_left tail '1)) (transf (tree_right tail '2))))))) ; both

(defun postorder (tree)
  (cond
    ((null tree) nil)
    (T (append (postordine (cadr tree))  ; left
	       (postordine (caddr tree)) ; right
	       (list (car tree))))))     ; root
