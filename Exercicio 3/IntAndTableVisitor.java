package br.ufpe.cin.if688.visitor;

import br.ufpe.cin.if688.ast.AssignStm;
import br.ufpe.cin.if688.ast.CompoundStm;
import br.ufpe.cin.if688.ast.EseqExp;
import br.ufpe.cin.if688.ast.Exp;
import br.ufpe.cin.if688.ast.ExpList;
import br.ufpe.cin.if688.ast.IdExp;
import br.ufpe.cin.if688.ast.LastExpList;
import br.ufpe.cin.if688.ast.NumExp;
import br.ufpe.cin.if688.ast.OpExp;
import br.ufpe.cin.if688.ast.PairExpList;
import br.ufpe.cin.if688.ast.PrintStm;
import br.ufpe.cin.if688.ast.Stm;
import br.ufpe.cin.if688.symboltable.IntAndTable;
import br.ufpe.cin.if688.symboltable.Table;

/*
 * A classe estah bem semelhante a Interpreter pela forma como ambas funcionam, mudando o tipo de retorno
 */
public class IntAndTableVisitor implements IVisitor<IntAndTable> {
	private Table t;

	public IntAndTableVisitor(Table t) {
		this.t = t;
	}

	@Override
	public IntAndTable visit(Stm s) {
		return s.accept(this);
	}

	@Override
	public IntAndTable visit(AssignStm s) {
		String id = s.getId();
		IntAndTable it = s.getExp().accept(this);
		
		this.t = new Table(id, it.result, this.t);
		return null;
	}

	@Override
	public IntAndTable visit(CompoundStm s) {
		s.getStm1().accept(this);
		s.getStm2().accept(this);
		return null;
	}

	@Override
	public IntAndTable visit(PrintStm s) {
		return s.getExps().accept(this);
	}

	@Override
	public IntAndTable visit(Exp e) {
		return e.accept(this);
	}

	@Override
	public IntAndTable visit(EseqExp e) {
		e.getStm().accept(this);
		return e.getExp().accept(this);
	}

	@Override
	public IntAndTable visit(IdExp e) {
		Table result = t.findById(e.getId());
		return new IntAndTable(result.value, t);
	}

	@Override
	public IntAndTable visit(NumExp e) {
		return new IntAndTable(e.getNum(), t);
	}

	@Override
	public IntAndTable visit(OpExp e) {
		IntAndTable el = e.getLeft().accept(this);
		IntAndTable er = e.getRight().accept(this);
		int result=0, left, right;
		
		if(el != null && er != null) {
			left = el.result;
			right = er.result;
		} else {
			throw new RuntimeException();
		}
		
		switch (e.getOper()) {
		case 1:
			result = left + right;
			break;
			
		case 2:
			result = left - right;
			break;
			
		case 3:
			result = left * right;
			break;
			
		case 4:
			result = left / right;
			break;
		}
		
		return new IntAndTable(result, t);
	}

	@Override
	public IntAndTable visit(ExpList el) {
		return el.accept(this);
	}

	@Override
	public IntAndTable visit(PairExpList el) {
		System.out.println(el.getHead().accept(this).result);
		el.getTail().accept(this);
		return null;
	}

	@Override
	public IntAndTable visit(LastExpList el) {
		System.out.println(el.getHead().accept(this).result);
		return null;
	}


}
