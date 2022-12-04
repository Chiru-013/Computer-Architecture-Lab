	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
    load %x0, $n, %x3
    addi %x0, 0, %x5
    addi %x0, 25, %x10
    addi %x0, 0, %x4
sumcalc:
    load %x4, $a, %x6
    add %x6, %x5, %x5
    addi %x4, 1, %x4
    bne %x4, %x3, sumcalc
meancalc:
    div %x5, %x3, %x6
    store %x6, 0, %x10
    end