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

public class Interpreter implements IVisitor<Table> {
	
	//a=8;b=80;a=7;
	// a->7 ==> b->80 ==> a->8 ==> NIL
	private Table t;
	
	public Interpreter(Table t) {
		this.t = t;
	}

	@Override
	public Table visit(Stm s) {
		return s.accept(this);
	}

	/* 
	 * Para o problema do Interpreter, eu entendi que a unica possibilidade de modificacao em Table sao os casos onde se encontra
	 * AssignStm, que significa a atribuicao de algum valor em alguma varialvel do programa.
	*/
	@Override
	public Table visit(AssignStm s) {
		String id = s.getId();
		IntAndTableVisitor itv = new IntAndTableVisitor(this.t);
		IntAndTable it = itv.visit(s.getExp());

		this.t = new Table(id, it.result, this.t);
		
		return this.t;
	}

	/*
	 * Alguns metodos eu retorno null porque nao havia dependencia para quem chamou esse tipo de visit
	 * Como o exemplo do CompoundStm, apenas faco a chamada do metodo accept dos dois stms, neles podem haver acoes do tipo AssignStm
	 * que o metodo acima ja fica responsavel pela tarefa, e caso seja outro compound ou print nao se ve necessario o retorno de uma
	 * Table, uma vez que eles nao tem interacao com a mesma.
	 */
	@Override
	public Table visit(CompoundStm s) {
		s.getStm1().accept(this);
		s.getStm2().accept(this);
		return null;
	}

	@Override
	public Table visit(PrintStm s) {
		return s.getExps().accept(this);
	}

	@Override
	public Table visit(Exp e) {
		return e.accept(this);
	}

	/*
	 * Nesse caso, como havia explicado no metodo CompoundStm, do lado do stm do EseqExp pode haver alteracao na tabela (com o Assign)
	 * mas isso ja fica tratado com o proprio metodo de AssignStm, nao havendo necessidade de novo de manipular a Table.
	 * Para os casos de Compound e Print a responsabilidade eh apenas passada para os metodos.
	 * 
	 * Do lado da exp eu retorno a tabela resultante uma vez que EseqExp pode estar envolvida em algum AssignStm como por exemplo:
	 * b=(print(a,a-1),10*a), no caso eh realizado a acao do print, e a expressao eh utilizada como o valor de b.
	 */
	@Override
	public Table visit(EseqExp e) {
		e.getStm().accept(this);
		return e.getExp().accept(this);
	}

	/*
	 * Foi criado um metodo na classe Table para percorrer a estrutura e encontrar a primeira referencia ao ID informado, retornando
	 * assim a tabela encontrada (id, value)
	 */
	@Override
	public Table visit(IdExp e) {
		return this.t.findById(e.getId());
	}

	/*
	 * Para o caso de ser chamado o visit de NumExp na propria classe Interpreter, no caso eu apenas retorno um objeto Table com
	 * o value settado, e para quem o chamou, trata esse caso de apenas pegar o valor de value do objeto retornado.
	 */
	@Override
	public Table visit(NumExp e) {
		return new Table(null, e.getNum(), null);
	}

	/*
	 * Faco o uso de IntAndTableVisitor pois nessa classe contem o mesmo codigo de regra, evito a duplicacao e retorno em um objeto
	 * Table o resultado da operacao em value
	 */
	@Override
	public Table visit(OpExp e) {
		IntAndTableVisitor itv = new IntAndTableVisitor(this.t);
		IntAndTable it = itv.visit(e);
		
		return new Table(null, it.result, null);
	}

	@Override
	public Table visit(ExpList el) {
		return el.accept(this);
	}

	/*
	 * Como PairExpList e LastExpList sao apenas chamados por PrintStm, posso utilizar o System.out.println() para executar o comando
	 * de print.
	 */
	@Override
	public Table visit(PairExpList el) {
		System.out.println(el.getHead().accept(this).value);
		el.getTail().accept(this);
		return null;
	}

	@Override
	public Table visit(LastExpList el) {
		System.out.println(el.getHead().accept(this).value);
		return null;
	}
}