	.data
a:
	70
	80
	40
	20
	1
    9
n:
	6
	.text
main:
    load %x0, $n, %x3
    addi %x0, 0, %x4
    addi %x0, 0, %x10
    addi %x0, 1, %x9
loop:
    load %x4, $a, %x5
    jmp checkPos
increement:
    addi %x4, 1, %x4
    beq %x4, %x3, moveOut
    jmp loop
checkPos:
    blt %x5, %x0, increement
checkEven:
    beq %x9, %x5, increement
    divi %x5, 2, %x20
    beq %x31, %x0, count
    jmp increement
count:
    addi %x10, 1, %x10
    jmp increement
moveOut:
    end