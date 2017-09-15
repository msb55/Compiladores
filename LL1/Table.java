package br.ufpe.cin.if688.table;


import br.ufpe.cin.if688.parsing.analysis.*;
import br.ufpe.cin.if688.parsing.grammar.*;
import java.util.*;


public final class Table {
	private Table() {    }

	public static Map<LL1Key, List<GeneralSymbol>> createTable(Grammar g) throws NotLL1Exception {
        if (g == null) throw new NullPointerException();

        Map<Nonterminal, Set<GeneralSymbol>> first = SetGenerator.getFirst(g);
        Map<Nonterminal, Set<GeneralSymbol>> follow = SetGenerator.getFollow(g, first);

        Map<LL1Key, List<GeneralSymbol>> parsingTable =  new HashMap<LL1Key, List<GeneralSymbol>>();

        /*
         * Implemente aqui o método para retornar a parsing table
         */
        
        Collection<Nonterminal> nts = g.getNonterminals();
        Iterator<Nonterminal> it = nts.iterator();
        Nonterminal nt = null;
        
        Set<GeneralSymbol> first_nt = null;
        Iterator<GeneralSymbol> it_first_nt = null;
        Set<GeneralSymbol> follow_nt = null;
        Iterator<GeneralSymbol> it_follow_nt = null;
        GeneralSymbol gn = null;
        
        ArrayList<List<GeneralSymbol>> prod = null;
        
        while(it.hasNext()) { // para cada nao terminal da gramatica
        	nt = it.next();
        	first_nt = first.get(nt); // lista de first para o nao terminal nt
        	prod = SetGenerator.productionsByNonterminal(nt, g.getProductions()); // toads as producoes do nao terminal nt
        	
        	it_first_nt = first_nt.iterator();
        	while(it_first_nt.hasNext()) { // cada terminal do first
        		gn = it_first_nt.next();
        		if(!gn.equals(SpecialSymbol.EPSILON)) { // caso nao seja vazio, colocar a producao na linha e coluna correta
        			parsingTable.put(new LL1Key(nt, gn), findProduction(g,prod,gn));
        		} else { // caso seja vazio...
        			follow_nt = follow.get(nt); // pegar todos os follows
        			it_follow_nt = follow_nt.iterator();
        			
        			while(it_follow_nt.hasNext()) {
        				GeneralSymbol gn_aux = it_follow_nt.next();
        				parsingTable.put(new LL1Key(nt, gn_aux), findProduction(g,prod,gn)); // colocar a producao que gera vazio na(s) coluna(s) correspondente aos elementos do follow
        			}
        		}
        	}
        }
        
        return parsingTable;
    }
	
	public static List<GeneralSymbol> findProduction(Grammar g, ArrayList<List<GeneralSymbol>> productions, GeneralSymbol first) {
		for(List<GeneralSymbol> list : productions) {
			Iterator<GeneralSymbol> it = SetGenerator.firstProduction(g, list).iterator();
			while(it.hasNext()) {
				if(it.next().equals(first)) return list;
			}
		}
		
		return null;
	}
}
