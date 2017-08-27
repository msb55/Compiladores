%%

/* Não altere as configurações a seguir */

%line
%column
%unicode
//%debug
%public
%standalone
%class Minijava
%eofclose

/* Insira as regras léxicas abaixo */

fimLinha 			= \r|\n|\r\n
entradaChar 		= [^\r\n]
whitespace 			= {fimLinha} | [ \t\f]
comentario 			= {comentarioComposto} | {comentarioSimples}
comentarioComposto	= "/*" [^*] ~"*/" | "/*" "*"+ "/"
comentarioSimples	= "//" {entradaChar}* {fimLinha}?
letter 				= [A-Za-z_]
semzero				= [1-9]
digit 				= [0]|{semzero}
identificador 		= {letter}({letter}|{digit})*
literal				= [0]|{semzero}({digit})*

%%

/**
* PALAVRAS RESERVADAS
*/
boolean				{ System.out.println("Token BOOLEAN"); }
class				{ System.out.println("Token CLASS"); }
public				{ System.out.println("Token PUBLIC"); }
extends				{ System.out.println("Token EXTENDS"); }
static				{ System.out.println("Token STATIC"); }
void				{ System.out.println("Token VOID"); }
main				{ System.out.println("Token MAIN"); }
String				{ System.out.println("Token STRING"); }
int					{ System.out.println("Token INT"); }
while				{ System.out.println("Token WHILE"); }
if					{ System.out.println("Token IF"); }
else				{ System.out.println("Token ELSE"); }
return				{ System.out.println("Token RETURN"); }
length				{ System.out.println("Token LENGTH"); }
true				{ System.out.println("Token TRUE"); }
false				{ System.out.println("Token FALSE"); }
this				{ System.out.println("Token THIS"); }
new					{ System.out.println("Token NEW"); }
System.out.println	{ System.out.println("Token SYSTEM.OUT.PRINTLN"); }

/**
* OPERADORES
*/

"&&"	{ System.out.println("Token &&"); }
"<"		{ System.out.println("Token <"); }
"=="	{ System.out.println("Token =="); }
"!="	{ System.out.println("Token !="); }
"+"		{ System.out.println("Token +"); }
"-"		{ System.out.println("Token -"); }
"*"		{ System.out.println("Token *"); }
"!"		{ System.out.println("Token !"); }
    
    
/**
* DELIMITADORES
*/

";"		{ System.out.println("Token ;"); }
"."		{ System.out.println("Token ."); }
","		{ System.out.println("Token ,"); }
"="		{ System.out.println("Token ="); }
"("		{ System.out.println("Token ("); }
")"		{ System.out.println("Token )"); }
"{"		{ System.out.println("Token {"); }
"}"		{ System.out.println("Token }"); }
"["		{ System.out.println("Token ["); }
"]"		{ System.out.println("Token ]"); }

{comentario}		{ /* Ignorar comentario. */ }
{whitespace}    	{ /* Ignorar whitespace. */ }
{identificador}    	{ System.out.println("Token ID " + yytext()); }
{literal}       	{ System.out.println("Token LITERAL"); }
    
/* Insira as regras léxicas no espaço acima */     
     
. { throw new RuntimeException("Caractere ilegal! '" + yytext() + "' na linha: " + yyline + ", coluna: " + yycolumn); }
