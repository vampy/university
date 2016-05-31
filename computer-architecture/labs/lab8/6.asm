ASSUME CS: code, DS:data

; 6. Implement an authentication program. The program has the string
; 'password' defined in its data segment. The program repeatedly asks
; the user to input the password at the keyboard and verifies if the
; password given by the user is identical with the string 'password' from
; the data segment and prints a suitable message on the screen. The
; program exits when the user guessed the password.

data SEGMENT
    ; the program password
    password                DB  'password123'
    password_len            =   $-password

    ; the user password
    user_password_max       DB  20 ; max length of the password
    user_password_length    DB  ? ; charcters received
    user_password           DB  20 DUP(?)

    msg_start               DB  'Authentication program. Please enter passoword.', '$'
    msg_give_password       DB  'Password: ', '$'
    msg_wrong_password      DB  'Wrong password please try again', '$'
    msg_correct_password    DB  'Password is correct. Welcome :)', '$'
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
        ; After IHR will be executred the name of the file will be stored
        ; at max_bytes + 2 and the offset max_bytes + 1 will store the length
    ENDM


start:
    mov AX, data
    mov DS, AX
    mov ES, AX

    ; print start message
    PRINT_STRING msg_start
    PRINT_EOL
    PRINT_EOL

    ; while 1
    repeat:
        PRINT_STRING msg_give_password
        READ_STRING user_password_max

        ; compare length
        mov AL, user_password_length
        cmp AL, password_len
        ; if passwords do not have the same length do not compare
        jnz wrong_password

        ; check password character by character
        mov CX, password_len
        lea SI, password
        lea DI, user_password
        check_password:
            cmpsb
            jnz wrong_password

            loop check_password

        ; if here our password is good
        jmp after_repeat

        jmp end_repeat
        wrong_password:
            ; wrong password try again
            ; print message
            PRINT_EOL
            PRINT_STRING msg_wrong_password
            PRINT_EOL
            jmp end_repeat

        end_repeat:
            jmp repeat
    after_repeat:

    end_start:
        PRINT_EOL
        PRINT_EOL
        PRINT_STRING msg_correct_password

        mov AX, 4C00h ; function 4Ch with exit code 0
        int 21h
code ENDS
END start
