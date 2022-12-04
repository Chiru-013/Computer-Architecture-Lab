	.data
a:
	-2
	.text
main:
    load %x0, $a, %x3 ;x3 = a
    addi %x0, 2, %x5 ;x5 = 2
    addi %x0, 1, %x10 ;x10 = 1 ;store 1 if prime
    blt %x3, %x5, isnotprime
loop:
    beq %x5, %x3, moveOut
    div %x3, %x5, %x30
    beq %x31, %x0, isnotprime
    addi %x5, 1, %x5
    jmp loop
isnotprime:
    subi %x0, 1, %x10
moveOut:
    end