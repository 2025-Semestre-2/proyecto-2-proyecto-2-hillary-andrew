package com.sistemasoperativos.pcvirtual.compilador;

import java_cup.runtime.Symbol;

%%

%class MiLexer
%unicode
%public
%cup
%line
%column

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

/* ====== DEFINICIONES DE EXPRESIONES REGULARES ====== */

/* Espacios */
space              = [ \t]+

/* Separador */
separator          = \r\n|[\r\n]
comma              = ","

/* Literales */
int_literal        = -?(0|[1-9][0-9]*)

/* Instrucciones */
load    = "load"
store   = "store"
mov     = "mov"
add     = "add"
sub     = "sub"
inc     = "inc"
dec     = "dec"
swap    = "swap"
int     = "int"
jmp     = "jmp"
cmp     = "cmp"
je      = "je"
jne     = "jne"
param   = "param"
push    = "push"
pop     = "pop"

/* Registros */
ax = "ax"
bx = "bx"
cx = "cx"
dx = "dx"
ac = "ac"

/* Argumentos int */
int20h = "20h"
int10h = "10h"
int09h = "09h"

%%

{space}               { /* ignorar espacios */ }
{separator}           { return symbol(sym.SEPARATOR); }
{comma}               { return symbol(sym.COMMA); }

/* Literal */

{int_literal}         { return symbol(sym.INT_LITERAL, Integer.valueOf(yytext())); }

/* Instrucciones */

{load}          { return symbol(sym.LOAD, yytext()); }
{store}         { return symbol(sym.STORE, yytext()); }
{mov}           { return symbol(sym.MOV, yytext()); }
{add}           { return symbol(sym.ADD, yytext()); }
{sub}           { return symbol(sym.SUB, yytext()); }
{inc}           { return symbol(sym.INC, yytext()); }
{dec}           { return symbol(sym.DEC, yytext()); }
{swap}          { return symbol(sym.SWAP, yytext()); }
{int}           { return symbol(sym.INT, yytext()); }
{jmp}           { return symbol(sym.JMP, yytext()); }
{cmp}           { return symbol(sym.CMP, yytext()); }
{je}            { return symbol(sym.JE, yytext()); }
{jne}           { return symbol(sym.JNE, yytext()); }
{param}         { return symbol(sym.PARAM, yytext()); }
{push}          { return symbol(sym.PUSH, yytext()); }
{pop}           { return symbol(sym.POP, yytext()); }

/* Registros */
{ax}            { return symbol(sym.AX, yytext()); }
{bx}            { return symbol(sym.BX, yytext()); }
{cx}            { return symbol(sym.CX, yytext()); }
{dx}            { return symbol(sym.DX, yytext()); }
{ac}            { return symbol(sym.AC, yytext()); }

/* Argumentos int */
{int20h}        { return symbol(sym.INT20H, yytext()); }
{int10h}        { return symbol(sym.INT10H, yytext()); }
{int09h}        { return symbol(sym.INT09H, yytext()); }

/* Caracteres no reconocidos */
.                     { System.err.println("Carácter ilegal: " + yytext() + " en línea " + yyline); }
