	.file	"p2g.c"
	.section	.rodata.cst4,"aM",@progbits,4
	.align	4
.LCPI0_0:
	.long	1082549862              # float 4.19999981
	.text
	.globl	main
	.align	16, 0x90
	.type	main,@function
main:                                   # @main
# BB#0:
	pushl	%ebp
	movl	%esp, %ebp
	subl	$24, %esp
	leal	.L.str, %eax
	movss	.LCPI0_0, %xmm0
	movss	%xmm0, -4(%ebp)
	cvtss2sd	-4(%ebp), %xmm0
	movl	%eax, (%esp)
	movsd	%xmm0, 4(%esp)
	calll	printf
	leal	.L.str1, %ecx
	cvtss2sd	-4(%ebp), %xmm0
	movl	%ecx, (%esp)
	movsd	%xmm0, 4(%esp)
	movl	%eax, -8(%ebp)          # 4-byte Spill
	calll	printf
	movl	$0, %ecx
	movl	%eax, -12(%ebp)         # 4-byte Spill
	movl	%ecx, %eax
	addl	$24, %esp
	popl	%ebp
	ret
.Ltmp0:
	.size	main, .Ltmp0-main

	.type	.L.str,@object          # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"%.50f\n"
	.size	.L.str, 7

	.type	.L.str1,@object         # @.str1
.L.str1:
	.asciz	"%f\n"
	.size	.L.str1, 4


	.ident	"Ubuntu clang version 3.4-1ubuntu3 (tags/RELEASE_34/final) (based on LLVM 3.4)"
	.section	".note.GNU-stack","",@progbits
