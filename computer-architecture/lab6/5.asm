ASSUME CS: code, DS:data
;5. Two byte string S1 and S2 are given, having the same length.
; Obtain the string D by intercalating the elements of the two strings.
; Exemple:
;   S1: 1, 3, 5, 7
;   S2: 2, 6, 9, 4
;   D: 1, 2, 3, 6, 5, 9, 7, 4

data SEGMENT
    s1 DB 1, 3, 5, 7
    s_len = $-s1

    s2 DB 2, 6, 9, 4

    d_len = s_len * 2
    D DB d_len DUP(?)

    result DW ?
data ENDS

code SEGMENT
start:
    mov AX, data
    mov DS, AX

    mov SI, 0
    mov DI, 0
    mov CX, s_len

    repeat:
        ; move from s1
        mov AL, s1[SI]
        mov d[DI], AL

        ; mov from s2
        mov BL, s2[SI]
        mov d[DI+1], BL

        ; increment
        add DI, 2
        inc SI
        loop repeat

    mov AX, 4C00h
    int 21h
code ENDS
END start
