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

public class MaxArgsVisitor implements IVisitor<Integer> {

	@Override
	public Integer visit(Stm s) {
		return s.accept(this);
	}

	@Override
	public Integer visit(AssignStm s) {
		return s.getExp().accept(this);
	}

	@Override
	public Integer visit(CompoundStm s) {
		int arg1 = s.getStm1().accept(this);
		int arg2 = s.getStm2().accept(this);
		
		return Math.max(arg1, arg2);
	}

	@Override
	public Integer visit(PrintStm s) {
		return s.getExps().accept(this);
	}

	@Override
	public Integer visit(Exp e) {
		return e.accept(this);
	}

	@Override
	public Integer visit(EseqExp e) {
		int arg1 = e.getStm().accept(this);
		int arg2 = e.getExp().accept(this);
		
		return Math.max(arg1, arg2);
	}

	@Override
	public Integer visit(IdExp e) {
		return 0;
	}

	@Override
	public Integer visit(NumExp e) {
		return 0;
	}

	@Override
	public Integer visit(OpExp e) {
		return 0;
	}

	@Override
	public Integer visit(ExpList el) {
		return el.accept(this);
	}

	@Override
	public Integer visit(PairExpList el) {
		if(el.getHead() instanceof EseqExp) {
			int arg1 = el.getHead().accept(this);
			int arg2 = el.getTail().accept(this);
			
			return Math.max(arg1, arg2);
		} else {
			return 1 + el.getTail().accept(this);
		}
	}

	@Override
	public Integer visit(LastExpList el) {
		if(el.getHead() instanceof EseqExp) {
			return el.getHead().accept(this);
		} else {
			return 1;
		}
	}
	

}
