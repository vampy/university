; 4.  Return the list of nodes of a tree of type (1) accessed inorder.
; inorder(l:list)
; l - the list representing the tree
(defun inorder(l)
  (cond
    ((null l) nil)
    ((= (cadr l) 1) (append (inorder (left_subtree (cddr l) 1)) (list (car l))))
    ((= (cadr l) 2) (append (inorder (left_subtree (cddr l) 1)) (list (car l)) (inorder (right_subtree (cddr l) 1))))
    (t (list (car l)))
  )
)

; left_subtree(l:list, n:number)
; l - the list representing the tree to be split
; n - number of leafs to be searched
(defun left_subtree(l n)
    (cond
        ((null l) nil)
        ((= n 0) nil)
        ((= (cadr l) 2) (append (list (car l)) (list (cadr l)) (left_subtree (cddr l) (+ n 1))))
        ((= (cadr l) 0) (append (list (car l)) (list (cadr l)) (left_subtree (cddr l) (- n 1))))
        (t (append (list (car l)) (list (cadr l)) (left_subtree (cddr l) n)))
    )
)


; right_subtree(l:list, n:number)
; l - the list representing the tree to be split
; n - number of leafs to be searched
(defun right_subtree(l n)
    (cond
        ((null l) nil)
        ((= n 0) l)
        ((= (cadr l) 2) (right_subtree (cddr l) (+ n 1)))
        ((= (cadr l) 0) (right_subtree (cddr l) (- n 1)))
        (t (right_subtree (cddr l) n))
    )
)

; 4.  Return the list of nodes of a tree of type (1) accessed inorder.
; Example:
; CL-USER> (return-inorder '(1 2 2 0 3 2 4 0 5 0))
; (2 1 4 3 5)

(defvar *inorder-traversal*)

(defun inorder (tree)
  (let ((root (car tree))
        (nr (cadr tree)))
    (cond
      ((= nr 0) (push root *inorder-traversal*) (cddr tree))
      ((= nr 1) (setf tree (inorder (cddr tree))) (push root *inorder-traversal*) tree)
      ((= nr 2) (setf tree (inorder (cddr tree))) (push root *inorder-traversal*) (inorder tree)))))

(defun return-inorder (tree)
  (setf *inorder-traversal* nil)
  (inorder tree)
  (reverse *inorder-traversal*))
