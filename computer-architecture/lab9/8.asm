ASSUME CS: code, DS:data

; 8. Print of the screen, for each number between 32 and 126, the value
; of the number (in base 10) and the character whose ASCII code the number is.

data SEGMENT public
    i_start DB 32
    i_end DB 126
data ENDS

code SEGMENT public
; import print procedure
extrn print:proc

start:
    mov AX, data
    mov DS, AX
    ; .......

    repeat:
        mov AL, i_start
        call print

        inc i_start
        cmp AL, i_end
        jne repeat

    end_start:
        mov AX, 4C00h ; function 4C with exit code 0
        int 21h
code ENDS
END start
