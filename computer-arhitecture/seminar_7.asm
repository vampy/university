--- Seminary 7. Multi-module programming ---

Multi-module programming - is writing a program which is formed by two or 
                             more files(modules)
    - main module(containing the main program)
    - secondary(containing procedures or functions which are called by the main
    module)

In order to pass data or information between modules we use 3 new directives:
    - PUBLIC symbol - exports "symbol" which is defined in the current module
                      in order to be used in another module
    - EXTRN symbol:type - imports "symbol" which is defined in another module 
                          in order to be used in this module. 
                          "type" can be: BYTE, WORD, DWORD, 
                          NEAR or FAR(for addreses),
                          PROC(for procedures), ABS(for constants, equ)
    - GLOBAL symbol[:type]  - does the same thing as PUBLIC if "symbol" is 
                              defined in the current module and does the same
                              thing as EXTRN if "symbol" is defined in an 
                              external module

Ex.1
Write a program which concatenates 2 strings by calling a procedure written
in a different module

;Module main.asm
ASSUME DS:data, CS:code
data SEGMENT
    ; asciiz strings ends in 0
    s1  DB  "Happy", 0
    s2  DB  " holiday!", 0
    result  DB  30 DUP(?)
data ENDS

PUBLIC result ;exports result
EXTRN concat:PROC ;import procedure concat from module second.asm

code SEGMENT
start:
    mov AX, data
    mox DS, AX
    ; ------

    lea AX, s1 ; <=> mov AX, offset s1
    lea BX, s2 ; <=> mov AX, offset s2
    ; we pass offset s1 in AX and offset s2 in BX to procedure concat
    call concat 

    ; print result using function 09h
    mov AH, 09h
    lea DX, result
    int 21h

    mov AX, 4C00h
    int 21h
code ENDS
END start

;Module second.asm
ASSUME DS:data, CS:code

data SEGMENT
data ENDS

EXTRN result:BYTE ; import from main.asm
PUBLIC concat ; export procedure concat

code SEGMENT
    concat PROC
        ; we have in AX the offset of s1 and in BX the offset of s2
        ; we put the address of s1 in DS:SI 
        mov SI, AX

        ; we put the address of result in ES:DI
        push DS
        pop ES
        lea DI, result

        cld

        repeat_a:
            lodsb

            cmp AL, 0
            je next_a ; if AL == 0 we have reached the end of s1
            
            stosb
            jmp repeat_a
        next_a:
        
        ; put the address of s2 in DS:SI
        mov SI, BX
        repeat_b:
            lodsb

            cmp AL, 0
            je next_b ; AL == 0 we have reached the end of s2
            
            stosb
            jmp repeat_b
        next_b:

        mov AL, '$'
        stosb ; put '$' at the end of result 

        ret
    concat ENDP
code ENDS
END

Building the executable:
    tasm /zi main.asm
    tasm /zi second.asm
    tlink /v main+second => main.exe
In the executable program main.exe the data segment from main module is
concatenated with the data segment from the second module and the same 
happens with the code segments from both modules



Ex.2
A string of bytes is read from a file. Build a new string of bytes in which we 
put the reverse of the bytes from the first string which have an equal number
bits 1 and 0,

ASSUME CS:code, DS:data

data SEGMENT
    inputFile    DB  "input.txt", 0 ; ASCIIZ string
    
    s1  DB 10 DUP(?)
    lenght1 DB 0 ; length1 will store the length of s1
    s2  DB 10 DUP(?)
    
    handle  DW   ? ; for the handle
data ENDS

code SEGMENT
start:
    mov AX, data
    mov DS, AX
    ; ---
    
    ; open the file "input.txt" with function 3Dh of int 21h
    mov AH, 3Dh
    mov AL, 0 ; open the file in read only mode
    lea DX, inputFile
    int 21h ; function 3Dh of int 21h returns in AX the file handle or
            ; it return in AX an error code and sets CF=1 if an error
            ; occured. We assume there is NO error
    
    mov handle, AX ; save the file handle

    ; we read maximum of 10 bytes from the file into s1 using function
    ; 3Fh of int 21h
    mov AH, 3f
    mov BX, handle ; file handle
    mov CX, 10 ; maximum number of characters to be read 
    lea DX, s1 ; we read bytes in s1
    int 21h
    ; the function returns in AX the actual number of read bytes or it sets
    ; CF=1 if error occured
    
    ; we only read a small number of bytes so the result will fit
    mov length, AL

    ; close the file using 3Eh of int 21h
    mov AH, 3Eh
    mov BX, handle
    int 21h

    ; we parse the bytes of s1
    ; we put the address of s1 in DS:SI and
    ; the address of s2 in ES:DI
    lea SI, s1
    push DS
    pop ES ; make ES to be the same as DS
    lea DI, s2

    mov CL, length
    mov CH, 0
    cld

    repeat:
        lodsb
        ; we count in BL the number of bits from AL

        mov BL, 0
        push CX ; save CX on the stack

        mov CX, 8
        repeat_1:
            rol AL, 1 ; the first bit from the left is stored in CF 
            adc BL, 0 ; BL = BL + 0 + CF
            loop repeat_1

        cmp BL, 4
        jne next
        ; if the number of bits 1 =  the number of bits 0 reverse AL
        ; and put the result in s2

        ; we put the reverse of AL in AH
        mov AH, 0
        mov CX, 8
        repeat_2:
            rol AL, 1 ; will set carry flag
            rcr AH, 1 ; will shift with carry flag
            loop repeat_2
        ; reverse AL

        mov AL, AH ; put the inverse byte AH in s2
        stosb
        
        next:
            pop CX ; restore CX from the stack
        
            loop repeat
    
    mov  AX, 4C00h
    int 21h
code ENDS   
END START
