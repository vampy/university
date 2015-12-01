ASSUME CS: code, DS:data

; 1. A string of bytes is given in the data segment.
; Print on the standard output (screen) the elements of this string in base 2.

data SEGMENT
    nr DB 23
    ten DB 10
    two DB 2

    hex_table DB '0123456789ABCDEF'
data ENDS

code SEGMENT
    PRINT_EOL MACRO
        mov DL, 13 ; \r
        mov AH, 02h
        int 21h
        mov DL, 10 ; \n
        mov AH, 02h
        int 21h
        ; print \r\n
    ENDM

    PRINT_STRING MACRO string
        lea DX, string
        mov AH, 09h
        int 21h
    ENDM

    PRINT_CHAR MACRO char
        mov DL, char
        mov AH, 02h
        int 21h
    ENDM

    READ_STRING MACRO max_bytes
        mov AH, 0Ah
        lea DX, max_bytes
        int 21h
        ; Afet IHR will be executred the name of the file will be stored
        ; at max_bytes + 2 and the offset max_bytes + 1 will store the length
    ENDM

    CONVERT_TO_ASCIIZ MACRO string, string_length
        ; Assume string is always longer with 1
        mov AL, string_length
        mov AH, 0
        mov SI, AX
        mov string[SI], 0
    ENDM

    ; BX will be altered and also AL
    ; return:
    ;       in AL
    hexnum_to_char PROC
        lea BX, hex_table
        xlat
        ret
    hexnum_to_char ENDP

start:
    mov AX, data
    mov DS, AX
    mov ES, AX

    PRINT_EOL

    mov AL, nr
    mov AH, 0
    mov CX, 0
    repeat_stack:
        ; AX / 2
        div two
        ; remainder in AH, quotient in AL
        mov BL, AH
        mov BH, 0
        push BX

        ;prepare for next iteration
        mov AH, 0
        inc CX

        ; until AL the quotient is zero
        cmp AL, 0
        jnz repeat_stack

    repeat_display:
        pop AX
        add AL, '0'
        PRINT_CHAR AL
        loop repeat_display

    ;READ_STRING user_password_max
    ;CONVERT_TO_ASCIIZ user_password, user_password_length

    end_start:
        PRINT_EOL
        ;........
        mov AX, 4C00h ; function 4C with exit code 0
        int 21h
code ENDS
END start
