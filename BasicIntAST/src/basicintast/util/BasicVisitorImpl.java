/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicintast.util;

import basicintast.parser.BasicBaseVisitor;
import basicintast.parser.BasicLexer;
import basicintast.parser.BasicParser;
import java.util.Scanner;
import org.antlr.v4.runtime.misc.ParseCancellationException;

/**
 *
 * @author wellington
 */
public class BasicVisitorImpl extends BasicBaseVisitor<Object> {

    @Override
    public Object visitIfStmt(BasicParser.IfStmtContext ctx) {
        Boolean cond = (Boolean) visit(ctx.condExpr());
        if (cond) {
            return visit(ctx.b1);
        }
        return null;
    }

    @Override
    public Object visitIfElseStmt(BasicParser.IfElseStmtContext ctx) {
        Boolean cond = (Boolean) visit(ctx.condExpr());
        
        if (cond) {
            return visit(ctx.b1);
        } else {
            return visit(ctx.b2);
        }
    }

    @Override
    public Object visitCondRelOp(BasicParser.CondRelOpContext ctx) {
        
        
        Object c = (Object) visit(ctx.expr(0));
        Object d = (Object) visit(ctx.expr(1));
        Double a = Double.parseDouble(c.toString());
        Double b = Double.parseDouble(d.toString());
        int op = ctx.relop.getType();
        switch (op) {
            case BasicLexer.EQ:
                return a.equals(b);
            case BasicLexer.NE:
                return !a.equals(b);
            case BasicLexer.LT:
                return a < b;
            case BasicLexer.GT:
                return a > b;
            case BasicLexer.LE:
                return a <= b;
            case BasicLexer.GE:
                return a >= b;
        }
        return null;
    }

    @Override
    public Object visitCondExpresion(BasicParser.CondExpresionContext ctx) {
        Double d = (Double) visit(ctx.expr());
        return d > 0;
    }

    @Override
    public Object visitPrintStr(BasicParser.PrintStrContext ctx) {
        String val = ctx.STR().getText();
        System.out.println(val);
        return 0d;
    }

    @Override
    public Object visitPrintExpr(BasicParser.PrintExprContext ctx) {
        Object o = visit(ctx.expr());
        System.out.println(o);
        return o;
    }

    @Override
    public Object visitReadID(BasicParser.ReadIDContext ctx) {
        Scanner s = new Scanner(System.in);
        Object[] value = new Object[2];
        value = SymbolsTable.getInstance().getSymbol(ctx.ID().getText());
        value[1] = s.next();
        return value;
    }

    @Override
    public Object visitAttrExpr(BasicParser.AttrExprContext ctx) {
        if(SymbolsTable.getInstance().getSymbol(ctx.var().getText()) != null){
                Object aux = visit(ctx.expr());
                if(aux == null){
                    aux = ctx.expr().getText();
                }
                
                Object[] var = SymbolsTable.getInstance().getSymbol(ctx.var().getText());
                    if(var[0].equals("float")){
                        var[1] = Float.parseFloat(aux.toString());
                    }else if(var[0].equals("integer")){           
                        var[1] = (int)Double.parseDouble(aux.toString());
                    }else if(var[0].equals("boolean")){
                        if(aux.toString().equals("true") || aux.toString().equals("false")){
                            var[1] = aux;
                        }else{
                            throw new ParseCancellationException("Tipo incorreto");
                        }
                    }else if(var[0].equals("string")){
                        if(aux.toString().startsWith("\"")){
                            var[1] = aux;
                        }else{
                            throw new ParseCancellationException("Tipo incorreto");
                        }
                    }

            }else{
                throw new ParseCancellationException("Variavel nunca criada");
            }
          
    return 0;
    }

    @Override
    public Object visitExprPlus(BasicParser.ExprPlusContext ctx) {
        Object a = (Object) visit(ctx.expr1());
        Object b = (Object) visit(ctx.expr());
        Double dd = Double.parseDouble(a.toString());
        Double cc = Double.parseDouble(b.toString());
        return dd+cc;
    }

    @Override
    public Object visitExprMinus(BasicParser.ExprMinusContext ctx) {
        Object a = (Object) visit(ctx.expr1());
        Object b = (Object) visit(ctx.expr());
        Double dd = Double.parseDouble(a.toString());
        Double cc = Double.parseDouble(b.toString());
        return dd-cc;
    }

    @Override
    public Object visitExpr1Empty(BasicParser.Expr1EmptyContext ctx) {
        return visit(ctx.expr1());
    }

    @Override
    public Object visitExpr1Mult(BasicParser.Expr1MultContext ctx) {
        Object a = (Object) visit(ctx.expr2());
        Object b = (Object) visit(ctx.expr());
        Double dd = Double.parseDouble(a.toString());
        Double cc = Double.parseDouble(b.toString());
        return dd * cc;
    }

    @Override
    public Object visitExpr1Div(BasicParser.Expr1DivContext ctx) {
        Object a = (Object) visit(ctx.expr2());
        Object b = (Object) visit(ctx.expr());
        Double dd = Double.parseDouble(a.toString());
        Double cc = Double.parseDouble(b.toString());
        return dd / cc;
    }

    @Override
    public Object visitExpr2Empty(BasicParser.Expr2EmptyContext ctx) {
        return visit(ctx.expr2());
    }

    @Override
    public Object visitExpr2Par(BasicParser.Expr2ParContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Object visitExpr2Num(BasicParser.Expr2NumContext ctx) {
        String num = ctx.NUM().getText();
        double retorno = Double.parseDouble(num);       
         
        return retorno;
    }

    @Override
    public Object visitExpr2ID(BasicParser.Expr2IDContext ctx) {
        Object[] var = new Object[2];
        var[1] = SymbolsTable.getInstance().getSymbol(ctx.ID().toString())[1];
        return var[1];
    }
    
    @Override
    public Object visitVarType(BasicParser.VarTypeContext ctx) {
        
        Object[] type = new Object[2];
        type[0] = ctx.simpletype().getText();
        SymbolsTable.getInstance().addSymbol(ctx.ID(0).getText(), type);
        
        return 0d;
        
        
        
    }
    
    @Override 
    public Object visitVarArrayType(BasicParser.VarArrayTypeContext ctx) {
            
            Object[] list = (Object[]) visit(ctx.arraytype());
            int inicial = Integer.parseInt(list[0].toString());
            int end = Integer.parseInt(list[1].toString());
            for(int i = inicial ; i<end ; i++){
                Object[] varData = new Object[2];
                varData[0] = list[2];
                String varName = ctx.ID(0).getText()+"["+i+"]";
                SymbolsTable.getInstance().addSymbol(varName, varData);
            }    
        return 0d;
    }
    
    @Override 
    public Object visitArraytypeIndexRange(BasicParser.ArraytypeIndexRangeContext ctx) {
        
        Object inicio = (Object) ctx.indexRange().children.get(0);
        Object end = (Object) ctx.indexRange().children.get(2);
        Object type = (Object) ctx.simpletype().getText();
        Object[] inicio_end = new Object[3] ;
        inicio_end[0] = inicio;
        inicio_end[1] = end;
        inicio_end[2] = type;
        return inicio_end;
    }
    
   @Override
   public Object visitIndexRangeNum(BasicParser.IndexRangeNumContext ctx) {
        
       
       return 0d;
    }
   
    @Override
   public Object visitWhileCondExpr(BasicParser.WhileCondExprContext ctx) {
       Boolean cond = (Boolean) visit(ctx.condExpr());
       while(cond){
       visit(ctx.b1);
       cond = (Boolean) visit(ctx.condExpr());
           
       }
       
       return 0d;
       
   }
   
    @Override
    public Object visitForCondExpr(BasicParser.ForCondExprContext ctx) {
       Boolean cond = (Boolean) visit(ctx.condExpr());
       while(cond){
       cond = (Boolean) visit(ctx.condExpr());
       visit(ctx.b2);
       visit(ctx.b1);      
           
       }
       
       return 0d;
       
   }
       
       
       
   }
   
    
