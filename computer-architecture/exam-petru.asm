; Read from the keyboard a number composed of multiple digits
; and do the sum of all the digits and write the result into a file

assume cs:code,ds:data
data segment
	msg db 'Name of the file: $'
	mnr db 'Enter the number: $'
	mNrLen db 12
	lNr db ?
	nr db 12 dup (?)
	maxFileName db 12
	lFileName db ?
	fileName db 12 dup (?)
	maxRezLen db 12
	rezLen db ?
	rez db 12 dup(?)
	openErrorMsg db 'File does not exist.$'
	writeErrorMsg db 'Can t write to file.$'
	lgsum dw ?
data ends

code segment
start:
	mov ax,data
	mov ds,ax
	
	; print the string "mnr" on the screen	
	mov ah, 09h
	mov dx, offset mnr
	int 21h

	;read the number
	mov ah, 0ah
	mov dx, offset mNrLen
	int 21h
	
	mov cl, lNr
	mov ch, 0
	mov si, offset nr
	aici:
		LODSB
		sub al, '0'
		add bl, al
		adc bh, 0
	loop aici
	
	;put the inverted sum into rez
	mov ax, bx
	mov bx, 0
	inv:
		mov cx, 16
		div cl
		cmp ah, 9
		ja litera
			add ah, '0'
			jmp addddd
		litera:
			add ah, 'A'-10
		addddd:
		mov rez[bx], ah
		mov ah, 0
		add bx, 1
		cmp ax, 0
		jne inv
	mov rez[bx], '$'
	mov lgsum, bx
	sub bx, 1
	mov bp, 0
	;invert rez
	cmp bp, bx
	jae qwerty
	inve:
		;swap rez[bp], rez[bx]
		mov dl, rez[bp]
		mov al, rez[bx]
		mov rez[bx], dl
		mov rez[bp], al
		add bp, 1
		sub bx, 1
		cmp bp, bx
		jb inve
	; print the string "msg" on the screen	
	qwerty:
	mov ah, 09h
	mov dx, offset msg
	int 21h

	
	; read from the keyboard the name of the file using interrupt 21, function 0ah
	mov ah, 0ah
	mov dx, offset maxFileName
	int 21h
 
	; we transform the filename into an ASCIIZ string (put zero at the end)
	mov al, lFileName
	xor ah, ah
	mov si, ax
	mov fileName[si], 0
	
	; open the file using function 3dh of the interrupt 21h
	mov ah, 3dh
	mov al, 1 
	mov dx, offset fileName
	int 21h	
	
	jc openError ; CF will be set by the CPU if an error occured
	mov bx, ax 
	
	;write to file	
	mov ah,40h
	mov cx,lgsum
	mov dx,offset rez
	int 21h
	jc writeError
	
	jmp endprg

	openError:		; print the openErrorMsg string using function 09h of interrupt 21h
		mov ah, 09h
		mov dx, offset openErrorMsg
		int 21h
		jmp endPrg 
	writeError:		; print the readErrorMsg string using function 09h of interrupt 21h
		mov ah, 09h
		mov dx, offset writeErrorMsg
		int 21h 
	endprg: 
	mov ax,4c00h
	int 21h
code ends
end start