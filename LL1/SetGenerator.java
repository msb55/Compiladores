package br.ufpe.cin.if688.parsing.analysis;

import java.security.GeneralSecurityException;
import java.util.*;

import br.ufpe.cin.if688.parsing.grammar.*;


public final class SetGenerator {
    
    public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {
        
    	if (g == null) throw new NullPointerException("g nao pode ser nula.");
        
    	Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);
    	/*
    	 * Implemente aqui o método para retornar o conjunto first
    	 */
    	
    	Nonterminal nt = null;
    	
    	Iterator<Nonterminal> nts = first.keySet().iterator();
    	while(nts.hasNext()) {
    		nt = nts.next();    		
    		first.put(nt, first(g, nt));
    	}

        return first;
    	
    }
    
    public static Set<GeneralSymbol> first(Grammar g, Nonterminal nt) {
    	Collection<Production> productions = g.getProductions();
    	ArrayList<List<GeneralSymbol>> ntProductions = productionsByNonterminal(nt, productions);//retornar uma lista de producoes para um dado nao terminal
    	Set<GeneralSymbol> output = new HashSet<GeneralSymbol>();
    	
    	for(List<GeneralSymbol> list : ntProductions) {
    		output.addAll(firstProduction(g, list));
    	}
		
		return output;//retornar uma lista de terminais que seriam o 'first' de um nao terminal
    }

	public static Set<GeneralSymbol> firstProduction(Grammar g, List<GeneralSymbol> list) {
		Set<GeneralSymbol> output = new HashSet<GeneralSymbol>();
		
		for(GeneralSymbol gs : list) {
			if(gs instanceof Terminal || gs instanceof SpecialSymbol) {
				output.add(gs);
				return output;
			} else if(gs instanceof Nonterminal) {
				Set<GeneralSymbol> aux = first(g, (Nonterminal) gs);	
				
				if(!aux.contains(SpecialSymbol.EPSILON)) {
					output.addAll(aux);
					return output;
				} else {
					aux.remove(SpecialSymbol.EPSILON);
					output.addAll(aux);
				}				
			}
		}
		
		return output;
	}
    
    public static ArrayList<List<GeneralSymbol>> productionsByNonterminal(Nonterminal nt, Collection<Production> productions) {
    	ArrayList<List<GeneralSymbol>> output = new ArrayList<List<GeneralSymbol>>();
    	Iterator<Production> it = productions.iterator();
    	Production p = null;
    	while(it.hasNext()) {
    		p = it.next();    		
    		if(p.getNonterminal().equals(nt)) output.add(p.getProduction()); // para caso um nao terminal tenha mais de uma producao    		
    	}
    	
    	return output;
    }

    
    public static Map<Nonterminal, Set<GeneralSymbol>> getFollow(Grammar g, Map<Nonterminal, Set<GeneralSymbol>> first) {
        
        if (g == null || first == null)
            throw new NullPointerException();
                
        Map<Nonterminal, Set<GeneralSymbol>> follow = initializeNonterminalMapping(g);
        
        /*
         * implemente aqui o método para retornar o conjunto follow
         */
        
        Iterator<Nonterminal> nts = follow.keySet().iterator();
        Nonterminal nt = null;
        while(nts.hasNext()) {
        	nt = nts.next();
        	follow.put(nt, follow(g, nt, first));
        }
        
        //simbolo inicial => $
        //Naoterminal no final da producao = FIRST do nao terminal 'dono' da producao (nao calcular para casos de recursao)
        //Naoterminal seguido de Naoterminal, FOLLOW = FIRST.
        
        return follow;
    }
    
    public static Set<GeneralSymbol> follow(Grammar g, Nonterminal nt, Map<Nonterminal, Set<GeneralSymbol>> first) {
    	Collection<Production> productions = g.getProductions();
    	ArrayList<Production> ntProductions = getProductionsByNonterminal(nt, productions);//retornar uma lista de producoes que um dado nao terminal apareca
    	Set<GeneralSymbol> output = new HashSet<GeneralSymbol>();
    	
    	if(g.getStartSymbol().equals(nt)) output.add(SpecialSymbol.EOF);
    	
    	for(Production prod : ntProductions) {
    		List<GeneralSymbol> lista = prod.getProduction();
    		int index = lista.indexOf(nt);    		
    		
    		if((index+1) >= lista.size()) {
    			output.addAll(follow(g, prod.getNonterminal(), first));
    		} else if(lista.get(index+1) instanceof Terminal) {
    			output.add(lista.get(index+1));
    		} else {
    			Set<GeneralSymbol> aux = first.get(lista.get(index+1));
    			if(aux.contains(SpecialSymbol.EPSILON)) {
    				output.addAll(follow(g, prod.getNonterminal(), first));
    			}
    			aux.remove(SpecialSymbol.EPSILON);
    			Iterator<GeneralSymbol> it = aux.iterator();
    			while(it.hasNext()) {
    				GeneralSymbol current = it.next();
    				if(!current.equals(SpecialSymbol.EPSILON)) output.add(current);
    			}				   			
    		}    		
    	}
		
		return output;//retornar uma lista de terminais que seriam o 'follow' de um nao terminal    	
    }
    
    public static ArrayList<Production> getProductionsByNonterminal(Nonterminal nt, Collection<Production> productions) {
    	ArrayList<Production> output = new ArrayList<Production>();
    	Iterator<Production> it = productions.iterator();
    	Production p = null;
    	while(it.hasNext()) {
    		p = it.next();    		
    		if(!p.getNonterminal().equals(nt) && p.getProduction().contains(nt)) output.add(p); // para caso um nao terminal apareca em mais de uma producao (nao sendo a dele)
    	}
    	
    	return output;
    }
    
    
    //método para inicializar mapeamento nãoterminais -> conjunto de símbolos
    private static Map<Nonterminal, Set<GeneralSymbol>> initializeNonterminalMapping(Grammar g) {
	    Map<Nonterminal, Set<GeneralSymbol>> result = 
	        new HashMap<Nonterminal, Set<GeneralSymbol>>();
	
	    for (Nonterminal nt: g.getNonterminals())
	        result.put(nt, new HashSet<GeneralSymbol>());
	
	    return result;
    }

} 
