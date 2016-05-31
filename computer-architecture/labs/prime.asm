ASSUME CS: code, DS:data
; Check if a number is prime

data SEGMENT
    ; variables for is_prime procedure
    is_prime_n DW 0
    prime_start DW 2
    prime_end DW ?

    msg_number DB "Check a number:", "$"
    number_max      DB 5 ; max length of number
    number_length   DB ?
    number          DB 5 DUP(?)

    test_n DW 2

    n2 DW 2
data ENDS

code SEGMENT

PRINT_CHAR MACRO char
    mov DL, char
    mov AH, 02h ; print char
    int 21h
ENDM

PRINT_STRING MACRO string
    lea DX, string
    mov AH, 09h ; print string
    int 21h
ENDM

PRINT_EOL MACRO
    PRINT_CHAR 13 ; /r
    PRINT_CHAR 10 ; /n
ENDM

is_prime PROC
    ; Input the number which is a word into AX
    ; return in AX = 1 if is prime
    ; otherwise return in AX = 0
    push BX
    push CX
    push DX

    ; save number
    mov is_prime_n, AX

    ; test edge case, number should be >= 2
    cmp is_prime_n, 2
    jl is_prime_false ; is_prime_n < 2 (signed comparison)

    ; we will from 2 to number/2 + 1 to check if is prime
    mov DX, 0
    div n2 ; in AX the quotient and in DX the remainder
    mov prime_end, AX
    inc prime_end

    ; prepare loop
    is_prime_loop:
        ; divide by prime by each number between 2 and number/2 + 1
        mov AX, is_prime_n
        mov DX, 0
        div prime_start ; quotient = AX, remainder = DX

        ; check primality
        cmp DX, 0
        jz is_prime_false ; if remainder is 0 number is not prime

        ; increment the numerator
        inc prime_start

        ; loop
        mov DX, prime_start
        cmp DX, prime_end
        jne is_prime_loop

    ; if we are here number is prime
    jmp is_prime_true

    jmp end_is_prime
    is_prime_true:
        mov AX, 1
        jmp end_is_prime

    jmp end_is_prime
    is_prime_false:
        mov AX, 0
        jmp end_is_prime

    end_is_prime:

    pop BX
    pop CX
    pop DX
    ret

is_prime ENDP

start:
    mov AX, data
    mov DS, AX

    add test_n, 0
    js negative_number

    jmp end_negative_number
    negative_number:
        PRINT_CHAR "-"
    end_negative_number:

    ; read a number
    PRINT_EOL
    PRINT_STRING msg_number
    mov AH, 0Ah
    lea DX, number_max
    int 21h

    end_start:
        mov AX, 4C00h ; function 4C with exit code 0
        int 21h
code ENDS
END start
