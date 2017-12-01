/******************************************************
 * A multi-line Javadoc-like comment about my grammar *
 ******************************************************/
grammar Basic;

@header{
package basicintast.parser;
import basicintast.util.*;
}

@members{
String traducao = "";
String aux = "";
char open = 123;
char close = 125;
String num = "";
String num2 = "";



}


program : PROGRAM {traducao+="#include<iostream>\n";
                   traducao+="#include<string>\n";
                                  traducao+="using namespace std;\n";
                                  traducao+="int main()"+open+"\n"; 
 
}  
STR ';' block             #programBlock
        ;

block   : var_Declarion? procedure? stmt1   #blockVarProcStmt
        ;

//Variaveis
var_Declarion : VAR var+                    #varDecvar
        ;

var     : ID (',' ID )* ':=' simpletype {traducao+=$ID.text+";"+"\n";}  #varType
        | ID {aux+=$ID.text;} (',' ID)* ':=' arraytype        #varArrayType
        | ID                                #varID 
        | ID '['expr']'                     #indexedVar
        ;

simpletype : 
          INTEGER      {traducao+="int ";}                     #typeInteger
        | FLOAT        {traducao+="float ";}                    #typeFloat
        | STRING       {traducao+="string ";}                     #typeSrt
        | BOOLEAN      {traducao+="bool ";}                    #typeBoolean
        ;


arraytype: ARRAY '['indexRange']' OF simpletype{traducao+=aux+"["+num2+"-"+num+"];"+"\n";}  #arraytypeIndexRange
        ;
        
indexRange: NUM{num+=$NUM.text;}'..' NUM {num2+=$NUM.text;}                  #indexRangeNum
        ;

procedure:
        ;
stmt1   : BEGIN stmt*  END'.'  #stmt1Stmt
        | BEGIN stmt*  END {{traducao+="return 0;"+close;}}{System.out.println(traducao);}{GravarArquivo.get(traducao);} #stmt1Stmt1
        ;

stmt    :  
        write             #stmtWrite
        | read              #stmtRead
        | attr              #stmtAttr
        | expr              #stmtExpr
        | cond              #stmtCond
        | whilexd           #stmtWhile
        | forxd             #stmtFor    
        ;

whilexd : WHILE {traducao+="while(";} '(' condExpr ')'{traducao+=")";} DO {traducao+=open+"\n";} b1=stmt1 {traducao+=close;} #whileCondExpr 
        ;

forxd   : FOR {traducao+="for(";;} '(' attr? {traducao+=";";} EOL condExpr {traducao+=";";}EOL b2 = attr2 ')' {traducao+=")"+open;}  b1=stmt1 {traducao+=close;} #forCondExpr
        ;


cond    : IF {traducao+="if(";}'('condExpr')' {traducao+=")"+open;} THEN b1=stmt1 {traducao+=close;}                 #ifStmt
        | IF {traducao+="if(";} '('condExpr')'{traducao+=")"+open;} THEN b1=stmt1 {traducao+=close;}ELSE {traducao+="else"+open;} b2=stmt1  {traducao+=close;}    #ifElseStmt 
        ;

condExpr: expr                                              #condExpresion
        | expr relop=('>'|'<'|'=='|'>='|'<='|'!='){traducao+=$relop.text;} expr     #condRelOp
        ;

//block1   : '{' program '}'   #blockStmt
//       ;

write   : WRITE STR   {traducao+="cout<<"+$STR.text+"<<endl;"+"\n";}      #printStr
        | WRITE  {traducao+="cout<<";} expr {traducao+="<<endl;"+"\n";}      #printExpr
        ;

read    : READ ID {traducao+="cin>>"+$ID.text+";"+"\n";}  #readID
        ;

attr    :   var {traducao+=$var.text+"=";} ':=' expr {traducao+=";"+"\n";}   #attrExpr
        
        
        ;
attr2   :    var {traducao+=$var.text+"=";} ':=' expr  #attrExpr1
        
        
        ; 

expr    : expr1 '+'{traducao+="+";} expr    #exprPlus
        | expr1 '-'{traducao+="-";} expr    #exprMinus
        | expr1             #expr1Empty
        ;

expr1   : expr2 '*'{traducao+="*";} expr    #expr1Mult
        | expr2 '/'{traducao+="/";} expr    #expr1Div
        | expr2             #expr2Empty
        ;

expr2   : '(' expr ')'      #expr2Par
        | NUM  {traducao+=$NUM.text;}             #expr2Num
        | ID   {traducao+=$ID.text;}            #expr2ID
        | STR  {traducao+=$STR.text;}           #expr2STR
        | boolean1          #expr2Bool   
        ;

boolean1:TRUE   {traducao+=$TRUE.text;}                #boolTrue
        |FALSE  {traducao+=$FALSE.text;}                #boolFalse
; 



//TOKENS
FALSE   : 'false';
TRUE    : 'true';
THEN    : 'then';
STRING  : 'string';
WHILE   : 'while';
FOR     : 'for';
DO      : 'do';
BEGIN   : 'begin';
END     : 'end';
WRITE   : 'write';
PROGRAM : 'program';
VAR     : 'var';
INTEGER : 'integer';
FLOAT   : 'float';
BOOLEAN : 'boolean';
ARRAY   : 'array';
OF      : 'of';
IF      : 'if';
ELSE    : 'else';
GT      : '>' ;
LT      : '<' ;
EQ      : '==';
GE      : '>=';
LE      : '<=';
NE      : '!=';
PLUS    : '+' ;
MINUS   : '-' ;
MULT    : '*' ;
DIV     : '/' ;
OPEN    : '(' ;
CLOSE   : ')' ;
OPEN_BL : '{' ;
CLOSE_BL: '}' ;
IS      : '=' ;
EOL     : ';' ;
PRINT   : 'print' ;
READ    : 'read' ;
NUM     : [0-9]+('.'[0-9]+)? ;
ID      : [a-zA-Z][a-zA-Z0-9_]*('['[0-9]']')?;
STR     : '"' ('\\' ["\\] | ~["\\\r\n])* '"';
WS      : [\n\r \t]+ -> skip;