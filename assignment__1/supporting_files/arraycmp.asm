	.data
a:
	70
	80
	40
	20
	10
b:
    7
	8
	400
	2
	12
n:
	5
	.text
main:
    load %x0, $n, %x5
    addi %x0, 0, %x4
    addi %x0, 11, %x9
loop:
    load %x4, $a, %x3
    load %x4, $b, %x6
    bgt %x3, %x6, shifta
shiftb:
    store %x6, 0, %x9
    addi %x9, 1, %x9
    jmp increement
shifta:
    store %x3, 0, %x9
    addi %x9, 1, %x9
increement:
    addi %x4, 1, %x4
    beq %x4, %x5, moveOut
    jmp loop
moveOut:
    end