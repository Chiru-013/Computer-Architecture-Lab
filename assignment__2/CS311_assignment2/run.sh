3#!/bin/bash
ant;
ant make-jar;
java -Xmx1g -jar jars/assembler.jar test_cases/1.asm a.out;
