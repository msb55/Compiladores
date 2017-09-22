package br.ufpe.cin.if688.symboltable;

public class Table {
	public String id;
	public int value;
	public Table tail;
	public Table(String i, int v, Table t) {
		id = i;
		value = v;
		tail = t;
	}
	
	/*
	 * Adicionei esse metodo apenas pra facilitar a busca por algum item na tabela, sem precisar utilizar um for para cada uso.
	 */
	public Table findById(String id) {
		if(this.id.equals(id)) return this;
		else {
			if(this.tail != null) return tail.findById(id);
			else return null;
		}
	}
}
